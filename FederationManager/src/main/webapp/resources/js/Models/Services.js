/**
 * Created by dne on 23.05.14.
 */

define(["backbone", "service"], function(Backbone, Service) {

    var Services = Backbone.Collection.extend({
        url: "/federationManager/api/v3/services",
        model: Service,
        defaults: {
            services: null
        }
    });

    return Services;
});