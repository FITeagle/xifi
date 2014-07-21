/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "models"], function (Backbone) {

    var SideBarView = Backbone.View.extend({

        _template: _.itemplate($('#sideBarTemplate').html()),

        initialize: function () {

        },

        render: function (title, showTenants) {
            var self = this;
            var html = self._template({
                models: self.model.models,

                loginModel: this.options.loginModel,
                title: title
            });
            $(self.el).empty();
            $(self.el).html(html);

        }
    });
    return SideBarView;
});