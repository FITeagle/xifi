/**
 * Created by dne on 23.05.14.
 */
define(["backbone","regionStatus", "contacts"], function(Backbone, RegionStatus, Contacts) {

    var Region = Backbone.Model.extend({
        defaults: {
            id: "",
            uuid: "",
            country: "",
            latitude: "",
            longitude: "",
            adminUsername: "",
            regionStatus: null,
            nodeType: "",
            contacts: null,
            endpoints: null,
            public: ""

        },

        initialize: function () {
            this.regionStatus = new RegionStatus();
            this.regionStatus.url = "/federationManager/api/v3/regions/" + this.get('uuid') + "/status"
            this.contacts = new Contacts();
            this.contacts.url = "/federationManager/api/v3/regions/" + this.get('uuid') + "/contacts"
        },

        post: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            var some = this.toJSON();
            delete some.endpoints;
            var self = JSON.stringify(some);
            // var self = JSON.stringify(this);
            //delete self.endpoints;
            var par = this;
            console.log(self);
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/regions',
                async: false, // synchonous call in case code tries to use template before it's loaded
                data: self,
                type: 'POST',
                processData: false,
                contentType: 'application/json',
                success: function (data) {
                    console.log(data);
                    par.set('uuid', data.uuid);
                }
            });
            console.log(JSON.stringify(this));
        },

        fetchStatus: function (modelid) {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            var self = JSON.stringify(this);
            var par = this;
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/regions/' + par.get('uuid') + '/status',
                async: false, // synchonous call in case code tries to use template before it's loaded
                type: 'GET',
                processData: false,
                contentType: 'application/json',
                success: function (data) {
                    console.log(data);
                    par.set({
                        regionStatus: data
                    })
                }
            });
        },

        patchStatus: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            var self = JSON.stringify(this.regionStatus);
            var par = this;
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/regions/' + par.get('uuid') + '/status',
                async: false, // synchonous call in case code tries to use template before it's loaded
                type: 'PATCH',
                data: self,
                processData: false,
                contentType: 'application/json',
                success: function (data) {
                    console.log(data);
                }
            });

        },

        delete: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];

            var par = this;
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/regions/' + par.get('uuid'),
                async: false, // synchonous call in case code tries to use template before it's loaded
                type: 'DELETE',

                success: function (data) {
                    console.log(data);
                }
            });
        },

        patch: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            var par = this;
            var some = this.toJSON();
            delete some.endpoints;
            delete some._links;
            var self = JSON.stringify(some);
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/regions/' + par.get('uuid'),
                async: false, // synchonous call in case code tries to use template before it's loaded
                type: 'PATCH',
                data: self,
                processData: false,
                contentType: 'application/json',
                success: function (data) {
                    console.log(data);
                }
            });
        },
        logIt: function () {
            console.log("Logging:" + this.country)
        }
    });

    return Region;
});