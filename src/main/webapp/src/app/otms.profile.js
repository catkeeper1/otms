var profile = (function(){
	var testResourceRe = /^app\/tests\//,

		copyOnly = function(filename, mid){
			
			var list = {
				
				"app/otms.profile":1,
				"app/package.json":1 /*,
				//"app/security/user/UserMaintenance":1
				/*,
				"dojo/OpenAjax":1,
				"dojo/tests":1,
				// these are test modules that are not intended to ever be built
				"dojo/tests/_base/loader/requirejs/requirejs-setup":1,
				"dojo/tests/_base/loader/requirejs/dataMain":1,
				"dojo/tests/_base/loader/requirejs/depoverlap":1,
				"dojo/tests/_base/loader/requirejs/simple-tests":1,
				"dojo/tests/_base/loader/requirejs/relative/relative-tests":1,
				"dojo/tests/_base/loader/requirejs/exports/exports-tests":1
				*/
				
			};
			
			
			return (mid in list) ||
				///^dojo\/_base\/config\w+$/.test(mid) ||
				//(/^dojo\/resources\//.test(mid) && !/\.css$/.test(filename)) ||
				/(png|jpg|jpeg|gif|tiff)$/.test(filename);// ||
				// /built\-i18n\-test\/152\-build/.test(mid);
				
				 
		};

	return {
		resourceTags:{
			
			test: function(filename, mid){
				return testResourceRe.test(mid) || mid=="app/tests";
			},
			
			copyOnly: function(filename, mid){
				return copyOnly(filename, mid);
			},

			amd: function(filename, mid){
				
				if(mid == "app/security/user/UserMaintenance"){
					return true;
				}
				
				if(filename == "UserMaintenance.js"){
					return true;
				}
				
				return !testResourceRe.test(mid) && !copyOnly(filename, mid) && /\.js$/.test(filename);
			}
		}
	};
})();
