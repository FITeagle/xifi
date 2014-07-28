/**
 * Created by dne on 23.05.14.
 */
define(["backbone"], function (Backbone) {

    var EditRegionView = Backbone.View.extend({
        _template: _.itemplate($("#editRegionTemplate").html()),
        model: null,
        parent: null,
        initialize: function (options) {
            this.options = options;
            this.model = this.options.model;
            this.parent = this.options.parent;
            this.admin = this.options.admin;
        },
        render: function () {
            var editRegionTemplate = this._template({
                model: this.model,
                admin: this.admin
            });
            $(this.el).empty();
            $(this.el).html(editRegionTemplate);
            this.delegateEvents({
                'click .editRegion': 'editRegion',
                'click .close': 'close'
            });
        },
        close: function (e) {

            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');
            return true;
        },

        editRegion: function (e) {
            this.model.set("id", $("#regionName").val());
            this.model.set("country", $("#regionCountry").val());
            this.model.set("latitude", $("#regionLat").val());
            this.model.set("longitude", $("#regionLong").val());
            this.model.set("organizationName", $("#regionOwner").val());
            this.model.set("nodeType", $("#nodeType").val());

            this.model.patch();
            this.parent.render();
            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');
            return true;
        }

    });
    return EditRegionView;

});