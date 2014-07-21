/**
 * Created by dne on 23.05.14.
 */
define(["backbone", "models", "libs"], function(Backbone) {

    var NameSuggestion = Backbone.Model.extend({
        defaults: {
            lng: 0,
            lat: 0,
            names: null
        },

        getNames: function (lng, lat) {
            this.lng = lng;
            this.lat = lat;
            var self = this
            var host = document.URL.match(/http.?:\/\/([^\/]*)\/.*/)[1];
            ;
            $.ajax({
                url: 'http://' + host + '/federationManager/api/v3/locationname?lng=' + lng + '&lat=' + lat,
                async: false, // synchonous call in case code tries to use template before it's loaded
                type: 'GET',
                dataType: 'json',
                success: function (data) {
                    self.set('names', data.names);
                }
            });


        }
    });

    return NameSuggestion;
});