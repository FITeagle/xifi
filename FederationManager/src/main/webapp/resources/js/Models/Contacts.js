/**
 * Created by dne on 23.05.14.
 */

define(["backbone", "contact"], function(Backbone, Contact) {

    var Contacts = Backbone.Collection.extend({

        model: Contact
    });
    return Contacts;
});