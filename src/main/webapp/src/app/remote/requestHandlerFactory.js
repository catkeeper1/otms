define([
	"dojo/Deferred",
	"../util/lang",
	"./spinFactory",
	"dojo/json",
	"dijit/Dialog",
	"../widget/winUtil"
], function(Deferred, appLang, spinFactory,JSON, Dialog,winUtil){
	
	
	return new function(){
	
		console.log("requestHandlerFactory is created.");
		
		this.createRequestHandler= function(opts){
			
			return new function(){
				
				var showErrorDialog = function(errorContent){
					winUtil.alert(errorContent, "Error");
					
				};
				
				
				this.showSysErrorMsg = function(errData){
					
					
					var errorContent = "";
					
					if(errData.exceptionId != null && errData.exceptionId != ""){
						errorContent= errorContent + "System exception. Exception ID is " + errData.exceptionId +"<br/>";
					}
					
					if(errData.messageList != null){
						
						for(var i = 0; i < errData.messageList.length; i++){
							var errMsg = errData.messageList[i];
							
							errorContent = errorContent + errMsg.message + "<br/>";
							
						}
						
					}
					
					showErrorDialog(errorContent);
					
				};
				
				this.showHttpErrorMsg = function(err){
					
					var errorContent ="Invalid HTTP status " + err.status;
					
					showErrorDialog(errorContent);
					
				};
				
				
				//this statement must be after showHttpErrorMsg() and showHttpErrorMsg()
				appLang.mixin(this, opts);
				
				
				var currentHandler = this;
				
				
				
				//Other user can customize the spin by passing another spin object through opts.
				//spin object must implement showSpin() and closeSpin();
				//if no cusomitzed spin is specified, system create a default spin object through spinFactory.
				if(this.spin == null){
					this.spin = spinFactory.createSpin({
												blockedScreen:this.blockedScreen
											});
				}
				
				this.attachToRequest = function(response){
					
					this.spin.showSpin();
	
					var curSpin = this.spin;
					
					var doCloseSpin = function(res){
						curSpin.closeSpin();
						
						return res;
					};
					
					//cannot call always() here since JsonRest return dojo/_base/Deffered.
					response.then(doCloseSpin, doCloseSpin, doCloseSpin);
					
					
					//var result = new Deferred();
					
					response.then(function(res){
						
						//result.resolve(res);
						
						return res;
					
					}, function(err){
						
						console.log("http request " + err.response.url +" is rejected.");
						
						//convert the data from string to js object since the xhr(used by JsonRest) request only return string data.
						if( typeof err.response.data =="string"){
							
							try{
								var tempData = JSON.parse(err.response.data, true);
								err.response.data = tempData;
							}catch(e){
							
							}
							
						}
						
						if(err.response.status == 401 && typeof err.response.getHeader == 'function' && err.response.getHeader("needAuthentication") == "true"){
							
							document.location ="loginPage.do?msg=TO";
						
							return null;			
						}
						
						
						if(err.response.status == 500 && err.response.getHeader("isSystemException") == "true"){
							
							if(typeof currentHandler.showSysErrorMsg == 'function'){
								currentHandler.showSysErrorMsg(err.response.data);
								err.handled = true;
							}
							
						} else {//if(err.response.status >= 400 || err.response.status <= 505 )
							
							console.log("invalid http status:" + err.response.status +" for url "+ err.response.url);
							
							if(typeof currentHandler.showHttpErrorMsg == 'function'){
								currentHandler.showHttpErrorMsg(err.response);
								err.handled = true;
							}
						}
						
						//result.reject(err);
						
						return err;
					});
				
					return response;
					
				};
				
				
			};
			
		};
		
		
	};
		
	
	
		
});