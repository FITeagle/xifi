/**
 * Created by dne on 23.05.14.
 */

define(["backbone"], function(Backbone) {

    var Contact = Backbone.Model.extend({
        defaults: {
            name: null,
            country: null,
            fax: null,
            phone: null,
            email: null,
            location: "",
            type: ""
        },
        parseForm: function (form) {

            this.set("name", form.find(".name").val());
            this.set("country", form.find(".countrypre").val());
            this.set("fax", form.find(".fax").val());
            this.set("phone", form.find(".phone").val());
            this.set("email", form.find(".emailadd").val());
            var c = "";
            form.find(".address").each(function () {
                c = c + " " + $(this).val();
            });
            this.set("location", c);

            return this;

        }

    });

    return Contact;
});