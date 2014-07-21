define(["backbone"], function(Backbone) {

    var Organization = Backbone.Model.extend({
        defaults: {
            id: undefined,
            actorId: undefined,
            displayName: undefined,
            roles: undefined


        },
        initialize: function () {

        }

    });
    return Organization;
});