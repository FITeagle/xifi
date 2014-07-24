define(["backbone", "organization"], function(Backbone, Organization) {

    var Organizations = Backbone.Collection.extend({

        model: Organization,
        initialize : function(data){
            for(var i =0; i< data.length; i++){
                var o = new Organization(data[i]);
                this.add(o);
            }
        }
    });


    return Organizations;
});