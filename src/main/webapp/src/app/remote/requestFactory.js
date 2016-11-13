define([
	"dojo/Deferred",
	"dojo/request",
	"../util/lang",
	"./requestHandlerFactory"
], function(Deferred, request, appLang, requestHandlerFactory){
	
	
	return new function(){
	
		console.log("requestfactory is created.");
		
		
		this.createRequest= function(reqHandleOpts){
			
			return new function(){
				
				this.makeRequest = function(url, options){
					
					
	
					options = appLang.mixin({handleAs:"json"}, options);
					
					if(options.headers == null){
						options = appLang.mixin({headers : { 'Content-Type': 'application/json' }}, options); 
					}
					
					var response = request(url,  options).response;
					
					var handler = requestHandlerFactory.createRequestHandler(reqHandleOpts);
	      			
	      			return handler.attachToRequest(response);
				};
				
				
				
				this.get = function(url, options){
					
					return this.makeRequest(url, appLang.mixin(options, {method:"get"}));
					
				};
				
				this.post = function(url, options){
					
					return this.makeRequest(url, appLang.mixin(options, {method:"post"}));
					
				};
				
				this.put = function(url, options){
					
					return this.makeRequest(url, appLang.mixin(options, {method:"put"}));
					
				};
				
				this.del = function(url, options){
					
					return this.makeRequest(url, appLang.mixin(options, {method:"del"}));
					
				};
				
			};
			
		};
		
		
	};
		
	
	
		
});