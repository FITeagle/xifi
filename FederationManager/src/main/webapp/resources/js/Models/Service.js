/**
 * Created by dne on 23.05.14.
 */
define(["backbone"], function(Backbone) {

    var Service = Backbone.Model.extend({
        defaults: {
            id: 0,
            type: ""
        },
        post: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            var self = JSON.stringify(this);
            var par = this;
            console.log(self);
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/services',
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
        },
        delete: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];

            var par = this;
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/services/' + par.get('id'),
                async: false, // synchonous call in case code tries to use template before it's loaded
                type: 'DELETE',

                success: function (data) {

                }
            });
        },
        patch: function () {
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            var par = this;
            var some = this.toJSON();
            delete some._links;
            var self = JSON.stringify(some);
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/services/' + par.get('id'),
                async: false, // synchonous call in case code tries to use template before it's loaded
                type: 'PATCH',
                data: self,
                processData: false,
                contentType: 'application/json',
                success: function (data) {
                    console.log(data);
                }
            });
        }
    });
    return Service;
});