var LoginView = Backbone.View.extend({

	_template : _.itemplate($('#not_logged_in').html()),

	initialize : function() {
		console.log("Init Login view");
		this.model.bind('change:loggedIn', this.onLogin, this);
		this.model.bind('auth-error', this.renderonerror, this);
		this.model.bind('auth-needed', this.render, this);
		this.onLogin();
	},

	onCredentialsSubmit : function(e) {

		e.preventDefault();
		this.model.setCredentials(this.$('input[name=username]').val(), this.$('input[name=password]').val());
	},

	onCloseErrorMsg : function(e) {
		this.model.set({
			"error_msg" : null
		});
		this.renderonerror();
	},

	onLogin : function() {
		console.log("Checking");
		if (this.model.get('loggedIn')) {
			if (this.options.next_view !== undefined) {
				window.location.href = "#" + this.options.next_view;
			} else {
				window.location.href = "#home";
			}
		}
	},

	render : function() {
		console.log("Rendering login");
		self = this;
		self.$el = $(self.options.auth_el);
		$('#root').css('display', 'none');
		$(this.options.auth_el).empty().html(self._template(self.model));
		$(this.options.auth_el).fadeIn('slow');
		// });
		self.delegateEvents({
			'click #home_loginbtn' : 'onCredentialsSubmit',
			'click #registerbtn' : 'onRegisterSwitch',
			'click .close' : 'onCloseErrorMsg'

		});
		return this;
	},

	renderonerror : function() {
		$('#root').css('display', 'none');
		console.log("got the right one");
		$(this.options.auth_el).empty().html(this._template(this.model));
		$('body').attr("id", "splash");
		return this;
	},

	onRegisterSwitch : function(e) {
		e.preventDefault();
		console.log("Register switch");
		window.location.href = "#auth/register";

	}
});
var NavTabView = Backbone.View.extend({

	_template : _.itemplate($('#navTabTemplate').html()),

	initialize : function() {
		this.model.bind('change:actives', this.render, this);
	},

	render : function() {
		var self = this;
		$(self.el).empty().html(self._template({
			models : self.model.models,
			showAdmin : self.options.loginModel.get("user").isAdmin(),
			tenants : self.options.tenants,
			tenant : self.options.tenant
		}));
		return this;
	}
});
var RootView = Backbone.View.extend({

	_roottemplate : _.itemplate($('#rootTemplate').html()),

	initialize : function() {
		$(this.options.root_el).empty().html(this._roottemplate()).css('display', 'None');
		this.model.bind('change:loggedIn', this.onLogin, this);
		this.onLogin();
		UserDB.fetch();
		RegistrationDB.fetch();

	},

	onCloseErrorMsg : function(e) {
		this.model.set({
			"error_msg" : null
		});
		this.renderAuthonerror();
	},

	onLogin : function() {
		if (this.model.get('loggedIn')) {
			if (this.options.next_view !== undefined) {
				window.location.href = "#" + this.options.next_view;
			} else {
				window.location.href = "#home";
			}
		}
	},

	renderAuth : function() {
		var self = this;
		var authView = new LoginView({
			auth_el : this.options.auth_el,
			model : this.model
		});
		authView.render();

		return this;
	},

	renderRoot : function() {
		var self = this;
		self.$el = $(self.options.auth_el);
		self.delegateEvents({});
		if ($(self.options.auth_el).css('display') !== 'None')
			$(self.options.auth_el).fadeOut();
		$(self.options.root_el).fadeIn();
		$("#content").empty();
		return this;
	},

	renderRegister : function() {

		var self = this;
		var registerView = new RegisterView({
			register_el : this.options.register_el,
			model : this.model
		});
		registerView.render();

		return this;
	},
	addContent : function(view) {
		$("#content").empty();
		view.render();
		return this;
	}
});
var TopBarView = Backbone.View.extend({

	_template : _.itemplate($('#topBarTemplate').html()),
	_templateUser : _.itemplate($('#userTemplate').html()),

	initialize : function() {
		this.model.bind('change:title', this.renderTitle, this);
		this.model.bind('change:subtitle', this.renderTitle, this);
		this.options.loginModel.bind('change:username', this.render, this);
	},

	render : function() {
		var self = this;
		this.model.set({
			'username' : this.options.loginModel.get('username')
		});
		$(self.el).empty().html(self._template(self.model));
		$('#mainNav .navbar-inner').append(self._templateUser(self.model));
		return this;
	},

	renderTitle : function() {
		var html = '<h2 class="in-big-title" data-i18n="' + this.model.get('title') + '">' + this.model.get('title');
		if (this.model.has('subtitle')) {
			html += '<small>' + this.model.get('subtitle') + '</small>';
		}
		html += '</h2>';
		$('#page-title').html(html);
		return this;
	}
});
var RegisterView = Backbone.View.extend({
	_template : _.itemplate($('#not_registered').html()),
	initialize : function() {

	},
	events : {
		'click .register' : 'onRegisterSubmit'
	},

	render : function() {
		console.log("Rendering register");
		self = this;
		self.$el = $(self.options.register_el);
		$("#auth").css('display', 'none');
		$('#root').css('display', 'none');
		$(this.options.register_el).empty().html(self._template(self.model));
		$(this.options.register_el).fadeIn('slow');
		// });
		self.delegateEvents({
			'click #home_registerbtn' : 'onRegisterSubmit',
			'click #loginbtn' : 'onLoginSwitch',
			'click .close' : 'onCloseErrorMsg'
		});
		return this;

	},
	onLoginSwitch : function(e) {
		e.preventDefault();
		$("#register").css('display', 'none');
		console.log("switch to login");
		window.location.href = "#/auth/login";
	},
	onRegisterSubmit : function(e) {
		e.preventDefault();
		console.log("submitting infos");
		console.log(this.$('input[name=username]').val() + ' ' + this.$('input[name=password]').val() + ' ' + this.$('input[name=email]').val());

		var u = new User();
		var checked = $("#register_admin").attr("checked") === "checked";
		var newrole = 'io';
		if (checked) {
			newrole = 'admin';
		}
		u.set({
			"id" : (Math.abs(Math.random() * 9999)),
			"name" : this.$('input[name=username]').val(),
			"password" : this.$('input[name=password]').val(),
			"email" : this.$('input[name=email]').val(),
			"roles" : {
				'roles' : [{
					'role' : newrole
				}]
			}
		});

		console.log(u.toJSON());
		UserDB.create(u);
		alert("you can now login as:" + u.get("name"));

	}
});
var SettingsView = Backbone.View.extend({

	_template : _.itemplate($('#settingsTemplate').html()),

	//
	// events : {
	// 'click #select' : 'changeLang'
	// },
	//
	initialize : function() {
		this.model.set({
			username : this.options.loginModel.get('username'),
			email : this.options.loginModel.get('user').get('email')
		});
	},

	// changeLang : function(evt) {
	// $("select option:selected").each(function() {
	// var lang = $(this).attr('value');
	// UTILS.i18n.setlang(lang, function() {
	// window.location.href = "";
	// });
	//
	// });
	// },
	//
	render : function() {
		$(this.el).empty().html(this._template(this.model));
		console.log("show settings");
	}
});

