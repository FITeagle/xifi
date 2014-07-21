/**
 * Created by dne on 23.05.14.
 */
define(["backbone"], function(Backbone) {

    var Endpoint = Backbone.Model.extend({
        defaults: {
            interface: null,
            service_id: 0,
            region: 0,
            url: null,
            name: ""

        },

        post: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            var self = JSON.stringify(this);
            var par = this;

            console.log(self);
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/endpoints',
                async: false, // synchonous call in case code tries to use template before it's loaded
                data: self,
                type: 'POST',
                processData: false,
                contentType: 'application/json',
                success: function (data) {
                    par.set('id', data.id);

                }
            });
            console.log(JSON.stringify(this));
        }
    });
    return Endpoint;
});