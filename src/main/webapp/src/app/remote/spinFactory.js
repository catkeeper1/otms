define([
	"../util/lang",
	"dojox/widget/Standby"
], function(appLang, StandBy){
	
	
	return new function(){
	
		console.log("spinFactory is created.");
		
		this.createSpin= function(params){
			
			return new function(){
				
				appLang.mixin(this, params);
				
				this.showSpin = function(){
				
					if(this.blockedScreen != null){
						
						this.closeSpin();
						
						this._spin = new StandBy({
        					target:this.blockedScreen.domNode
    					});
						
						document.body.appendChild(this._spin.domNode);
   						this._spin.startup();
   						this._spin.show();
					}
				};
				
				this.closeSpin = function(){
					
					if(this._spin != null){
						
						this._spin.hide();
						this._spin.destroyRecursive();
						this._spin = null;
						
					}
					
				};
				
				
				
			};
			
		};
		
		
	};
		
	
	
		
});