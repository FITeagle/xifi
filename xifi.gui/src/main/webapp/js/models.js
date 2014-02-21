// var Container = Backbone.Model.extend({
//
// _action : function(method, options) {
// var model = this;
// options = options || {};
// var error = options.error;
// options.success = function(resp) {
// model.trigger('sync', model, resp, options);
// if (options.callback !== undefined) {
// options.callback(resp);
// }
// };
// options.error = function(resp) {
// model.trigger('error', model, resp, options);
// if (error !== undefined) {
// error(model, resp);
// }
// };
// var xhr = (this.sync || Backbone.sync).call(this, method, this, options);
// return xhr;
// },
//
// copyObject : function(currentObject, targetContainer, targetObject, options) {
// console.log("Copy object");
// options = options || {};
// options.currentObject = currentObject;
// options.targetContainer = targetContainer;
// options.targetObject = targetObject;
// return this._action('copyObject', options);
// },
//
// uploadObject : function(objectName, object, options) {
// console.log("Upload object");
// options = options || {};
// options.objectName = objectName;
// options.object = object;
// return this._action('uploadObject', options);
// },
//
// downloadObject : function(objectName, options) {
// options = options || {};
// options.objectName = objectName;
// return this._action('downloadObject', options);
// },
//
// deleteObject : function(objectName, options) {
// console.log("Delete objects");
// options = options || {};
// options.objectName = objectName;
// return this._action('deleteObject', options);
// },
//
// sync : function(method, model, options) {
// switch (method) {
// case "read":
// mySucess = function(objects) {
// var cont = {};
// cont.objects = objects;
// return options.success(cont);
// };
// CDMI.Actions.getobjectlist(model.get('name'), mySucess);
// break;
// case "delete":
// CDMI.Actions.deletecontainer(model.get('name'), options.success, options.error);
// console.log(options.success, options.error);
// break;
// case "create":
// CDMI.Actions.createcontainer(model.get('name'), options.success, options.error);
// break;
// case "copyObject":
// CDMI.Actions.copyobject(model.get('name'), options.currentObject, options.targetContainer, options.targetObject, options.success, options.error);
// break;
// case "uploadObject":
// var reader = new FileReader();
// reader.onload = function(event) {
// var data = event.target.result.toString();
// var data_index = data.indexOf('base64') + 7;
// var data_index2 = data.indexOf('data:') + 5;
// var filedata = data.slice(data_index, data.length);
// var filetype = data.slice(data_index2, data_index - 8);
// console.log(data, filetype);
// CDMI.Actions.uploadobject(model.get('name'), options.objectName, filedata, filetype, options.success, options.error);
// };
// reader.readAsDataURL(options.object);
// break;
// case "downloadObject":
// console.log("Download object, ", options.success, options.error);
// CDMI.Actions.downloadobject(model.get('name'), options.objectName, options.success, options.error);
// break;
// case "deleteObject":
// CDMI.Actions.deleteobject(model.get('name'), options.objectName, options.success, options.error);
// break;
// }
// },
//
// parse : function(resp) {
// if (resp.container !== undefined) {
// resp.container.id = resp.container.name;
// return resp.container;
// } else {
// resp.id = resp.name;
// return resp;
// }
// }
// });
//
// var Containers = Backbone.Collection.extend({
// model : Container,
//
// sync : function(method, model, options) {
// if (method === "read") {
// CDMI.Actions.getcontainerlist(options.success, options.error);
// }
// },
//
// comparator : function(container) {
// return container.get("id");
// },
//
// parse : function(resp) {
// return resp;
// }
// });

