/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "endpoints", "regions", "services", "newServiceTypeView", "changeStatusView", "editRegionView", "newInfrastructureView", "newEndpointTemplateView"], function (Backbone, Endpoints, Regions, Services, NewServiceTypeView, ChangeStatusView, EditRegionView, NewInfrastructureView, NewEndpointTemplateView) {

    var AllRegionsView = Backbone.View.extend({
        _template: _.itemplate($("#allRegionsTemplate").html()),
        events: {
            'shown.bs.tab': 'setTab',
            'show.bs.modal': 'setTab'


        },

        initialize: function (options) {
            this.options = options;
            services = null;
            endpoints = null;
            privateRegions = null;
            selected = null;
        },

        render: function () {

            console.log("rendering all")
            var endpoints = new Endpoints();
            endpoints.fetch({async: false});

            var regions = new Regions();
            regions.fetch({async: false, data: {"public": "true"}});
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

            this.privateRegions = new Regions();
            this.privateRegions.fetch({async: false, data: {"public": "false"}});
            this.privateRegions.each(function (region) {
                region.contacts.fetch({async: false});
                region.regionStatus.fetch({async: false});
                if (endpoints != null && endpoints.length > 0) {
                    region.set("endpoints", endpoints.where({region: region.get("uuid")}));
                }
                else {
                    region.set('endpoints', new Endpoints());
                }
            });
            this.model = regions;
            this.services = new Services();
            this.services.fetch({async: false});
            var newTemplate = this._template({
                model: this.model,
                services: this.services,
                privateRegions: this.privateRegions

            });
            $(this.el).empty();
            $(this.el).html(newTemplate);
            this.delegateEvents({
                'click .addNewInf': 'addNewInf',
                'click .addEndpoint': 'addEndpoint',
                'click .addServiceType': 'addServiceType',
                'click .deleteRegion': 'deleteRegion',
                'click .editRegion': 'editRegion',
                'click .changeStatus': 'changeStatus',
                'click .editService': 'editService',
                'click .deleteService': 'deleteService',
                'click .changeStatusPrivate' : 'changeStatusPrivate'

            });

        },


        setTab: function (e) {
            console.log("someone called me");
        },
        editService: function (e) {
            var serviceToChange = this.services.get($(e.currentTarget).data("id"));
            var newServiceType = new NewServiceTypeView({
                el: "#myModal",
                parent: this,
                service: serviceToChange
            });
            newServiceType.render();


        },
        deleteService: function (e) {
            var serviceToChange = this.services.get($(e.currentTarget).data("id"));
            serviceToChange.delete();
            this.render();
        },
        changeStatus: function (e) {
            var regionToChange = this.model.get($(e.currentTarget).data("id"));
            var status = regionToChange.regionStatus;
            var changeStatusView = new ChangeStatusView({
                el: "#myModal",
                model: status,
                region: regionToChange,
                parent: this
            });
            changeStatusView.render();

        },
        changeStatusPrivate : function(e){
            var regionToChange = this.privateRegions.get($(e.currentTarget).data("id"));
            var status = regionToChange.regionStatus;
            var changeStatusView = new ChangeStatusView({
                el: "#myModal",
                model: status,
                region: regionToChange,
                parent: this
            });
            changeStatusView.render();
        },
        editRegion: function (e) {
            var regionToEdit = this.model.get($(e.currentTarget).data("id"));
            var view = new EditRegionView({
                el: "#myModal",
                parent: this,
                model: regionToEdit,
                admin: true
            });
            view.render();

        },

        deleteRegion: function (e) {
            var regionToDelete = this.model.get($(e.currentTarget).data("id"));
            regionToDelete.delete();


            this.render();
        },
        addNewInf: function (e) {
            var newInf = new NewInfrastructureView({
                el: "#myModal",
                parent: this
            });
            newInf.render();
        },

        addEndpoint: function (e) {
            var region = this.model.get($(e.currentTarget).data("id"));
            var newService = new NewEndpointTemplateView({
                el: "#myModal",
                services: this.services,
                parent: this,
                region: region
            });
            newService.render();
        },
        addServiceType: function (e) {
            var newServiceType = new NewServiceTypeView({
                el: "#myModal",
                parent: this
            });
            newServiceType.render();

        }

    });

    return AllRegionsView;
});
