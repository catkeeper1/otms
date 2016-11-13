define([
	"dojo/_base/declare",
	"dijit/Dialog"

], function(declare, dialog){

	return declare( [dialog], {
		
		postCreate: function(){
			
			this.inherited(arguments);
			
			console.log(this);
			
		}
	});
	
	

	
});