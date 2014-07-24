/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "organizations"], function(Backbone,Organizations) {

    var User = Backbone.Model.extend({
        defaults: {
            id: undefined,
            nickName: undefined,
            actorId: undefined,
            displayName: undefined,
            email: undefined,
            roles: undefined,
            organizations: undefined

        },
        initialize: function (data) {

            this.set("id",data.id);
            this.set("nickName",data.nickName);
            this.set("actorId", data.actorId);
            this.set("displayName",data.displayName);
            this.set("email",data.email);
            this.set("organizations" ,new Organizations(data.organizations));

        },
        isAdmin: function () {
            return false;
        }

    });
    return User;
});