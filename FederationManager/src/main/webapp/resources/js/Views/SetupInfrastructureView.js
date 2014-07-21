/**
 * Created by dne on 23.05.14.
 */
define(["backbone"], function (Backbone) {


    var SetupInfrastructureView = Backbone.View.extend({
        _template: _.itemplate($("#setupInfTemplate").html()),
        render: function () {
            $(this.el).empty();
            $(this.el).html(this._template);
            this.delegateEvents({
                'click .commitResults': 'downloadToolbox'

            });
        },

        downloadToolbox: function (e) {
            e.preventDefault();
            console.log(this.model)
            this.model.regionStatus.set("status", "installed");
            this.model.regionStatus.unset("_links");
            this.model.patchStatus();
            window.location.href = "#infrastructures";
        },
        installGuide: function (e) {
            e.preventDefault();
            console.log("show Install Guide");

        }
    });
    return SetupInfrastructureView;

});
