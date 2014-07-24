
/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "underscore"], function (Backbone,_) {

    var NavTabView = Backbone.View.extend({

        _template: _.itemplate($('#navTabTemplate').html()),

        initialize: function () {
            this.model.bind('change:actives', this.render, this);
        },

        render: function () {
            var self = this;
            $(self.el).empty().html(self._template({
                models: self.model.models


            }));

        }
    });

    return NavTabView;

});