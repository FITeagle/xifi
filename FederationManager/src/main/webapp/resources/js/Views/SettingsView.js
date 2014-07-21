/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "models"], function (Backbone) {

    var SettingsView = Backbone.View.extend({

        _template: _.itemplate($('#settingsTemplate').html()),

        initialize: function () {
            this.model.set({
                username: this.options.loginModel.get('username'),
                email: this.options.loginModel.get('user').get('email')
            });
        },

        render: function () {
            $(this.el).empty().html(this._template(this.model));
            console.log("show settings");
        }
    });

    return SettingsView;
});