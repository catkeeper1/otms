define([
	"dojo/Deferred",
	"dojo/store/JsonRest",
	"./requestHandlerFactory",
	"dojo/aspect"
], function(Deferred, JsonRest, requestHandlerFactory, aspect){
	
	
	return new function(){
	
		this.createJsonRest = function(reqHandleOpts, jsonRestOpts){
			
			if(jsonRestOpts != null){
				if(jsonRestOpts.sortParam == null){
					jsonRestOpts.sortParam = "restSortBy";
				}
			}
		
			var result = new JsonRest(jsonRestOpts);
			
			var aspectFun = function(orgObj, orgMethod, orgArguments){
				var response = orgMethod.apply(orgObj, orgArguments);
	      			
	      		var handler = requestHandlerFactory.createRequestHandler(reqHandleOpts);
	      			
	      		return handler.attachToRequest(response);
			};
			
			aspect.around(result, "query", function(orgMethod){
    			return function(){
    				return aspectFun(this, orgMethod, arguments);
    			};
  			});
			
			aspect.around(result, "get", function(orgMethod){
    			return function(){
    				return aspectFun(this, orgMethod, arguments);
    			};
  			});
			
			aspect.around(result, "remove", function(orgMethod){
    			return function(){
    				return aspectFun(this, orgMethod, arguments);
    			};
  			});
			
			aspect.around(result, "put", function(orgMethod){
    			return function(){
    				return aspectFun(this, orgMethod, arguments);
    			};
  			});
			/* because add() call put() as well, so that do not need to around add(). Otherwise, it will be arounded 2 times.
			aspect.around(result, "add", function(orgMethod){
    			return function(){
    				return aspectFun(this, orgMethod, arguments);
    			};
  			});
			*/
			return result;
		
		}
		
	
	};
		
});