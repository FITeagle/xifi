/**
 * Created by dne on 23.05.14.
 */

define(["backbone"], function(Backbone) {

    var NavTabModel = Backbone.Model.extend({

        defaults: {
            name: undefined,
            active: false,
            url: undefined,
            css: undefined
        }

    });
    return NavTabModel;

});