var SideBarView = Backbone.View.extend({

	_template : _.itemplate($('#sideBarTemplate').html()),

	initialize : function() {
		this.model.bind('change:actives', this.render, this);
		this.options.loginModel.bind('change:tenant_id', this.render, this);
	},

	render : function(title, showTenants) {
		var self = this;
		var html = self._template({
			models : self.model.models,
			showTenants : showTenants,
			loginModel : this.options.loginModel,
			title : title
		});
		$(self.el).empty();
		$(self.el).html(html);
		// $("#tenant_switcher").selectbox({
		// onChange: function (val, inst) {
		// window.location = val;
		// }
		// });
	}
});
var QuickOnlineTestView = Backbone.View.extend({
	_template : _.itemplate($("#qotTemplate").html()),
	_legalComplianceTemplate : _.itemplate($("#legalComplianceTemplate").html()),
	_operationalComplianceTemplate : _.itemplate($("#operationalComplianceTemplate").html()),
	_technicalComplianceTemplate : _.itemplate($("#technicalComplianceTemplate").html()),
	_contactTemplate : _.itemplate($("#contactTemplate").html()),
	_qotEndTemplate : _.itemplate($("#qotEndTemplate").html()),
	loginModel : null,

	initialize : function() {
		this.loginModel = this.options.loginModel;
	},

	render : function() {
		$(this.el).empty();
		$(this.el).html(this._template);
		this.delegateEvents({
			'click #startSurvey' : 'startSurvey'

		});
	},

	startSurvey : function(e) {
		e.preventDefault();
		this.model.set('owner', this.loginModel.get('user'));
		this.model.set('ownerName', this.loginModel.get('user').get('name'));
		$(this.el).empty();
		$(this.el).html(this._legalComplianceTemplate);
		this.delegateEvents({
			'click #checkLegal' : 'toggleContinueButton1',
			'click #btnagree1' : 'operationalCompliance'

		});
	},

	toggleContinueButton1 : function(e) {

		if ($(":checkbox").attr('checked') == 'checked') {
			$(".btn-primary").removeAttr("disabled");
		} else {
			$(".btn-primary").attr("disabled", "disabled");
		}

	},

	operationalCompliance : function(e) {
		e.preventDefault();
		this.model.set('legalCompliance', true);

		$(this.el).empty();
		$(this.el).html(this._operationalComplianceTemplate);
		this.delegateEvents({
			'click #checkOper' : 'toggleContinueButton1',
			'click #btnagree2' : 'technicalCompliance'

		});
	},
	technicalCompliance : function(e) {
		e.preventDefault();
		this.model.set('operationalCompliance', true);
		$(this.el).empty();
		$(this.el).html(this._technicalComplianceTemplate);
		this.delegateEvents({
			'click #checkTechnical' : 'toggleContinueButton1',
			'click #btnagree3' : 'contact'

		});
	},
	contact : function(e) {
		e.preventDefault();
		this.model.set('technicalCompliance', true);
		$(this.el).empty();
		$(this.el).html(this._contactTemplate);
		this.delegateEvents({
			'click #checkTechnical' : 'toggleContinueButton1',
			'click #endSurvey' : 'endSurvey'

		});
	},

	endSurvey : function(e) {
		e.preventDefault();

		//read forms
		var organizationForm = $('#organizationForm');
		var organizationModel = new Contact();
		organizationModel = organizationModel.parseForm(organizationForm);
		this.model.set("organization", organizationModel);
		var nodeMaster = ($("#checkMaster").attr("checked") === "checked");
		var nodeSlave = ($("#checkSlave").attr("checked") === "checked");
		if (nodeMaster) {
			this.model.set("nodeType", "master");
		} else {
			this.model.set("nodeType", "slave");
		}

		var managementForm = $("#managementForm");
		var managementModel = new Contact();
		managementModel = managementModel.parseForm(managementForm);
		this.model.set("management", managementModel);

		var supportForm = $('#supportForm');
		var supportModel = new Contact();
		supportModel = supportModel.parseForm(supportForm);
		this.model.set("support", supportModel);

		$(this.el).empty();
		$(this.el).html(this._qotEndTemplate);
		//this.model.set("step", 2);
		RegistrationDB.create(this.model);
		this.delegateEvents({
			'click #restartSurvey' : 'survey1'

		});

	},
	survey1 : function(e) {
		e.preventDefault();
		console.log("step");

	}
});

