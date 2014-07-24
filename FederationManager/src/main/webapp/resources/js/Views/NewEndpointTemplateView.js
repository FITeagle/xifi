/**
 * Created by dne on 23.05.14.
 */

define(["backbone", "endpoint"], function (Backbone, Endpoint) {

    var NewEndpointTemplateView = Backbone.View.extend({
        _template: _.itemplate($("#newEndpointTemplate").html()),
        initialize: function (options) {
            this.options = options;
            region : this.options.region
        },
        render: function () {
            var newEndpointTemplate = this._template({ services: this.options.services});
            $(this.el).empty();
            $(this.el).html(newEndpointTemplate);
            this.delegateEvents({
                'click .submitEndpoints': 'submitEndpoints',
                'click .close': 'close'

            });
        },


        close: function (e) {

            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');

        },

        submitEndpoints: function (e) {


            var regionId = this.options.region.get('uuid');
            var serviceId = $('#serviceType').children(":selected").attr("id");
            var serviceName = $('#serviceType').children(":selected").val();
            console.log("selected service:" + serviceName);
            var publicEndpoint = new Endpoint();
            publicEndpoint.set('interface', 'public');
            publicEndpoint.set('service_id', serviceId);
            publicEndpoint.set('region', regionId);
            publicEndpoint.set('url', $('#publicEndpointURL').val());
            publicEndpoint.set('name', serviceName + " public");
            var internalEndpoint = new Endpoint();
            internalEndpoint.set('interface', 'internal');
            internalEndpoint.set('service_id', serviceId);
            internalEndpoint.set('region', regionId);
            internalEndpoint.set('url', $('#internalEndpointURL').val());
            internalEndpoint.set('name', serviceName + " internal");
            var adminEndpoint = new Endpoint();
            adminEndpoint.set('interface', 'admin');
            adminEndpoint.set('service_id', serviceId);
            adminEndpoint.set('region', regionId);
            adminEndpoint.set('url', $('#adminEndpointURL').val());
            adminEndpoint.set('name', serviceName + " admin");

            publicEndpoint.post();
            internalEndpoint.post();
            adminEndpoint.post();
            this.options.parent.render();
            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');


        }


    });
    return NewEndpointTemplateView;
});