var LoginStatus = Backbone.Model.extend({

	defaults : {
		loggedIn : false,
		username : null,
		password : null,
		access_token : '',
		error_msg : null,
		token : '',
		expired : true,
		user : null
	},

	initialize : function() {

		var self = this;

		this.bind('credentials', this.onCredentialsChange, this);
		// this.bind('change:token', this.onTokenChange, this);
		this.set({
			'token-ts' : localStorage.getItem('token-ts')
		});
		this.set({
			'token' : localStorage.getItem('token')
		});
		this.bind('error', this.onValidateError, this);

	},

	onValidateError : function(model, error) {
		model.set({
			error_msg : "Username and password are mandatory."
		});
		model.trigger('auth-error', error.msg);
	},

	onCredentialsChange : function(model, password) {

		var self = this;
		var name = self.get("username");
		if (self.get("username") !== '' && self.get("password") !== '') {
			this.findUser(self.get("username"), self.get("password"), undefined, undefined, function(user) {

				self.setToken(name);
				self.set({
					user: user
				});
				self.set({
					username : user.get("name")
				});

				self.set({
					'loggedIn' : true
				});

			}, function(msg) {
				self.set({
					'error_msg' : msg
				});
				self.trigger('auth-error', msg);
			});
		} else {
			var msg = "Username and password are mandatory.";
			self.set({
				'error_msg' : msg
			});
			self.trigger('auth-error', msg);
		}
	},
	findUser : function(username, password, tenant, token, callback, error) {
		//
		// var _authenticatedWithTenant = function(resp) {
		// console.log(resp);
		// console.log("Authenticated for tenant ", tenant);
		//
		// var host = "localhost:8080";
		// host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
		//
		var user = UserDB.findWhere({name: username});
		
		if(user !== undefined && user.get("password")===password){
			callback(user);
		}else{
			error("Username or password wrong");
		}
		
		
	},
	onTokenChange : function(context, token) {

		var self = context;
		console.log(token !== '');

		if (UTILS.Auth.isAuthenticated() && token !== '' && (new Date().getTime()) < self.get('token-ts') + 24 * 60 * 60 * 1000) {
			console.log("token changed to:" + token)
			// UTILS.Auth.authenticateWithCredentials(undefined, undefined, this.get('tenant-id'), token, function() {
			console.log("Authenticated with token: ", +24 * 60 * 60 * 1000 - (new Date().getTime()) - self.get('token-ts'));
			self.set({
				username : UTILS.Auth.getNameForToken(token)
			});
			console.log("Info: " + self.attributes);
			//

			self.set({
				'loggedIn' : true

			});

			// }, function(msg) {
			// console.log("Error authenticating with token");
			// self.set({
			// 'expired' : true
			// });
			// self.trigger('auth-needed', "");
			// self.set({
			// 'loggedIn' : false
			// });
			// self.trigger('auth-error', "");
			// });
		} else {
			console.log("On Token Change");
			console.log("Not logged In");
			self.set({
				'expired' : true
			});
			self.trigger('auth-needed', "");
			console.log("auth-needed triggered");
			self.set({
				'loggedIn' : false
			});
		}
	},

	onAccessTokenChange : function(context, access_token) {
		var self = context;
		console.log('veamos ', (new Date().getTime()), self.get('token-ts'), self.get('token-ex'));
		if (!UTILS.Auth.isAuthenticated() && access_token !== '' && (new Date().getTime()) < self.get('token-ts') + self.get('token-ex')) {
			console.log('autentico con ', this.get('tenant_id'), access_token);
			UTILS.Auth.authenticate(this.get('tenant_id'), access_token, function(tenant) {
				console.log("Authenticated with token: ", +self.get('token-ex') - (new Date().getTime()) - self.get('token-ts'));
				//console.log("New tenant: " + self.attributes.tenant.name);
				//self.set({'tenant': self.attributes.tenant});
				//console.log("New tenant: " + self.get("name"));
				self.set({
					username : UTILS.Auth.getName()
				});
				UTILS.Auth.getTenants(function(tenants) {
					self.set({
						tenant_id : tenant.id
					});
					self.set({
						tenants : tenants.tenants
					});
					self.set({
						'loggedIn' : true
					});
					localStorage.setItem('tenant_id', tenant.id);
					var subview = new MessagesView({
						state : "Info",
						title : "Connected to project " + tenant.name + " (ID " + tenant.id + ")"
					});
					subview.render();
				});
			}, function(msg) {
				console.log("Error authenticating with token");
				UTILS.Auth.logout();
			});
		} else {
			console.log("Not logged In");
			UTILS.Auth.logout();
		}
	},

	setToken : function(name) {
		console.log("setting token")
		var token = Math.floor(Math.random*999);
		if (localStorage.getItem('token') !== token) {
			localStorage.setItem('token-ts', new Date().getTime());
			// localStorage.setItem('tenant-id', UTILS.Auth.getCurrentTenant().id);
		}
		localStorage.setItem('token', token);
		this.set({
			'token' : token
		});
		//UTILS.Auth.setNameAndToken(name, token);
	},

	
	removeToken : function() {
		this.set({
			'access_token' : ''
		});
	},

	setCredentials : function(username, password) {
		console.log("Setting credentials");
		this.set({
			'username' : username,
			'password' : password,
			'error_msg' : undefined
		});
		this.trigger('credentials', this);
	},

	clearAll : function() {
		console.log("here remove token when set");
		this.set(this.defaults);
	}
});

