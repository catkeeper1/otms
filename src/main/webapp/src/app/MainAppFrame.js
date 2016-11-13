define([
	"dojo/_base/declare",
	"dijit/_Container",
	"dijit/form/Button",
	"dijit/layout/BorderContainer", "dijit/layout/ContentPane",
	"dijit/_WidgetBase",
	"dojo/store/Memory",
    "dijit/tree/ObjectStoreModel", "dijit/Tree",
    "app/remote/requestFactory",
    "app/remote/jsonRestFactory",
    "dojo/json",
    //
    "app/widget/winUtil",
    "dijit/Dialog",
    "dojo/store/Cache",
    "dijit/layout/TabContainer",
	"./remote/spinFactory"
], function(declare, _Container,Button, BorderContainer,ContentPane, _WidgetBase, Memory, ObjectStoreModel, Tree, requestFactory,jsonRestFactory,JSON, winUtil,Dialog,Cache,TabContainer,spinFactory){

	return declare( [_WidgetBase,_Container], {
		
		postCreate: function(){
			
			
			var bc = new BorderContainer({
	        	style: "height: 100%; width: 100%;",
	        	liveSplitters: true,
	        	gutters:true
	    	});
	
	    	// create a ContentPane as the left pane in the BorderContainer
	    	var cpLeft = new ContentPane({
	        region: "left",
	        style: "width: 200px",
	        splitter:true
	    	});
	    	
	    	
	    	
	    	bc.addChild(cpLeft);
	
	    	// create a ContentPane as the center pane in the BorderContainer
	    	var cpRight = new TabContainer({
	        	region: "center",
	        	splitter:true
	    	});
	    	
	    	
	    	bc.addChild(cpRight);
	    	
	    	createMenuTree(cpLeft, cpRight);
	    	
	    	this.addChild(bc);
		}
	});
	
	
	function createMenuTree( cpLeft, cpRight){
		
		var req = requestFactory.createRequest({blockedScreen: cpLeft });
	        	
	    req.get("iwd/getMenuForCurUser").then(function(res){
	       		
	       		
	       		var menuStore = new Memory({
		        	data:res.data 
				    ,
		        	getChildren: function(object){
		        		
		            	return this.query({parentCode: object.code});
		        	},
		        	getIdentity: function(item){
		        		return item.code;
		        	}
		    	});
				
						
				
			    // Create the model
			    var menuModel = new ObjectStoreModel({
			        store: menuStore,
			        query: {parentCode: null},
			        getLabel: function(item){
			        	return item.description;
			        }
			    });
			
			    // Create the Tree.
			    var menuTree = new Tree({
			        model: menuModel,
			        onClick: function(item, node, evt) {
			        	
			        	if(item.module == null || item.module ==''){
			        		return;
			        	}
			        	
				        var closablePane = new ContentPane({
							        title:item.description,
							        closable: true,
							        onClose: function(){
							           // confirm() returns true or false, so return that.
							           return confirm("Do you really want to Close this?");
							        }
							    });
						
						cpRight.addChild(closablePane);	    	
				        
				        cpRight.selectChild(closablePane);
				        
				        var spin = spinFactory.createSpin({
											   	blockedScreen:closablePane
											   });
						
						spin.showSpin();					   
				        
				        require([item.module], function(NewScreen){
				        	spin.closeSpin();
					       	var srn = new NewScreen(); 
					       	closablePane.addChild(srn);
					        	
					       	
					    });
					        
			        	
			        }
			    });
	       		
	       		cpLeft.addChild(menuTree);
	       		
	       	}
	    );
	
		
	}
	
});