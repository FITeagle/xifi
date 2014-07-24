/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "underscore"], function (Backbone, _) {

    var RootView = Backbone.View.extend({

        _roottemplate: _.itemplate($('#rootTemplate').html()),

        initialize: function (options) {
            this.options = options;
            $(this.options.root_el).empty().html(this._roottemplate()).css('display', 'None');
            this.model.bind('change:loggedIn', this.onLogin, this);
            this.onLogin();


        },

        onCloseErrorMsg: function (e) {
            this.model.set({
                "error_msg": null
            });
            this.renderAuthonerror();
        },

        onLogin: function () {
      /*      if (this.model.get('loggedIn')) {
                if (this.options.next_view !== undefined) {
                    window.location.href = "#" + this.options.next_view;
                } else {
                    window.location.href = "#home";
                }
            }*/
        },


        renderRoot: function () {
            var self = this;
            self.$el = $(self.options.auth_el);
            self.delegateEvents({});
            if ($(self.options.auth_el).css('display') !== 'None')
                $(self.options.auth_el).fadeOut();
            $(self.options.root_el).fadeIn();
            $("#content").empty();

            return this;
        },


        addContent: function (view) {
            $("#content").empty();
            view.render();
            return this;
        }
    });
    return RootView;
});