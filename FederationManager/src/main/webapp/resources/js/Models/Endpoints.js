/**
 * Created by dne on 23.05.14.
 */
define(["backbone","endpoint"], function(Backbone, Endpoint) {

    var Endpoints = Backbone.Collection.extend({
        url: "/federationManager/api/v3/endpoints",
        model: Endpoint,
        defaults: {
            endpoints: null
        }
    });
    return Endpoints;
});