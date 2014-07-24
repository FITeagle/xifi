/**
 * Created by dne on 03.07.14.
 */
define(["backbone","underscore"], function (Backbone,_) {


    var ToolsView = Backbone.View.extend({

        _template: _.itemplate($('#toolsViewTemplate').html()),

        initialize: function () {

        },

        render: function () {
            var self = this;
            var html = self._template({

            });
            $(this.el).empty();
            $(this.el).html(html);

        }
    });
    return ToolsView;

});