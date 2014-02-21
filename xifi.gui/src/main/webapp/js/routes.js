var OSRouter = Backbone.Router.extend({

	rootView : undefined,

	tabs : new NavTabModels([{
		name : 'Join the Federation',
		active : true,
		url : '#federation'
	}]),
	adminTabs : new NavTabModels({
		name: 'Overview',
		active : true,
		url: '#admin'
	}),
	top : new TopBarModel({
		title : 'Overview:',
		subtitle : ''
	}),
	navs : new NavTabModels([]),
	next : undefined,

	loginModel : undefined,
	registrationModel : undefined,
	currentView : undefined,

	timers : {},
	backgroundTime : 180,
	foregroundTime : 5,

	routes : {
		'auth/login' : 'login',
		'auth/switch/:id/' : 'switchTenant',
		'auth/logout' : 'logout',
		'home' : 'home',
		'auth/register' : 'register',
		'federation' : 'home',
		'settings/' : 'settings',
		'qot' : 'startQOT',
		'setupInfrastructure' : 'setupInf',
		'testInstallation' : 'testInstallation',
		'newRequests' : 'newRequests'
	},

	initialize : function() {

		this.loginModel = new LoginStatus();
		
		Backbone.wrapError = function(onError, originalModel, options) {
			return function(model, resp) {
				resp = model === originalModel ? resp : model;
				if (onError) {
					onError(originalModel, resp, options);
				} else {
					originalModel.trigger('error', originalModel, resp, options);
					var subview = new MessagesView({
						state : "Error",
						title : "Error. Cause: " + resp.message,
						info : resp.body
					});
					subview.render();
				}
			};
		};

		Backbone.View.prototype.close = function() {
			//this.remove();
			this.unbind();
			if (this.onClose) {
				this.onClose();
			}
		};

		this.rootView = new RootView({
			model : this.loginModel,
			auth_el : '#auth',
			root_el : '#root',
			register_el : '#register'
		});
		this.route('', 'init', this.wrap(this.init, this.checkAuthAndTimers));
		//     this.route('#', 'init', this.wrap(this.init, this.checkAuthAndTimers));

		this.route('home/', 'home', this.wrap(this.init, this.checkAuthAndTimers));

	},

	wrap : function(func, wrapper, modelArray) {
		var ArrayProto = Array.prototype;
		var slice = ArrayProto.slice;
		return function() {
			var args = [func].concat(slice.call(arguments, 0));
			return wrapper.apply(this, [args, modelArray]);
		};
	},

	initFetch : function() {
		if (Object.keys(this.timers).length === 0) {

			var seconds = this.backgroundTime;

			if (this.loginModel.isAdmin()) {
				console.log("admin");

			}
		}
	},

	checkAuthAndTimers : function() {
		console.log("checkAuthandTimers");
		var next = arguments[0][0];
		this.rootView.options.next_view = Backbone.history.fragment;
		if (!this.loginModel.get("loggedIn")) {
			window.location.href = "#auth/login";
			return;
		} else {
			this.initFetch();
			this.update_fetch(arguments[1], this.foregroundTime, this.backgroundTime);
		}
		var args = [this].concat(Array.prototype.slice.call(arguments[0], 1));
		if (next) {
			next.apply(this, args);
		}
	},

	newContentView : function(self, view) {

		if (self.currentView !== undefined) {
			self.currentView.close();
		}

		self.currentView = view;

	},

	init : function(self) {
		window.location.href = "#home";

	},

	login : function() {
		console.log("Rendering auth");
		this.rootView.renderAuth();
	},

	logout : function() {
		console.log("logout")
		this.loginModel.clearAll();
		window.location.href = "#auth/login";
	},

	home : function() {
		if (!this.loginModel.get("loggedIn")) {
			window.location.href = "#auth/login";
			return;
		} else {

			var roles = this.loginModel.get('user').get('roles');
			if (roles.roles[0].role === "admin") {
				this.showAdminView(this)
			} else {
				this.showRoot(this);
			}

		}
	},

	settings : function() {
		console.log("settings called");
		var view = new SettingsView({
			el : "#content",
			model : new SettingsModel(),
			loginModel : this.loginModel
		});
		this.top.set({
			title : "Settings"
		});
		this.topBarView.renderTitle();
		this.rootView.addContent(view);
	},
	showAdminView : function(self, option) {
		self.rootView.renderRoot();
		if (this.navTabView === undefined) {
			this.navTabView = new NavTabView({
				el : '#navtab',
				model : self.adminTabs,
				loginModel : self.loginModel
			});
		}
		this.navTabView.render();

		if (this.topBarView === undefined) {
			this.topBarView = new TopBarView({
				el : '#topbar',
				model : self.top,
				loginModel : self.loginModel
			});
			this.topBarView.render();
		}
		this.top.set({
			title : "Admin View"
		});

		this.topBarView.renderTitle();
		
		var showTenants = (self.adminTabs.getActive() == 'Overview');
		this.sideBarView = undefined;
		self.navs = new NavTabModels([]);
		if (this.sideBarView === undefined) {
			this.sideBarView = new SideBarView({
				el : '#sidebar',
				model : self.navs,
				loginModel : self.loginModel
			});
			this.sideBarView.el = '#sidebar';
			self.navs.add(new NavTabModel({
				name : "New Requests",
				active : true,
				url : "#newRequests",
				css : undefined
			}));
		}
		
		this.sideBarView.model = self.navs;
		this.sideBarView.render(option, showTenants);
	},
	newRequests : function(){
		console.log("new Requests will appear here");
	
		var view = new NewRegistrationsView({
			el : "#content",
			model : this.registrationModel,
			loginModel : this.loginModel
		});
		this.top.set({
			title : "New Registrations"
		});
		this.topBarView.renderTitle();
		this.rootView.addContent(view);

	},
	showRoot : function(self, option) {
		console.log(self.loginModel.get("user"));
		RegistrationDB.localStorage = new Backbone.LocalStorage('registrations');
		RegistrationDB.fetch();
		this.registrationModel = RegistrationDB.findWhere({ownerName: self.loginModel.get("user").get('name')});
		if(this.registrationModel === undefined){
			console.log("new registrationModel");
			this.registrationModel = new RegistrationStatus();
		}
		console.log(this.registrationModel);
		self.rootView.renderRoot();
		if (this.navTabView === undefined) {
			this.navTabView = new NavTabView({
				el : '#navtab',
				model : self.tabs,
				loginModel : self.loginModel
			});
		}
		this.navTabView.render();

		if (this.topBarView === undefined) {
			this.topBarView = new TopBarView({
				el : '#topbar',
				model : self.top,
				loginModel : self.loginModel
			});
			this.topBarView.render();
		}
		this.top.set({
			title : "Federation"
		});

		this.topBarView.renderTitle();

		var showTenants = (self.tabs.getActive() == 'Join the Federation');
		this.sideBarView = undefined;
		self.navs = new NavTabModels([]);
		if (this.sideBarView === undefined) {
			this.sideBarView = new SideBarView({
				el : '#sidebar',
				model : self.navs,
				loginModel : self.loginModel
			});
			this.sideBarView.el = '#sidebar';
			self.navs.add(new NavTabModel({
				name : "Compliance Survey",
				active : true,
				url : "#qot",
				css : undefined
			}));
		}
		console.log(this.registrationModel.get("step"));
		if (this.registrationModel.get("step") > 1) {
			self.navs.add(new NavTabModel({
				name : "Set Up Infrastructure",
				active : true,
				url : "#setupInfrastructure",
				css : undefined
			}));
		}
		if (this.registrationModel.get("step") > 2) {
			self.navs.add(new NavTabModel({
				name : "Test your Installation",
				active : true,
				url : "#testInstallation",
				css : undefined
			}));
		}
		this.sideBarView.model = self.navs;
		this.sideBarView.render(option, showTenants);
	},

	sys_overview : function(self) {
		if (self.showSysRoot(self, 'Overview')) {
			var overview = new Overview();
			var view = new SysOverviewView({
				model : overview,
				el : '#content'
			});
			self.newContentView(self, view);
			view.render();
		}
	},

	sys_services : function(self) {
		if (self.showSysRoot(self, 'Services')) {
			var services = new Services();
			var view = new ServiceView({
				model : services,
				el : '#content'
			});
			self.newContentView(self, view);
			view.render();
		}
	},

	update_fetch : function(modelArray, currentSeconds, backgroundSeconds) {

		var self = this;

		modelArray = modelArray || [];

		if (this.timers.current !== undefined) {
			this.timers.current.forEach(function(oldModel) {
				clearInterval(self.timers[oldModel]);
				self.add_fetch(oldModel, backgroundSeconds);
			});
		}

		modelArray.forEach(function(modelName) {
			clearInterval(self.timers[modelName]);
			self.add_fetch(modelName, currentSeconds);
		});

		this.timers.current = modelArray;

	},

	clear_fetch : function() {
		var self = this;
		for (var index in this.timers) {
			var timer_id = this.timers[index];
			clearInterval(timer_id);
		}
		this.timers = {};
	},

	add_fetch : function(modelName, seconds) {
		var self = this;
		this[modelName].fetch();
		var id = setInterval(function() {
			self[modelName].fetch();
		}, seconds * 1000);

		this.timers[modelName] = id;
	},

	register : function() {
		this.rootView.renderRegister();

	},
	startQOT : function() {
		console.log("startQOT");
		var view = new QuickOnlineTestView({
			el : "#content",
			model : this.registrationModel,
			loginModel : this.loginModel
		});
		this.top.set({
			title : "Quick Compliance Survey"
		});
		this.topBarView.renderTitle();
		this.rootView.addContent(view);
	},
	setupInf : function() {
		console.log("setupInf");
		var view = new SetupInfrastructureView({
			el : "#content",
			model : this.registrationModel,
			loginModel : this.loginModel
		});
		this.top.set({
			title : "Setup your Infrastructure"
		});
		this.topBarView.renderTitle();
		this.rootView.addContent(view);

	},
	testInstallation : function() {
		console.log("setupInf");
		var view = new SetupInfrastructureView({
			el : "#content",
			model : this.registrationModel,
			loginModel : this.loginModel
		});
		this.top.set({
			title : "Setup your Infrastructure"
		});
		this.topBarView.renderTitle();
		this.rootView.addContent(view);

	}
});
