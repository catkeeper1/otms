define(["dojo/_base/lang", "../util/lang","dojo/store/Memory"],
function(lang, appLang, Memory /*=====, declare, Store =====*/){

/*
 * Sometimes we use Gridx with JsonRest. When we want to add or remove a record from the grid(because other transaction in server side already create or delete that entity in DB)
 * , the grid will triger the JsonRest to send a put or remove request to server which is not needed. This object is used to solve this issue.
 * For query(), this object just call the query function from JsonRest store. For add/put/get/remove function, just delegate them to a dummy store(a Memory object) so that it will
 * not send HTTP request to server.
 * 
 * For the options parameter, it is expected a object like below:
 * {
 * 		gdQueryEnabled; // true/false. If this is "ture" the query() should call the masterStore to query data from server. Otherwise, always return empty list.
 * }
 */

var GridDataStore = function(masterStore/*usually, this is the JsonRest */, options){
	
	var newObject = new Object();
	
	newObject = appLang.mixin(newObject, options);
	
	if(newObject.gdQueryEnabled == null){
		newObject.gdQueryEnabled = true;
	}
	
	newObject._gdDummyStore = new Memory([]);
	newObject._gdMasterStore = masterStore;
	
	newObject.query = function(query, directives){
		
		if(this.gdQueryEnabled){
			return this._gdMasterStore.query(query, directives);
		} else {
			return this._gdDummyStore.query(query, directives);
		}
		
	};
	
	//just implement a dummy get function. It will not return data.
	newObject.get = function(id, directives){
		return this._gdDummyStore.get(id, directives);
	};
	
	newObject.add = function(object, directives){
		return this._gdDummyStore.add(object, directives);
	};
	
	newObject.put = function(object, directives){
		return this._gdDummyStore.put(object, directives);
	};
	newObject.remove = function(id, directives){
		return this._gdDummyStore.remove(id, directives);
	};
	
	
	return lang.delegate(masterStore, newObject);
};


return GridDataStore;
});
