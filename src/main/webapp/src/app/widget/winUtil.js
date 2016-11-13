define([
	"dijit/Dialog",
	"dijit/ConfirmDialog",
	"dojo/aspect",
	"dojo/Deferred"
], function(Dialog, ConfirmDialog, aspect, Deferred){

	return new function() {
		
		
		/*
		this.prompt = function(blockedScreen, msgs){
			
			
		var html = '<div widgetid="dijit_Dialog_0"  class="dijitDialogFixed" role="dialog">' + 
						'<div class="dijitDialogTitleBar">' + 
							'<span class="dijitDialogTitle" role="heading">'+ '' + '</span>' +
							'<span class="dijitDialogCloseIcon" role="button">'+
								
							'</span>' + 
						'</div>' +
						'<div data-dojo-attach-point="containerNode" class="dijitDialogPaneContent">'+msgs+'</div>'+
						
					'</div>';
			
			//html = '<div> 12345</div>';
			
			var spin = new StandBy({
        					target:blockedScreen.domNode,
        					text: html,
        					centerIndicator: "text"
    					});
						
			document.body.appendChild(spin.domNode);
   			
   			
   			
   			spin.startup();
   			spin.show();
			
		};
		*/
		
		this.alert = function(msg, title){
			
			if(title == null){
				title = "";
			}
			
			var alertDialog = new Dialog({
	        				title: title,
	        				content: msg
       					});
       		
       		//the dojo doc said onCancel cannot be override, so use below approach to capture onCancel event;
			alertDialog.own(aspect.after(alertDialog, 
       					 				 "onCancel", 
       					 				 function(){
											//destroy the dialog after the animation is finished. The animation must be finished in 2 seconds.
	        			 					window.setTimeout(function(){alertDialog.destroyRecursive(); } , 2000);
	        			 					return true;
	        			 				}, 
	        			 				true));
       					
       		alertDialog.show();	
			
		};
		
		
		this.confirm = function(msg, title){
			
			if(title == null){
				title = "";
			}
			
			var result = new Deferred();
			
			var alertDialog = new ConfirmDialog({
	        				title: title,
	        				content: msg
       					});
       		
			var destoryDialog = function(dia){
				//destroy the dialog after the animation is finished. The animation must be finished in 2 seconds.
			 	window.setTimeout(function(){dia.destroyRecursive(); } , 2000);
			};
			
       		//the dojo doc said onCancel cannot be override, so use below approach to capture onCancel event;
       		aspect.after(alertDialog, 
       					 "onCancel", 
       					 function(){
       						
       						console.log("cancel the confirm dialog.");
       						
       						destoryDialog(alertDialog);
	        				
       						result.cancel();
       						
	        				return true;
	        			 }, 
	        			 true);
       		
       		
       		aspect.after(alertDialog, 
  					 "onExecute", 
  					 function(){
  						
  						console.log("click 'ok' button in the confirm dialog.");
  						
  						destoryDialog(alertDialog);
  						
  						result.resolve();
  						
  						return true;
       			 	}, 
       			 	true);
       		
       					
       		alertDialog.show();
       		
       		return result;
			
		};
		
	};
	
	
});