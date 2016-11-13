define([ "dojo/_base/declare", "dojo/_base/lang", "dijit/_Container"
		,"dijit/_WidgetBase", "dijit/_TemplatedMixin"
		,"dijit/_WidgetsInTemplateMixin", "dijit/layout/ContentPane"
		,"dojox/layout/TableContainer", "dijit/form/TextBox" 
		,"dijit/form/MultiSelect"
		,"dijit/Dialog","dijit/Tree"
		,"dojo/on"
		,"dojo/json"
		,'dojo/store/Memory'
		,'dijit/tree/ObjectStoreModel'
		,"app/remote/jsonRestFactory", "app/remote/requestFactory", "../../remote/GridDataStore"
		,'gridx/Grid'
		,'gridx/core/model/cache/Async', 'gridx/modules/SingleSort'
		,'gridx/modules/ColumnResizer', "gridx/modules/Pagination"
		,"gridx/modules/pagination/PaginationBar"
		,'gridx/modules/select/Row'
		,"dijit/layout/LayoutContainer", "dijit/layout/StackContainer"
		,"dijit/form/Form" 
		,"../../widget/winUtil"
		,"dojo/text!./UserMaintenanceTemplate.html" ],
		function(declare, lang, _Container, _WidgetBase, _TemplatedMixin
				,_WidgetsInTemplateMixin, ContentPane, TableContainer, TextBox, MultiSelect
				,Dialog, Tree
				,on, JSON, Memory, ObjectStoreModel
				,jsonRestFactory, requestFactory, GridDataStore, Grid, GridCache, SingleSort
				,ColumnResizer , Pagination , PaginationBar
				,SelectRow
				,LayoutContainer
				,stackContainer, Form, winUtil, template) {

			var _DUMMY_PASSWORD = "Qjlw$&*(!f@>";
	
			var _doSearch = function() {
				
				this._gridStore.gdQueryEnabled = true;
				
				var userName = this._criUserName.get("value");
				var userDesc = this._criUserDesc.get("value");
 
				var queryParam = {
					userName : userName,
					userDesc : userDesc
				};

				// alert(queryParam.userDesc);

				// curWidget._userGrid.model.clearCache();

				// curWidget._userGrid.store.query(queryParam);
				this._userGrid.select.row.clear();
				this._userGrid.model.query(queryParam);
				this._userGrid.body.refresh();

			};

			var _resetForm = function(curWidget) {
				curWidget._userForm.reset();
				
				
				curWidget._userName.set('readOnly', !(curWidget._createNew));

				curWidget._roleList.domNode.options.length = 0;

				curWidget._passwordUpdated = false;
			}
			
						
			var _doNew = function() {
				
				this._createNew = true;
				
				_resetForm(this);
				this._tabContainer.selectChild(this._detailPane);
				
			};
			
			
			
			var _doEdit = function() {
				
				var selectedRowsIds = this._userGrid.select.row.getSelected();
				
				if(selectedRowsIds.length == 0){
					winUtil.alert("Please select one record.");
					return;
				}
				
				if(selectedRowsIds.length > 1){
					winUtil.alert("Please do not select more than one record.");
					return;
				}
				
				this._createNew = false;
				
				_resetForm(this);
				
				var selectedUserName = selectedRowsIds[0];
				this._userName.setValue(selectedUserName);
				
				this._tabContainer.selectChild(this._detailPane);
				
				var t = this;
				
				var jsonStore = _createJsonRest(this);
				jsonStore.get(selectedUserName).then(function(res) {
					
					t._userForm.setValues(res);
					
					t._password.setValue(_DUMMY_PASSWORD);
					t._confirmedPassword.setValue(_DUMMY_PASSWORD);
					
					var roles = res.roles;
					
					//clear the options first.
					t._roleList.domNode.options.length = 0;
					
					if(roles != null && roles.length >0){
						for(var i = 0; i < roles.length; i++ ){
							_addToRoleList(t._roleList, roles[i].roleCode, roles[i].roleDescription);
						}
					}

				});
				
			};
			
			
			var _doDelete = function() {
				
				var selectedRowsIds = this._userGrid.select.row.getSelected();
				
				if(selectedRowsIds.length == 0){
					winUtil.alert("Please select records.");
					return;
				}
				
				var t = this;
				
				
				winUtil.confirm("Do you want to delete the selected records?").then(
					function(){
						var req = requestFactory.createRequest({blockedScreen: t }); 
						
						
						
						req.put("iwd/deleteUsers", {data : JSON.stringify(selectedRowsIds) }).then(function(res) {
							
							winUtil.alert("Delete successfully.");
							console.log(JSON.stringify(res));
							if(res == null || res.data == null || res.data.length == null){
								return;
							}
							
							var deleteUserNames = res.data;
							
							for(var i = 0; i< deleteUserNames.length; i++){
								
								console.log("Delete user from the grid. User name = " + deleteUserNames[0]);
								t._userGrid.store.remove(deleteUserNames[0]);
							}
						});
					}	
				
				);
				
				
			};
			
			
			var _createJsonRest = function(curWidget, reqTarget){
				
				if(reqTarget == null){
					reqTarget = "iwd/maintainUsers";
				}
				
				var jsonStore = jsonRestFactory.createJsonRest({
					blockedScreen : curWidget._userListPane
				}, {
					idProperty : "userName",
					target : reqTarget
				});
				
				return jsonStore;
			};
			
			
			
			var _doSave = function() {
				
				var formData = this._userForm.getValues();

				var roles = [];

				for(var i = 0 ; i < this._roleList.domNode.options.length; i++){
                    var role = {};
                    role.roleCode = this._roleList.domNode.options[i].value;
                    roles[i] = role;
				}

				formData.roles = roles;

				if(this._createNew || formData.password != _DUMMY_PASSWORD){
					if(formData.password != formData.confirmedPassword){
						
						winUtil.alert("The confirmed password is different from password.");
						return;
					}
				}
				
				if(!this._createNew && formData.password == _DUMMY_PASSWORD){
					delete formData.password;
					delete formData.confirmedPassword;
				}
				
				
				var jsonStore = _createJsonRest(this);
					
				var curWidget = this;
				
				if(this._createNew){
					
					jsonStore.add(formData).then(function(res) {
						winUtil.alert("User has been created successfully.");
							
						_resetForm(curWidget);
						
						curWidget._userGrid.store.add(res[0]);
						
						return res;
		
					});
					
				} else {
					
					jsonStore.put(formData).then(function(res) {
						winUtil.alert("User has been saved successfully.");
							
						curWidget._userGrid.store.put(res[0]);
						
						return res;
		
					});
					
					
				}
					
					
					
				

			};

			var _doBack = function() {
				this._tabContainer.selectChild(this._userListPane);

			};
			
			
			var _doAddRole = function(){
                var curWidget = this;
                if(curWidget._roleTree == null){
                    var req = requestFactory.createRequest({blockedScreen: curWidget });
                    req.get("iwd/maintainUsers/getAllRoles").then(function(res){
                        var roleStore = new Memory({
                                            data:res.data,
                                            getChildren: function(object){
                           		            	return this.query({parentRoleCode: object.roleCode});
                                            },
                                            getIdentity: function(item){
                                                return item.roleCode;
                                            }
                                        });

                        var roleTreeModel = new ObjectStoreModel({
                            store: roleStore,
                            query: {parentRoleCode: null},
                            getLabel: function(item){
                                return item.roleDescription;
                            }
                        });

                        curWidget._roleTree = new Tree({
                            model: roleTreeModel,
                            onClick: function(item, node, evt) {

                            }
                        });

       	       		    curWidget._roleTreePane.addChild(curWidget._roleTree);

                    });
                }

				this._roleSearchDialog.show();
				
			};
			
			var _doDeleteRole = function(){
				var selOpts = this._roleList.getSelected();
				
				if(selOpts == null || selOpts.length == 0){
					return;
				}
				
				for(var i = 0 ; i < selOpts.length; i++){
					this._roleList.domNode.removeChild(selOpts[i]);
				}
				
			};

            var _addToRoleList = function (roleList, roleCode, roleDesc){
                for(var i = 0; i < roleList.domNode.options.length; i++){
                    var opt = roleList.domNode.options[i];

                    if(opt.value == roleCode){
                        return;
                    }
                }

                var opt = new Option(roleDesc, roleCode);

                roleList.domNode.options[roleList.domNode.options.length] = opt;

            };

			var _doSelectRole = function(){
                var selectedRoles = this._roleTree.selectedItems;

                if(selectedRoles == null || selectedRoles.length == 0){
                    this._roleSearchDialog.hide();
                    return;
                }
                for(var i = 0 ; i < selectedRoles.length ; i++){
                    var role = selectedRoles[i];
                    _addToRoleList(this._roleList, role.roleCode, role.roleDescription);
                }

                this._roleSearchDialog.hide();
			};

			return declare([ _WidgetBase, _TemplatedMixin,
					_WidgetsInTemplateMixin ],
					{

						templateString : template,

						postCreate : function() {
							var curWidget = this;

							curWidget.own(on(this._searchButton, 
											  "click", 
											  lang.hitch(curWidget,	_doSearch)));

							curWidget.own(on(this._newButton, 
											 "click", 
											 lang.hitch(curWidget,_doNew)));
							
							curWidget.own(on(this._editButton, 
											 "click", 
											 lang.hitch(curWidget,	_doEdit)));

							curWidget.own(on(this._saveButton, 
											 "click", 
											 lang.hitch(curWidget, _doSave)));
							
							curWidget.own(on(this._deleteButton, 
										     "click", 
										     lang.hitch(curWidget, _doDelete)));

							curWidget.own(on(this._backButton, 
										     "click", 
										     lang.hitch(curWidget,	_doBack)));
							
							curWidget.own(on(this._deleteRoleButton, 
								     "click", 
								     lang.hitch(curWidget,	_doDeleteRole)));
							
							curWidget.own(on(this._addRoleButton, 
								     "click", 
								     lang.hitch(curWidget,	_doAddRole)));

							curWidget.own(on(this._closeRoleDialogButton,
                            		  "click",
                            		  lang.hitch(curWidget,	function(){curWidget._roleSearchDialog.hide();})));
							
							curWidget.own(on(this._selectRoleDialogButton,
                                                        		  "click",
                                                        		  lang.hitch(curWidget,	_doSelectRole)));
							/*
							 * var store = new Memory({ idProperty:"userName",
							 * data:
							 * [{"userName":"abc","userDescription":"abc","password":null,"locked":"N","lastModifiedTimestamp":"20131010160000"},
							 * {"userName":"ddd","userDescription":"ddd","password":null,"locked":"N","lastModifiedTimestamp":"20131010160000"},
							 * {"userName":"def","userDescription":"def","password":null,"locked":"N","lastModifiedTimestamp":"20131010160000"}]
							 * 
							 * });
							 */

							var jsonStore = _createJsonRest(curWidget);
							
							
							/*
							 * jsonStore.query("").then(function (items) {
							 * alert(items[0].userName); });
							 */

							var columns = [
									{
										field : 'userName',
										name : 'User Name',
										sortable : false
									},
									{
										field : 'userDescription',
										name : 'User Description'
									},
									{
										field : 'locked',
										name : 'Locked',
										decorator : function(cellData, rowId,
												rowIndex) {
											return cellData == 'Y' ? 'Yes'
													: 'No';
										}
									} ];
							
							var gridStore = new GridDataStore(jsonStore, {gdQueryEnabled:false});
							
							this._gridStore = gridStore;
							
							var grid = new Grid({
								cacheClass : GridCache,
								store : gridStore,
								structure : columns,
								modules : [ SingleSort, 
								            ColumnResizer, 
								            {
												moduleClass : Pagination
											},
								            
								            {
												moduleClass : PaginationBar,
												sizes : [ 25, 50, 100 ]
											}, 
											{
												moduleClass : SelectRow,
												triggerOnCell: true
											}
										  ],
								bodyRowHoverEffect: false
								}/* ,this._searchResult */);

							this._searchResult.addChild(grid);

							grid.startup();
							grid.pagination.setPageSize(50);
							
							//grid.setStore(jsonStore);
							
							this._userGrid = grid;

							// this._searchResult

						},
						
						destroyDescendants : function(){
							//must destory dialog manually since Dojo will not destroy it by default.
							this._roleSearchDialog.destroyRecursive();
							
							this.inherited(arguments);
							
							
						}
			
			

					});

		});