/**
 * Created by dne on 23.05.14.
 */
define(["backbone"], function(Backbone) {

    var RegionStatus = Backbone.Model.extend({
        defaults: {
            timestamp: 0,
            status: ""

        }

    });

    return RegionStatus;
});