var SetupInfrastructureView = Backbone.View.extend({
	_template : _.itemplate($("#setupInfTemplate").html()),
	render : function() {
		$(this.el).empty();
		$(this.el).html(this._template);
		this.delegateEvents({
			'click #dlToolboxLink' : 'downloadToolbox',
			'click #toolboxManual' : 'installGuide'

		});
	},

	downloadToolbox : function(e) {
		e.preventDefault();
		console.log(this.model)
		this.model.set("installationTestResult", "success");
		this.model.save();
	},
	installGuide : function(e) {
		e.preventDefault();
		console.log("show Install Guide");
	}
});

var TestInstallationView = Backbone.View.extend({
	_template : _.itemplate($("#testInstallationTemplate").html()),

	render : function() {
		$(this.el).empty();
		$(this.el).html(this._template);
	}
});
var QOTRegistrationsView = Backbone.View.extend({
	_template : _.itemplate($("#qotRegistrationsTemplate").html()),
	registrationsQOT : null,
	initialize : function() {
		this.registrationsQOT = this.options.models
	},
	render : function() {
		console.log("rendering qot view");

		RegistrationDB.localStorage = new Backbone.LocalStorage('registrations');
		RegistrationDB.fetch();
		var newTemplate = this._template({
			models : RegistrationDB.getRegistrations(1)
		});
		$(this.el).empty();
		$(this.el).html(newTemplate);
		this.delegateEvents({
			'click .qotDetails' : 'showQotDetails',
			'click .approve' : 'approveInf'

		});

	},

	approveInf : function(e) {
		e.preventDefault();
		var modelId = e.currentTarget.parentElement.parentElement.firstElementChild.textContent;
		modelId = new String(modelId);
		modelId = modelId.trim();
		var currentModel = RegistrationDB.findWhere({
			id : modelId
		});
		currentModel.set({
			step : 2
		});
		currentModel.save();
		// RegistrationDB.sync();

		this.render();

	},
	showQotDetails : function(e) {
		e.preventDefault();
		var modelId = e.currentTarget.parentElement.parentElement.firstElementChild.textContent;
		modelId = new String(modelId);

		modelId = modelId.trim();
		var currentModel = RegistrationDB.findWhere({
			id : modelId
		});

		var qotDetails = new QOTDetailsView({
			el : "#myModal",
			model : currentModel
		});
		qotDetails.render();

	}
});
var NewRegistrationsView = Backbone.View.extend({
	_template : _.itemplate($("#newRegistrationsTemplate").html()),
	registrationsQOT : null,
	registrationsINS : null,
	registrationsREM : null,

	initialize : function() {

	},
	render : function() {

		$(this.el).empty();
		$(this.el).html(this._template);
		var qotView = new QOTRegistrationsView({
			el : "#qotTab",
			models : RegistrationDB.getRegistrations(1)
		})

		qotView.render();

		var installationResultView = new InstallationTestResultView({
			el : "#InstallationTestResults",
			models : RegistrationDB.getRegistrations(2)
		})
		installationResultView.render();

		var remoteTestView = new RemoteTestView({
			el : "#RemoteTesting",
			models : RegistrationDB.getRegistrations(3)
		})
		remoteTestView.render();
	}
});

