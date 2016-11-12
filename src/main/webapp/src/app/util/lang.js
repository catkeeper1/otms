define([
	"dojo/_base/lang"
], function(lang){
	
	return {
		mixin: function(dest, src){
			
			if(dest == null){
				dest = new Object();
			}
			
			if(src == null){
				src = new Object();
			}
			
			return lang.mixin(dest, src);
		}
		
	};

});