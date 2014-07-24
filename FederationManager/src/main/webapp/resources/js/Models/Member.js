/**
 * Created by dne on 23.05.14.
 */
define(["backbone"], function(Backbone) {

    var Member = Backbone.Model.extend({
        defaults: {
            id: 0,
            tenant_id: 0
        }
    });

    return Member;

});

