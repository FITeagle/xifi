define(["backbone", "navTabModel"], function(Backbone, NavTabModel) {

    var NavTabModels = Backbone.Collection.extend({
        model: NavTabModel,

        setActive: function (name) {
            for (var index in this.models) {
                var tab = this.models[index];
                if (tab.get('name') === name) {
                    tab.set({
                        'active': true
                    });
                } else {
                    tab.set({
                        'active': false
                    });
                }
            }
            this.trigger("change:actives", "Changes");
        },

        getActive: function () {
            for (var index in this.models) {
                var tab = this.models[index];
                if (tab.get('active')) {
                    return tab.get('name');
                }
            }
        }
    });
    return NavTabModels;
});

/**
 * Created by dne on 23.05.14.
 */
