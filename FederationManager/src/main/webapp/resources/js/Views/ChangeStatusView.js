/**
 * Created by dne on 23.05.14.
 */
define(["backbone"], function (Backbone) {


    var ChangeStatusView = Backbone.View.extend({
        _template: _.itemplate($("#changeStatusTemplate").html()),
        model: null,
        parent: null,
        region: null,
        initialize: function (options) {
            this.options = options;
            this.model = this.options.model;
            this.region = this.options.region;
            this.parent = this.options.parent;
        },
        render: function () {
            var changeStatusTemplate = this._template({
                model: this.model
            });
            $(this.el).empty();
            $(this.el).html(changeStatusTemplate);
            this.delegateEvents({
                'click .change': 'change',
                'click .close': 'close'
            });
        },

        close: function (e) {

            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');
            return true;
        },
        change: function (e) {
            var val = $("#newStatus").val();
            this.model.set("status", val);
            this.region.regionStatus = this.model;
            this.region.patchStatus();
            this.parent.render();
            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');
            return true;

        }
    });
    return ChangeStatusView;
 });