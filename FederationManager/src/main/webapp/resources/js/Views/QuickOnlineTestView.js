/**
 * Created by dne on 23.05.14.
 */

define(["backbone", "member", "region", "contact"], function (Backbone, Member, Region, Contact) {

    var QuickOnlineTestView = Backbone.View.extend({
        _template: _.itemplate($("#qotTemplate").html()),
        _legalComplianceTemplate: _.itemplate($("#legalComplianceTemplate").html()),
        _operationalComplianceTemplate: _.itemplate($("#operationalComplianceTemplate").html()),
        _technicalComplianceTemplate: _.itemplate($("#technicalComplianceTemplate").html()),
        _nodeDetailsTemplate: _.itemplate($("#nodeDetailsTemplate").html()),
        _contactTemplate: _.itemplate($("#contactTemplate").html()),
        _qotEndTemplate: _.itemplate($("#qotEndTemplate").html()),
        _publicPrivateTemplate: _.itemplate($("#publicPrivateNode").html()),
        loginModel: null,
        region: null,
        memberCount: 0,
        members: null,
        parent: null,

        initialize: function (options) {
            this.options = options;
            this.loginModel = this.options.loginModel;
            this.members = new Array();
            this.parent = this.options.parent;
        },
        close: function (e) {

            this.$el.empty();
            this.undelegateEvents();
            $('#myModal').modal('hide');
            return true;
        },
        render: function () {
            $(this.el).empty();
            $(this.el).html(this._template);
            this.delegateEvents({
                'click .startSurvey': 'startSurvey',
                'click .close': 'close'

            });
        },

        startSurvey: function (e) {

            this.region = new Region();
            this.region.set('organizationName', this.loginModel.get('user').get('organizations').at(0).get("displayName"));
            $(this.el).empty();
            $(this.el).html(this._legalComplianceTemplate);
            this.delegateEvents({
                'click #checkLegal': 'toggleContinueButton1',
                'click #btnagree1': 'operationalCompliance',
                'click .close': 'close'

            });
        },

        toggleContinueButton1: function (e) {

            if ($(":checkbox").prop('checked',true)) {
                $(".step").removeAttr("disabled");
            } else {
                $(".step").attr("disabled", "disabled");
            }

        },

        operationalCompliance: function (e) {
            e.preventDefault();
            this.region.regionStatus.set('legalCompliance', true);

            $(this.el).empty();
            $(this.el).html(this._operationalComplianceTemplate);
            this.delegateEvents({
                'click #checkOper': 'toggleContinueButton1',
                'click #btnagree2': 'technicalCompliance',
                'click .close': 'close'

            });
        },
        technicalCompliance: function (e) {
            e.preventDefault();
            this.region.regionStatus.set('operationalCompliance', true);
            $(this.el).empty();
            $(this.el).html(this._technicalComplianceTemplate);
            this.delegateEvents({
                'click #checkTechnical': 'toggleContinueButton1',
                'click #btnagree3': 'publicPrivate',
                'click .close': 'close'

            });
        },

        publicPrivate: function (e) {
            this.region.regionStatus.set('technicalCompliance', true);

            $(this.el).empty();
            $(this.el).html(this._publicPrivateTemplate);
            this.delegateEvents({
                'click #publicYes': 'publicYes',
                'click #publicNo': 'publicNo',
                'click #submitPrivate': 'submitPrivate',
                'click #addMember': 'addMember',
                'click .close': 'close'

            });
        },

        addMember: function (e) {
            this.memberCount++;
            var row = '<label for="rowNum' + this.memberCount + '">Tenant-ID:</label><input type="text" class="member" id="rowNum' + this.memberCount + '">';
            $("#members").append(row);
        },

        submitPrivate: function (e) {
            var membersToBeAdded = $(".member");

            for (var idx = membersToBeAdded.length; idx > 0; idx--) {
                console.log($("#rowNum" + idx).val());
                var newMember = new Member();
                newMember.set("tenant_id", $("#rowNum" + idx).val());
                this.members.push(newMember);
                this.region.set("public", "false");
                this.region.set("id", $("#regionName").val());

                this.region.regionStatus.set({
                    timestamp: Date.now(),
                    status: "Config Pending",
                    legalCompliance: true,
                    operationalCompliance: true,
                    technicalCompliance: true
                });
                this.nodeDetails(e);
            }

        },
        publicYes: function (e) {

            this.region.set("public", "true");
            this.region.set("id", "tempID");

            this.region.regionStatus.set({
                timestamp: Date.now(),
                status: "New",
                legalCompliance: true,
                operationalCompliance: true,
                technicalCompliance: true
            });
            this.nodeDetails(e);
        },
        publicNo: function (e) {
            $('#chooseyourdestiny *').attr('disabled', true);

            $("#privateParts").show();
        },
        nodeDetails: function (e) {
            e.preventDefault();

            $(this.el).empty();
            $(this.el).html(this._nodeDetailsTemplate);
            $('#toContacts').attr("disabled", "disabled");
            this.delegateEvents({

                'click #validSupForm': 'contact',
                'click .close': 'close'


            });
        },

        getSuggestions: function (e) {
            e.preventDefault();

            var lng = $("#nodeLongitude").val();
            var lat = $("#nodeLatitude").val();

            var suggestion = new NameSuggestion();
            suggestion.getNames(lng, lat);
            for (var i = 0; i < suggestion.get('names').length; i++) {
                console.log(suggestion.get('names')[i]);
            }
        },
        contact: function (e) {
            e.preventDefault();
            this.region.set('country', ($("#nodeCountry").val()));
            this.region.set('longitude', ($("#nodeLongitude").val()));
            this.region.set('latitude', ($("#nodeLatitude").val()));

            this.region.set("nodeType", "slave");

            this.region.post();
            $(this.el).empty();
            $(this.el).html(this._contactTemplate);
            this.delegateEvents({
                'click #checkTechnical': 'toggleContinueButton1',
                'click #endSurvey': 'endSurvey',
                'click .close': 'close'

            });
        },

        endSurvey: function (e) {
            e.preventDefault();


            //read forms
            var organizationForm = $('#organizationForm');
            var organizationModel = new Contact();
            organizationModel = organizationModel.parseForm(organizationForm);
            organizationModel.set('type', "organization");

            var managementForm = $("#managementForm");
            var managementModel = new Contact();
            managementModel = managementModel.parseForm(managementForm);
            managementModel.set('type', "management");

            var supportForm = $('#supportForm');
            var supportModel = new Contact();
            supportModel = supportModel.parseForm(supportForm);
            supportModel.set('type', "support");


            //this.model.set("step", 2);
            this.region.contacts.url = "/federationManager/api/v3/regions/" + this.region.uuid + "/contacts"
            this.region.contacts.create(organizationModel);
            this.region.contacts.create(managementModel);
            this.region.contacts.create(supportModel);


            this.region.patchStatus();
            this.parent.render();
            this.$el.empty();


            this.undelegateEvents();
            $('#myModal').modal('hide');


            $('body').removeClass('modal-open');
            $('.modal-backdrop').remove();

        }

    });

    return QuickOnlineTestView;

});