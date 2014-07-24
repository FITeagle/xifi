/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "libs", "user"], function(Backbone, UTILS, User) {
    var LoginStatus = Backbone.Model.extend({

        defaults: {
            loggedIn: false,
            username: null,
            password: null,
            access_token: '',
            error_msg: null,
            token: '',
            expired: true,
            user: null
        },

        initialize: function () {

            var self = this;

            this.getCookie('token');
            this.getUser();
            this.set({
                username: this.get('user').get('displayName')
            });
            this.bind('error', this.onValidateError, this);

        },

        getUser: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            console.log(host);
            var _self = this;
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/auth?token=' + this.get('token'),
                async: false, // synchonous call in case code tries to use template before it's loaded
                success: function (data) {
                    _self.set({
                        user: new User(data)
                    });
                }
            });

        },
        onValidateError: function (model, error) {
            model.set({
                error_msg: "Username and password are mandatory."
            });
            model.trigger('auth-error', error.msg);
        },

        onCredentialsChange: function (model, password) {

            var self = this;
            var name = self.get("username");
            if (self.get("username") !== '' && self.get("password") !== '') {
                this.findUser(self.get("username"), self.get("password"), undefined, undefined, function (user) {

                    self.setToken(name);
                    self.set({
                        user: user
                    });
                    self.set({
                        username: user.get("name")
                    });

                    self.set({
                        'loggedIn': true
                    });

                }, function (msg) {
                    self.set({
                        'error_msg': msg
                    });
                    self.trigger('auth-error', msg);
                });
            } else {
                var msg = "Username and password are mandatory.";
                self.set({
                    'error_msg': msg
                });
                self.trigger('auth-error', msg);
            }
        },
        findUser: function (username, password, tenant, token, callback, error) {

            var user = UserDB.findWhere({name: username});

            if (user !== undefined && user.get("password") === password) {
                callback(user);
            } else {
                error("Username or password wrong");
            }


        },
        onTokenChange: function (context, token) {

            var self = context;
            console.log(token !== '');

            if (UTILS.Auth.isAuthenticated() && token !== '' && (new Date().getTime()) < self.get('token-ts') + 24 * 60 * 60 * 1000) {
                console.log("token changed to:" + token)
                // UTILS.Auth.authenticateWithCredentials(undefined, undefined, this.get('tenant-id'), token, function() {
                console.log("Authenticated with token: ", +24 * 60 * 60 * 1000 - (new Date().getTime()) - self.get('token-ts'));
                self.set({
                    username: UTILS.Auth.getNameForToken(token)
                });
                console.log("Info: " + self.attributes);
                //

                self.set({
                    'loggedIn': true

                });


            } else {
                console.log("On Token Change");
                console.log("Not logged In");
                self.set({
                    'expired': true
                });
                self.trigger('auth-needed', "");
                console.log("auth-needed triggered");
                self.set({
                    'loggedIn': false
                });
            }
        },

        onAccessTokenChange: function (context, access_token) {
            var self = context;
            console.log('veamos ', (new Date().getTime()), self.get('token-ts'), self.get('token-ex'));
            if (!UTILS.Auth.isAuthenticated() && access_token !== '' && (new Date().getTime()) < self.get('token-ts') + self.get('token-ex')) {
                console.log('autentico con ', this.get('tenant_id'), access_token);
                UTILS.Auth.authenticate(this.get('tenant_id'), access_token, function (tenant) {
                    console.log("Authenticated with token: ", +self.get('token-ex') - (new Date().getTime()) - self.get('token-ts'));
                    //console.log("New tenant: " + self.attributes.tenant.name);
                    //self.set({'tenant': self.attributes.tenant});
                    //console.log("New tenant: " + self.get("name"));
                    self.set({
                        username: UTILS.Auth.getName()
                    });
                    UTILS.Auth.getTenants(function (tenants) {
                        self.set({
                            tenant_id: tenant.id
                        });
                        self.set({
                            tenants: tenants.tenants
                        });
                        self.set({
                            'loggedIn': true
                        });
                        localStorage.setItem('tenant_id', tenant.id);
                        var subview = new MessagesView({
                            state: "Info",
                            title: "Connected to project " + tenant.name + " (ID " + tenant.id + ")"
                        });
                        subview.render();
                    });
                }, function (msg) {
                    console.log("Error authenticating with token");
                    UTILS.Auth.logout();
                });
            } else {
                console.log("Not logged In");
                UTILS.Auth.logout();
            }
        },

        setToken: function (name) {
            console.log("setting token")
            var token = Math.floor(Math.random * 999);
            if (localStorage.getItem('token') !== token) {
                localStorage.setItem('token-ts', new Date().getTime());
                // localStorage.setItem('tenant-id', UTILS.Auth.getCurrentTenant().id);
            }
            localStorage.setItem('token', token);
            this.set({
                'token': token
            });
            //UTILS.Auth.setNameAndToken(name, token);
        },


        removeToken: function () {
            this.set({
                'access_token': ''
            });
        },

        setCredentials: function (username, password) {
            console.log("Setting credentials");
            this.set({
                'username': username,
                'password': password,
                'error_msg': undefined
            });
            this.trigger('credentials', this);
        },

        clearAll: function () {
            this.deleteCookie('token');
            this.removeUser(this.get('user'));
            this.set(this.defaults);

        },

        getCookie: function (cname) {
            var name = cname + "=";
            var ca = document.cookie.split(';');
            for (var i = 0; i < ca.length; i++) {
                var c = ca[i].trim();

                if (c.indexOf(name) == 0) {
                    var value = c.split('=')[1];

                    this.set({'token': value});

                }
            }
        },

        deleteCookie: function (cname) {
            document.cookie = cname + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
        },

        removeUser: function (u) {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/auth?token=' + this.get('token'),
                async: false, // synchonous call in case code tries to use template before it's loaded
                type: 'DELETE',
                success: function (data) {

                }
            });
        }
    });

    return LoginStatus;
});