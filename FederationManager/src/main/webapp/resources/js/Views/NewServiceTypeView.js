/**
 * Created by dne on 23.05.14.
 */

define(["backbone", "service"], function (Backbone, Service) {

var NewServiceTypeView = Backbone.View.extend({
    _template: _.itemplate($("#addServiceTypeTemplate").html()),
    initialize: function (options) {
        this.options = options;
        this.service = this.options.service;
    },
    render: function () {
        var newServiceType = this._template({
            service: this.service
        });
        $(this.el).empty();
        $(this.el).html(newServiceType);
        this.delegateEvents({
            'click .submitServiceType': 'submitServiceType',
            'click .close': 'close',
            'click .patchService': 'patchService'

        });
    },

    patchService: function (e) {
        this.service.set('type', $("#newServiceType").val());
        this.service.patch();
        this.options.parent.render();
        this.$el.empty();
        this.undelegateEvents();
        $('#myModal').modal('hide');


    },
    close: function (e) {

        this.$el.empty();
        this.undelegateEvents();
        $('#myModal').modal('hide');

    },
    submitServiceType: function (e) {
        var newservice = new Service();
        newservice.set('type', $("#newServiceType").val());
        newservice.post();
        this.options.parent.services.add(newservice);
        this.options.parent.render();
        this.$el.empty();
        this.undelegateEvents();
        $('#myModal').modal('hide');


    }
});
    return NewServiceTypeView;
});