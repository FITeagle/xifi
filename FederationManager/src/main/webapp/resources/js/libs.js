define(["backbone"], function(Backbone) {
    var UTILS = UTILS || {};

// Current version is **0.1**.

    UTILS.VERSION = '0.1';

// It has been developed by GING (New Generation Internet Group) in
// the Technical University of Madrid.
    UTILS.AUTHORS = 'GING';


    UTILS.i18n = (function (U, undefined) {

        var dict = {
        };

        var params = {
            lang: "en",
            dict: dict
        };

        function init() {
            _.extend(_, {
                itemplate: function (html) {
                    var simple = _.template(html);
                    var func = function (args) {
                        var init = simple(args);

                        init = U.i18n.translate(init);
                        return init;
                    };
                    return func;

                }
            });
            if (localStorage.i18nlang === undefined) {
                localStorage.i18nlang = 'en';
            }
            UTILS.i18n.setlang(localStorage.i18nlang);
            console.log("Language: " + localStorage.i18nlang);

        }

        function setlang(lang, callback) {
            var url = "locales/" + lang + ".json?random=" + Math.random() * 99999;
            $.ajax({
                url: url,
                success: function (data, status, xhr) {
                    console.log('loaded: ' + url);
                    U.i18n.params.dict = data;
                    localStorage.i18nlang = lang;
                    if (callback !== undefined)
                        callback();
                },
                error: function (xhr, status, error) {
                    console.log('failed loading: ' + url);
                    if (callback !== undefined)
                        callback();
                },
                dataType: "json"
            });
        }

        function translateNodes(el) {
            var html = $(el);
            var items = html.find("*[data-i18n]");
            items.each(function (index, item) {
                item = $(items[index], el);
                var newItem = U.i18n.get(item.attr("data-i18n"));
                if (newItem !== undefined) {
                    var copy = item.clone();
                    item.text(newItem);
                    html.find(copy).replaceWith(item);
                }
            });
            return html;
        }

        function translate(html) {
            var initTime = new Date().getTime();
            html = translateNodes(html);
            var duration = new Date().getTime() - initTime;
            //console.log("Internationalization duration: " + duration);
            return html;
        }

        function pluralise(s, p, n) {
            var text = U.i18n.get(s);
            if (n != 1)
                text = U.i18n.get(p);
            var out = sprintf(text, n);
            return out;
        }

        function get(data) {
            var newItem = U.i18n.params.dict[data];
            if (newItem === undefined)
                newItem = data;
            return newItem;
        }

        function sprintf(s) {
            var bits = s.split('%');
            var out = bits[0];
            var re = /^([ds])(.*)$/;
            for (var i = 1; i < bits.length; i++) {
                p = re.exec(bits[i]);
                if (!p || arguments[i] === null)
                    continue;
                if (p[1] == 'd') {
                    out += parseInt(arguments[i], 10);
                } else if (p[1] == 's') {
                    out += arguments[i];
                }
                out += p[2];
            }
            return out;
        }

        return {
            params: params,
            init: init,
            setlang: setlang,
            translate: translate,
            get: get,
            pluralise: pluralise
        };
    })(UTILS);

    UTILS.i18n.init();

    return UTILS;
});