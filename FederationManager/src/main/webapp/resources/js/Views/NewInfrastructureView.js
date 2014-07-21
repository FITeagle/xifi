/**
 * Created by dne on 23.05.14.
 */

define(["backbone", "region"], function (Backbone, Region) {

    var NewInfrastructureView = Backbone.View.extend({
        _template: _.itemplate($("#newInfrastructureTemplate").html()),
        model: null,
        parent: null,

        initialize: function (options) {
            this.options = options;
            this.parent = this.options.parent;

        },
        close: function (e) {

            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');
            return true;
        },
        render: function () {
            var newInfrastructureTemplate = this._template({ });
            $(this.el).empty();
            $(this.el).html(newInfrastructureTemplate);
            this.delegateEvents({
                'click .addInf': 'addInf',
                'click .close': 'close'

            });

        },

        addInf: function () {

            var region = new Region();
            region.set("id", $("#infrastructureName").val());
            region.set("country", $("#infrastructureCountry").val());
            region.set("latitude", $("#infrastructureLatitude").val());
            region.set("longitude", $("#infrastructureLongitude").val());
            region.set("adminUsername", $("#ownerID").val());
            region.set("nodeType", $("#nodeType").val());
            region.set("public", "true");
            region.post();
            this.parent.render();

            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');

        }
        /*
         defaults: {
         country: "",
         latitude: "",
         longitude: "",
         adminUsername: "",
         regionStatus: null,
         nodeType: "",
         contacts: null
         },
         */

    });

    return NewInfrastructureView;
});