var RegistrationStatus = Backbone.Model.extend({
	defaults : {
		step : 1,
		legalCompliance :false,
		operationalCompliance : false,
		technicalCompliance: false,
		owner : null,
		organization : null,
		nodeType : null,
		management : null,
		support : null,
		installationTestResult : 'pending',
		remoteTestResult : 'pending',
		ownerName : null
		
	}
});

var Registrations = Backbone.Collection.extend({
	model : RegistrationStatus,
	localStorage : new Backbone.LocalStorage('registrations'),
	
	getRegistrations: function(current){
		return this.where({step:current});
	}
});
var NavTabModel = Backbone.Model.extend({

	defaults : {
		name : undefined,
		active : false,
		url : undefined,
		css : undefined
	}

});

var NavTabModels = Backbone.Collection.extend({
	model : NavTabModel,

	setActive : function(name) {
		for (var index in this.models) {
			var tab = this.models[index];
			if (tab.get('name') === name) {
				tab.set({
					'active' : true
				});
			} else {
				tab.set({
					'active' : false
				});
			}
		}
		this.trigger("change:actives", "Changes");
	},

	getActive : function() {
		for (var index in this.models) {
			var tab = this.models[index];
			if (tab.get('active')) {
				return tab.get('name');
			}
		}
	}
});

var TopBarModel = Backbone.Model.extend({
});
var User = Backbone.Model.extend({
	defaults : {
		id : undefined,
		name : undefined,
		email : undefined,
		password : undefined,
		roles : undefined

	},
	initialize : function() {

	},
	isAdmin: function(){
		return false;
	}
	// sync : function(method, model, options) {
	// switch(method) {
	// case "read":
	// JSTACK.Keystone.getuser(model.get("id"), options.success, options.error);
	// break;
	// case "delete":
	// JSTACK.Keystone.deleteuser(model.get("id"), options.success, options.error);
	// break;
	// case "create":
	// JSTACK.Keystone.createuser(model.get("name"), model.get("password"), model.get("tenant_id"), model.get("email"), model.get("enabled"), options.success, options.error);
	// break;
	// case "get-roles":
	// JSTACK.Keystone.getuserroles(model.get("id"), options.tenant, options.success, options.error);
	// break;
	// case "add-role":
	// JSTACK.Keystone.adduserrole(model.get("id"), options.role, options.tenant, options.success, options.error);
	// break;
	// case "remove-role":
	// JSTACK.Keystone.removeuserrole(model.get("id"), options.role, options.tenant, options.success, options.error);
	// break;
	// case "update":
	// JSTACK.Keystone.edituser(model.get("id"), model.get("name"), model.get("password"), model.get("tenant_id"), model.get("email"), model.get("enabled"), options.success, options.error);
	// break;
	// }
	// },
	//
	// addRole : function(role, tenant, options) {
	// options = options || {
	// success : function() {
	// }
	// };
	// options.role = role;
	// options.tenant = tenant;
	// this.sync("add-role", this, options);
	// },
	//
	// getRoles : function(tenant, options) {
	// options = options || {
	// success : function() {
	// }
	// };
	// options.tenant = tenant;
	// this.sync("get-roles", this, options);
	// },
	//
	// removeRole : function(role, tenant, options) {
	// options = options || {
	// success : function() {
	// }
	// };
	// options.role = role;
	// options.tenant = tenant;
	// this.sync("remove-role", this, options);
	// }
});
var SettingsModel = Backbone.Model.extend({
	
});
var Users = Backbone.Collection.extend({
	model : User,
	localStorage : new Backbone.LocalStorage('users')
	// url: "/users",
	// _tenant_id : undefined,
	//
	// sync : function(method, model, options) {
	// Backbone.ajaxSync(method, model, options);
	// }
	//
	// parse : function(resp) {
	// return resp.users;
	// },
	//
	// tenant : function(tenant_id) {
	// this._tenant_id = tenant_id;
	// }
});
var Contact = Backbone.Model.extend({
	defaults: {
		name : null,
		country : null,
		fax : null,
		phone : null,
		email : null,
		location : ""
	}, 
	parseForm : function(form){
		
		this.set("name",form.find(".name").val());
		this.set("country", form.find(".countrypre").val());
		this.set("fax",form.find(".fax").val());
		this.set("phone", form.find(".phone").val());
		this.set("email",form.find(".emailadd").val());
		var c = "";
		form.find(".address").each(function(){
			c = c+ " " + $(this).val();
		});
		this.set("location",c);
		
		return this;
		
	}
	
});
var UserDB = new Users();
var RegistrationDB = new Registrations();