var QOTDetailsView = Backbone.View.extend({
	_template : _.itemplate($("#qotDetailsTemplate").html()),
	render : function() {
		var newTemplate = this._template({
			model : this.options.model
		})
		$(this.el).empty();
		$(this.el).html(newTemplate);
	}
});

var InstallationTestResultView = Backbone.View.extend({
	_template : _.itemplate($("#installationTestResultTemplate").html()),

	render : function() {
		console.log("installation results rerender");
		RegistrationDB.localStorage = new Backbone.LocalStorage('registrations');
		RegistrationDB.fetch();
		var newTemplate = this._template({
			models : RegistrationDB.getRegistrations(2)
		});
		$(this.el).empty();
		$(this.el).html(newTemplate);
		this.delegateEvents({
			'click .qotDetails' : 'showQotDetails',
			'click .approve' : 'approveInf'

		});

	},
	approveInf : function(e) {
		e.preventDefault();
		var modelId = e.currentTarget.parentElement.parentElement.firstElementChild.textContent;
		modelId = new String(modelId);
		modelId = modelId.trim();
		var currentModel = RegistrationDB.findWhere({
			id : modelId
		});
		currentModel.set({
			step : 3
		});
		currentModel.save();
		// RegistrationDB.sync();

		this.render();
	}
});

var RemoteTestView = Backbone.View.extend({
	_template : _.itemplate($("#remoteTestTemplate").html()),
	render : function() {
		RegistrationDB.localStorage = new Backbone.LocalStorage('registrations');
		RegistrationDB.fetch();
		var newTemplate = this._template({
			models : RegistrationDB.getRegistrations(3)
		});
		$(this.el).empty();
		$(this.el).html(newTemplate);
		this.delegateEvents({
			'click .remoteTest' : 'showRemoteTestDetails'

		});

	},
	showRemoteTestDetails : function(e) {
		e.preventDefault();
		var modelId = e.currentTarget.parentElement.parentElement.firstElementChild.textContent;
		modelId = new String(modelId);

		modelId = modelId.trim();
		var currentModel = RegistrationDB.findWhere({
			id : modelId
		});

		var remoteTestDetails = new RemoteTestsDetailsView({
			el : "#myModal",
			model : currentModel,
			parent : this
		});
		remoteTestDetails.render();
	}
});

var RemoteTestsDetailsView = Backbone.View.extend({
_template : _.itemplate($("#remoteTestDetailsTemplate").html()),
render : function() {
var newTemplate = this._template({
model : this.options.model
})
$(this.el).empty();
$(this.el).html(newTemplate);
var self = this;
$('#connectionBar .bar').animate({width: '100%'}, 3000, function () {

$('#dcrmBar .bar').animate({width: '100%'}, 3000, function () {
$('#authBar .bar').animate({width: '100%'}, 3000, function () {
$('#paasBar .bar').animate({width: '100%'}, 3000, function () {
$('#sdcBar .bar').animate({width: '100%'}, 3000, function () {
	self.trigger("nextStep");
})})})})});
},
nextStep : function(){
	this.model.set("remoteTestResult", "success");
	this.model.save();
	this.options.parent.render();
}	
});

