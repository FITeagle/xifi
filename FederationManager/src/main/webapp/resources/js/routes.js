define(["backbone", "navTabModels", "loginStatus", "rootView", "allRegionsView", "myRegionsView", "navTabView", "toolsView"], function(Backbone, NavTabModels,LoginStatus, RootView, AllRegionsView, MyRegionsView, NavTabView, ToolsView) {


    var OSRouter = Backbone.Router.extend({

        rootView : undefined,

        tabs : new NavTabModels([
        {
            name: 'MyRegions',
            active: false,
            url : "#regions"
           },
            {
                name: 'Tools',
                active: false,
                url :"#tools"
            }
        ]),
        adminTabs : new NavTabModels({
            name: 'Overview',
            active : true,
            url: '#admin'
        }),

        navs : new NavTabModels([]),
        next : undefined,
        registrationModel : null,
        loginModel : undefined,
        currentView : undefined,

        timers : {},
        backgroundTime : 180,
        foregroundTime : 5,

        routes : {

            'settings/' : 'settings',
            'tools' : 'tools',
            '' : 'init',
            'regions': 'home'

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
            //	this.route('', 'init', this.init);
            //     this.route('#', 'init', this.wrap(this.init, this.checkAuthAndTimers));

//		this.route('home/', 'home', this.init);

        },



        newContentView : function(self, view) {

            if (self.currentView !== undefined) {
                self.currentView.close();
            }

            self.currentView = view;

        },

        init : function(self) {

            window.location.href = "#regions";
        },

        tools : function(){
            this.tabs.setActive("Tools")
            var view = new ToolsView({
                el: "#content"
            });
            this.rootView.addContent(view);
        },
        home : function() {


            var u = this.loginModel.get('user');


            var os = u.get("organizations");

            var roles = os.at('0').get("roles");

            $("#oil-usr").html(os.at('0').get("displayName"));
            var fm= false;
            if ( roles.length > 0 ) {
                for(var x = 0; x < roles.length; x++){
                    if(roles[x].name == "Provider"){
                        fm = true;
                    }
                }

            }
            if(fm == true) {
                this.showAdminView(this);
            }
            else
            {
                this.showRoot(this);
            }


        },

        settings : function() {
            console.log("settings called");
            var view = new SettingsView({
                el : "#content",
                model : new SettingsModel(),
                loginModel : this.loginModel
            });

            this.rootView.addContent(view);
        },
        showAdminView : function(self, option) {
            self.rootView.renderRoot();

            var view = new AllRegionsView({
                el : "#content",
                loginModel : this.loginModel
            });

            this.rootView.addContent(view);
        },

        showRoot : function(self, option) {

            this.tabs.setActive('MyRegions');
            self.rootView.renderRoot();
            var view = new MyRegionsView({
                el: "#content",
                loginModel: this.loginModel
            });

            var navTabView = new NavTabView({
                el: "#sidebar-wrapper",
                model: this.tabs
            });
            navTabView.render();
            this.rootView.addContent(view);
        },



        startQOT : function() {
            console.log("startQOT");
            var view = new QuickOnlineTestView({
                el : "#content",
                model : this.registrationModel,
                loginModel : this.loginModel
            });


            this.rootView.addContent(view);
        },
        setupInf : function() {
            console.log("setupInf");
            var view = new SetupInfrastructureView({
                el : "#content",
                model : this.registrationModel,
                loginModel : this.loginModel
            });

            this.rootView.addContent(view);

        }
    });
    return OSRouter;
});

