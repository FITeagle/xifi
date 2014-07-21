/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "region"], function(Backbone, Region) {

    var Regions = Backbone.Collection.extend({
        url: "/federationManager/api/v3/regions",
        model: Region,
        defaults: {
            regions: null
        }
    });
    return  Regions;

});