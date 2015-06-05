var OAuth;
if (OAuth == null) {
    OAuth = {}
}
OAuth.setProperties = function setProperties(f, g) {
    if (f != null && g != null) {
        for (var d in g) {
            f[d] = g[d]
        }
    }
    return f
};
OAuth.setProperties(OAuth, {
    percentEncode: function percentEncode(d) {
        if (d == null) {
            return ""
        }
        if (d instanceof Array) {
            var g = "";
            for (var f = 0; f < d.length; ++d) {
                if (g != "") {
                    g += "&"
                }
                g += OAuth.percentEncode(d[f])
            }
            return g
        }
        d = encodeURIComponent(d);
        d = d.replace(/\!/g, "%21");
        d = d.replace(/\*/g, "%2A");
        d = d.replace(/\'/g, "%27");
        d = d.replace(/\(/g, "%28");
        d = d.replace(/\)/g, "%29");
        return d
    }, decodePercent: function decodePercent(b) {
        if (b != null) {
            b = b.replace(/\+/g, " ")
        }
        return decodeURIComponent(b)
    }, getParameterList: function getParameterList(f) {
        if (f == null) {
            return []
        }
        if (typeof f != "object") {
            return OAuth.decodeForm(f + "")
        }
        if (f instanceof Array) {
            return f
        }
        var d = [];
        for (var g in f) {
            d.push([g, f[g]])
        }
        return d
    }, getParameterMap: function getParameterMap(f) {
        if (f == null) {
            return {}
        }
        if (typeof f != "object") {
            return OAuth.getParameterMap(OAuth.decodeForm(f + ""))
        }
        if (f instanceof Array) {
            var h = {};
            for (var i = 0; i < f.length; ++i) {
                var g = f[i][0];
                if (h[g] === undefined) {
                    h[g] = f[i][1]
                }
            }
            return h
        }
        return f
    }, getParameter: function getParameter(d, f) {
        if (d instanceof Array) {
            for (var g = 0; g < d.length; ++g) {
                if (d[g][0] == f) {
                    return d[g][1]
                }
            }
        } else {
            return OAuth.getParameterMap(d)[f]
        }
        return null
    }, formEncode: function formEncode(g) {
        var h = "";
        var j = OAuth.getParameterList(g);
        for (var i = 0; i < j.length; ++i) {
            var k = j[i][1];
            if (k == null) {
                k = ""
            }
            if (h != "") {
                h += "&"
            }
            h += OAuth.percentEncode(j[i][0]) + "=" + OAuth.percentEncode(k)
        }
        return h
    }, decodeForm: function decodeForm(p) {
        var n = [];
        var l = p.split("&");
        for (var m = 0; m < l.length; ++m) {
            var k = l[m];
            if (k == "") {
                continue
            }
            var q = k.indexOf("=");
            var j;
            var o;
            if (q < 0) {
                j = OAuth.decodePercent(k);
                o = null
            } else {
                j = OAuth.decodePercent(k.substring(0, q));
                o = OAuth.decodePercent(k.substring(q + 1))
            }
            n.push([j, o])
        }
        return n
    }, setParameter: function setParameter(k, h, j) {
        var g = k.parameters;
        if (g instanceof Array) {
            for (var i = 0; i < g.length; ++i) {
                if (g[i][0] == h) {
                    if (j === undefined) {
                        g.splice(i, 1)
                    } else {
                        g[i][1] = j;
                        j = undefined
                    }
                }
            }
            if (j !== undefined) {
                g.push([h, j])
            }
        } else {
            g = OAuth.getParameterMap(g);
            g[h] = j;
            k.parameters = g
        }
    }, setParameters: function setParameters(i, f) {
        var h = OAuth.getParameterList(f);
        for (var g = 0; g < h.length; ++g) {
            OAuth.setParameter(i, h[g][0], h[g][1])
        }
    }, completeRequest: function completeRequest(d, f) {
        if (d.method == null) {
            d.method = "GET"
        }
        var g = OAuth.getParameterMap(d.parameters);
        if (g.oauth_consumer_key == null) {
            OAuth.setParameter(d, "oauth_consumer_key", f.consumerKey || "")
        }
        if (g.oauth_token == null && f.token != null) {
            OAuth.setParameter(d, "oauth_token", f.token)
        }
        if (g.oauth_version == null) {
            OAuth.setParameter(d, "oauth_version", "1.0")
        }
        if (g.oauth_timestamp == null) {
            OAuth.setParameter(d, "oauth_timestamp", OAuth.timestamp())
        }
        if (g.oauth_nonce == null) {
            OAuth.setParameter(d, "oauth_nonce", OAuth.nonce(6))
        }
        OAuth.SignatureMethod.sign(d, f)
    }, setTimestampAndNonce: function setTimestampAndNonce(b) {
        OAuth.setParameter(b, "oauth_timestamp", OAuth.timestamp());
        OAuth.setParameter(b, "oauth_nonce", OAuth.nonce(6))
    }, addToURL: function addToURL(g, i) {
        newURL = g;
        if (i != null) {
            var f = OAuth.formEncode(i);
            if (f.length > 0) {
                var h = g.indexOf("?");
                if (h < 0) {
                    newURL += "?"
                } else {
                    newURL += "&"
                }
                newURL += f
            }
        }
        return newURL
    }, getAuthorizationHeader: function getAuthorizationHeader(j, o) {
        var k = 'OAuth realm="' + OAuth.percentEncode(j) + '"';
        var n = OAuth.getParameterList(o);
        for (var m = 0; m < n.length; ++m) {
            var l = n[m];
            var i = l[0];
            if (i.indexOf("oauth_") == 0) {
                k += "," + OAuth.percentEncode(i) + '="' + OAuth.percentEncode(l[1]) + '"'
            }
        }
        return k
    }, correctTimestampFromSrc: function correctTimestampFromSrc(g) {
        g = g || "oauth_timestamp";
        var h = document.getElementsByTagName("script");
        if (h == null || !h.length) {
            return
        }
        var i = h[h.length - 1].src;
        if (!i) {
            return
        }
        var j = i.indexOf("?");
        if (j < 0) {
            return
        }
        parameters = OAuth.getParameterMap(OAuth.decodeForm(i.substring(j + 1)));
        var k = parameters[g];
        if (k == null) {
            return
        }
        OAuth.correctTimestamp(k)
    }, correctTimestamp: function correctTimestamp(b) {
        OAuth.timeCorrectionMsec = (b * 1000) - (new Date()).getTime()
    }, timeCorrectionMsec: 0, timestamp: function timestamp() {
        var b = (new Date()).getTime() + OAuth.timeCorrectionMsec;
        return Math.floor(b / 1000)
    }, nonce: function nonce(i) {
        var j = OAuth.nonce.CHARS;
        var h = "";
        for (var k = 0; k < i; ++k) {
            var g = Math.floor(Math.random() * j.length);
            h += j.substring(g, g + 1)
        }
        return h
    }
});
OAuth.nonce.CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz";
OAuth.declareClass = function declareClass(j, h, k) {
    var i = j[h];
    j[h] = k;
    if (k != null && i != null) {
        for (var g in i) {
            if (g != "prototype") {
                k[g] = i[g]
            }
        }
    }
    return k
};
OAuth.declareClass(OAuth, "SignatureMethod", function OAuthSignatureMethod() {
});
OAuth.setProperties(OAuth.SignatureMethod.prototype, {
    sign: function sign(g) {
        var d = OAuth.SignatureMethod.getBaseString(g);
        var f = this.getSignature(d);
        OAuth.setParameter(g, "oauth_signature", f);
        return f
    }, initialize: function initialize(g, f) {
        var d;
        if (f.accessorSecret != null && g.length > 9 && g.substring(g.length - 9) == "-Accessor") {
            d = f.accessorSecret
        } else {
            d = f.consumerSecret
        }
        this.key = OAuth.percentEncode(d) + "&" + OAuth.percentEncode(f.tokenSecret)
    }
});
OAuth.setProperties(OAuth.SignatureMethod, {
    sign: function sign(g, f) {
        var d = OAuth.getParameterMap(g.parameters).oauth_signature_method;
        if (d == null || d == "") {
            d = "HMAC-SHA1";
            OAuth.setParameter(g, "oauth_signature_method", d)
        }
        OAuth.SignatureMethod.newMethod(d, f).sign(g)
    }, newMethod: function newMethod(n, j) {
        var o = OAuth.SignatureMethod.REGISTERED[n];
        if (o != null) {
            var k = new o();
            k.initialize(n, j);
            return k
        }
        var l = new Error("signature_method_rejected");
        var i = "";
        for (var m in OAuth.SignatureMethod.REGISTERED) {
            if (i != "") {
                i += "&"
            }
            i += OAuth.percentEncode(m)
        }
        l.oauth_acceptable_signature_methods = i;
        throw l
    }, REGISTERED: {}, registerMethodClass: function registerMethodClass(d, f) {
        for (var g = 0; g < d.length; ++g) {
            OAuth.SignatureMethod.REGISTERED[d[g]] = f
        }
    }, makeSubclass: function makeSubclass(f) {
        var d = OAuth.SignatureMethod;
        var g = function () {
            d.call(this)
        };
        g.prototype = new d();
        g.prototype.getSignature = f;
        g.prototype.constructor = g;
        return g
    }, getBaseString: function getBaseString(j) {
        var a = j.action;
        var i = a.indexOf("?");
        var k;
        if (i < 0) {
            k = j.parameters
        } else {
            k = OAuth.decodeForm(a.substring(i + 1));
            var l = OAuth.getParameterList(j.parameters);
            for (var m = 0; m < l.length; ++m) {
                k.push(l[m])
            }
        }
        return OAuth.percentEncode(j.method.toUpperCase()) + "&" + OAuth.percentEncode(OAuth.SignatureMethod.normalizeUrl(a)) + "&" + OAuth.percentEncode(OAuth.SignatureMethod.normalizeParameters(k))
    }, normalizeUrl: function normalizeUrl(o) {
        var n = OAuth.SignatureMethod.parseUri(o);
        var j = n.protocol.toLowerCase();
        var l = n.authority.toLowerCase();
        var k = (j == "http" && n.port == 80) || (j == "https" && n.port == 443);
        if (k) {
            var i = l.lastIndexOf(":");
            if (i >= 0) {
                l = l.substring(0, i)
            }
        }
        var m = n.path;
        if (!m) {
            m = "/"
        }
        return j + "://" + l + m
    }, parseUri: function parseUri(i) {
        var j = {
            key: ["source", "protocol", "authority", "userInfo", "user", "password", "host", "port", "relative", "path", "directory", "file", "query", "anchor"],
            parser: {strict: /^(?:([^:\/?#]+):)?(?:\/\/((?:(([^:@\/]*):?([^:@\/]*))?@)?([^:\/?#]*)(?::(\d*))?))?((((?:[^?#\/]*\/)*)([^?#]*))(?:\?([^#]*))?(?:#(.*))?)/}
        };
        var h = j.parser.strict.exec(i);
        var k = {};
        var g = 14;
        while (g--) {
            k[j.key[g]] = h[g] || ""
        }
        return k
    }, normalizeParameters: function normalizeParameters(n) {
        if (n == null) {
            return ""
        }
        var m = OAuth.getParameterList(n);
        var k = [];
        for (var l = 0; l < m.length; ++l) {
            var i = m[l];
            if (i[0] != "oauth_signature") {
                k.push([OAuth.percentEncode(i[0]) + " " + OAuth.percentEncode(i[1]), i])
            }
        }
        k.sort(function (a, b) {
            if (a[0] < b[0]) {
                return -1
            }
            if (a[0] > b[0]) {
                return 1
            }
            return 0
        });
        var j = [];
        for (var o = 0; o < k.length; ++o) {
            j.push(k[o][1])
        }
        return OAuth.formEncode(j)
    }
});
OAuth.SignatureMethod.registerMethodClass(["PLAINTEXT", "PLAINTEXT-Accessor"], OAuth.SignatureMethod.makeSubclass(function getSignature(b) {
    return this.key
}));
OAuth.SignatureMethod.registerMethodClass(["HMAC-SHA1", "HMAC-SHA1-Accessor"], OAuth.SignatureMethod.makeSubclass(function getSignature(c) {
    b64pad = "=";
    var d = b64_hmac_sha1(this.key, c);
    return d
}));
try {
    OAuth.correctTimestampFromSrc()
} catch (e) {
}
;