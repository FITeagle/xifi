/**
 * Created by dne on 23.05.14.
 */

define(["backbone", "services", "endpoints", "regions", "newEndpointTemplateView",  "editRegionView", "quickOnlineTestView", "setupInfrastructureView"], function (Backbone, Services, Endpoints, Regions, NewEndpointTemplateView, EditRegionView, QuickOnlineTestView, SetupInfrastructureView) {


    var MyRegionsView = Backbone.View.extend({
        _template: _.itemplate($("#myRegionsTemplate").html()),
        models: null,
        loginModel: null,
        services: null,
        initialize: function (options) {
            this.options = options;
            this.loginModel = this.options.loginModel;
        },
        render: function () {
            this.services = new Services();
            this.services.fetch({async: false});
            var endpoints = new Endpoints();
            endpoints.fetch({async: false});
            var org_id = this.loginModel.get("user").get("organizations").at(0).get("displayName");
            console.log("get regions for" + org_id);
            var regions = new Regions();
            regions.fetch({async: false, data: {"adminUsername": org_id}});
            regions.each(function (region) {
                region.contacts.fetch({async: false});
                region.regionStatus.fetch({async: false});
                if (endpoints != null && endpoints.length > 0) {
                    region.set("endpoints", endpoints.where({region: region.get("uuid")}));
                }
                else {
                    region.set('endpoints', new Endpoints());
                }
            });


            this.models = regions;
            var newTemplate = this._template({
                models: this.models
            })
            $(this.el).empty();
            $(this.el).html(newTemplate);
            this.delegateEvents({
                'click .addNewInf': 'addNewInf',
                'click .installationB': 'installPage',
                'click .deleteRegion': 'deleteRegion',
                'click .editRegion': 'editRegion',
                'click .addEndpoint': 'addEndpoint'

            });


        },

        addEndpoint: function (e) {
            var region = this.models.get($(e.currentTarget).data("id"));
            var newService = new NewEndpointTemplateView({
                el: "#myModal",
                services: this.services,
                parent: this,
                region: region
            });
            newService.render();
        },
        editRegion: function (e) {
            var regionToEdit = this.models.get($(e.currentTarget).data("id"));
            var view = new EditRegionView({
                el: "#myModal",
                parent: this,
                model: regionToEdit,
                admin: false
            });
            view.render();
        },


        deleteRegion: function (e) {
            var regionToDelete = this.models.get($(e.currentTarget).data("id"));
            regionToDelete.delete();


            this.render();
        },

        addNewInf: function (e) {

            var qot = new QuickOnlineTestView({
                el: "#myModal",
                loginModel: this.loginModel,
                parent: this
            });
            qot.render();
        },


        installPage: function (e) {
            e.preventDefault();
            var modelId = e.currentTarget.parentElement.parentElement.firstElementChild.textContent;
            modelId = new String(modelId);

            modelId = modelId.trim();
            var currentModel = this.models.get(modelId);
            var view = new SetupInfrastructureView({
                el: "#content",
                model: currentModel
            });
            view.render();
        }
    });

    return MyRegionsView;
});