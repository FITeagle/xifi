/**
 * Created by dne on 23.05.14.
 */
requirejs.config({

    paths: {
        "jquery": "jquery-1.11.1.min",
        "underscore": "underscore-min",
        "backbone": "backbone-min",
        "libs": "../../js/libs",
        "routes" : "../../js/routes",
        "bootstrap" :"bootstrap.min",
        "navTabModels": "../../js/Models/NavTabModels",
        "navTabModel": "../../js/Models/NavTabModel",
        "loginStatus": "../../js/Models/LoginStatus",
        "user": "../../js/Models/User",
        "rootView": "../../js/Views/RootView",
        "allRegionsView": "../../js/Views/AllRegionsView",
        "regions": "../../js/Models/Regions",
        "region": "../../js/Models/Region",
        "services": "../../js/Models/Services",
        "service": "../../js/Models/Service",
        "endpoints": "../../js/Models/Endpoints",
        "endpoint": "../../js/Models/Endpoint",
        "newServiceTypeView": "../../js/Views/NewServiceTypeView",
        "changeStatusView" :"../../js/Views/ChangeStatusView",
        "editRegionView" : "../../js/Views/EditRegionView",
        "newInfrastructureView" : "../../js/Views/NewInfrastructureView",
        "newEndpointTemplateView": "../../js/Views/NewEndpointTemplateView",
        "regionStatus" :"../../js/Models/RegionStatus",
        "contacts" :"../../js/Models/Contacts",
        "contact" :"../../js/Models/Contact",
        "quickOnlineTestView" : "../../js/Views/QuickOnlineTestView",
        "setupInfrastructureView" : "../../js/Views/SetupInfrastructureView",
        "member" : "../../js/Models/Member",
        "myRegionsView" :  "../../js/Views/MyRegionsView",
        "organizations" : "../../js/Models/Organizations",
        "organization" : "../../js/Models/Organization",
        "navTabView" : "../../js/Views/NavTabView",
        "toolsView" : "../../js/Views/ToolsView"
    },

    shim: {
        backbone: {
            deps: ['jquery','underscore'],
            exports: 'Backbone'
        },
        underscore: {
            exports :"_"
        },
        bootstrap: {
            deps :["jquery"]
        }
    }
});
require(["jquery"], function($){

    $('.bar').css({
        width: "0%"
    });
    loadTemplates([
            'resources/templates/root/root.html',
            'resources/templates/root/navTab.html',
            'resources/templates/root/topBar.html',
            'resources/templates/root/user.html',
            'resources/templates/root/sideBar.html',
            'resources/templates/root/settings.html',
            'resources/templates/registrationWF/publicPrivateNode.html',
            'resources/templates/registrationWF/qot.html',
            'resources/templates/registrationWF/legalCompliance.html',
            'resources/templates/registrationWF/technicalCompliance.html',
            'resources/templates/registrationWF/operationalCompliance.html',
            'resources/templates/registrationWF/nodeDetailsTemplate.html',
            'resources/templates/registrationWF/contact.html',
            'resources/templates/registrationWF/qot_end.html',
            'resources/templates/registrationWF/startSetupInf.html',
            'resources/templates/registrationWF/testInstallation.html',
            'resources/templates/registrationWF/myRegionsTemplate.html',
            'resources/templates/admin/allRegionsTemplate.html',
            'resources/templates/admin/newEndpointTemplate.html',
            'resources/templates/admin/addServiceTypeTemplate.html',
            'resources/templates/admin/newInfrastructureTemplate.html',
            'resources/templates/admin/editRegionTemplate.html',
            'resources/templates/admin/changeStatus.html',
            'resources/templates/registrationWF/toolsViewTemplate.html'
        ],
        function () {
            $('.bar').css({
                width: "30%"

            });
            console.log("Templates loaded.");



            $('#initbar').hide();
            host = document.URL.match(/((http|file).?:\/\/[^\/]*)\/.*/)[1];
            //UTILS.Auth.initialize("http://")


        });
    require(['libs','routes', "backbone", "bootstrap"], function(UTILS,OSRouter,Backbone) {
           var fiRouter = new OSRouter();
           Backbone.history.start();

    });
});

function loadTemplates(urls, callback) {
    var total = urls.length;
    var amount = 0;
    for (var index in urls) {
        var url = urls[index];

        $.ajax({
            url: url + "?random=" + Math.random() * 99999,
            asynx: false, // synchonous call in case code tries to use template before it's loaded
            success: function (response) {
                $('head').append(response);
                amount++;
                if (amount == total) {
                    callback();
                }
            }
        });
    }
};




