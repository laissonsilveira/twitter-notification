if (typeof window === "undefined" || window === null) {
    window = {twttr: {}}
}
if (window.twttr == null) {
    window.twttr = {}
}
if (typeof twttr === "undefined" || twttr === null) {
    twttr = {}
}
(function () {
    twttr.txt = {};
    twttr.txt.regexen = {};
    var h = {"&": "&amp;", ">": "&gt;", "<": "&lt;", '"': "&quot;", "'": "&#39;"};
    twttr.txt.htmlEscape = function (u) {
        return u && u.replace(/[&"'><]/g, function (v) {
                return h[v]
            })
    };
    function g(v, u) {
        u = u || "";
        if (typeof v !== "string") {
            if (v.global && u.indexOf("g") < 0) {
                u += "g"
            }
            if (v.ignoreCase && u.indexOf("i") < 0) {
                u += "i"
            }
            if (v.multiline && u.indexOf("m") < 0) {
                u += "m"
            }
            v = v.source
        }
        return new RegExp(v.replace(/#\{(\w+)\}/g, function (x, w) {
            var y = twttr.txt.regexen[w] || "";
            if (typeof y !== "string") {
                y = y.source
            }
            return y
        }), u)
    }

    twttr.txt.regexSupplant = g;
    function j(v, u) {
        return v.replace(/#\{(\w+)\}/g, function (x, w) {
            return u[w] || ""
        })
    }

    twttr.txt.stringSupplant = j;
    function d(v, x, u) {
        var w = String.fromCharCode(x);
        if (u !== x) {
            w += "-" + String.fromCharCode(u)
        }
        v.push(w);
        return v
    }

    twttr.txt.addCharsToCharClass = d;
    var m = String.fromCharCode;
    var c = [m(32), m(133), m(160), m(5760), m(6158), m(8232), m(8233), m(8239), m(8287), m(12288)];
    d(c, 9, 13);
    d(c, 8192, 8202);
    var b = [m(65534), m(65279), m(65535)];
    d(b, 8234, 8238);
    twttr.txt.regexen.spaces_group = g(c.join(""));
    twttr.txt.regexen.spaces = g("[" + c.join("") + "]");
    twttr.txt.regexen.invalid_chars_group = g(b.join(""));
    twttr.txt.regexen.punct = /\!'#%&'\(\)*\+,\\\-\.\/:;<=>\?@\[\]\^_{|}~\$/;
    var a = [];
    d(a, 1024, 1279);
    d(a, 1280, 1319);
    d(a, 11744, 11775);
    d(a, 42560, 42655);
    d(a, 1425, 1471);
    d(a, 1473, 1474);
    d(a, 1476, 1477);
    d(a, 1479, 1479);
    d(a, 1488, 1514);
    d(a, 1520, 1524);
    d(a, 64274, 64296);
    d(a, 64298, 64310);
    d(a, 64312, 64316);
    d(a, 64318, 64318);
    d(a, 64320, 64321);
    d(a, 64323, 64324);
    d(a, 64326, 64335);
    d(a, 1552, 1562);
    d(a, 1568, 1631);
    d(a, 1646, 1747);
    d(a, 1749, 1756);
    d(a, 1758, 1768);
    d(a, 1770, 1775);
    d(a, 1786, 1788);
    d(a, 1791, 1791);
    d(a, 1872, 1919);
    d(a, 2208, 2208);
    d(a, 2210, 2220);
    d(a, 2276, 2302);
    d(a, 64336, 64433);
    d(a, 64467, 64829);
    d(a, 64848, 64911);
    d(a, 64914, 64967);
    d(a, 65008, 65019);
    d(a, 65136, 65140);
    d(a, 65142, 65276);
    d(a, 8204, 8204);
    d(a, 3585, 3642);
    d(a, 3648, 3662);
    d(a, 4352, 4607);
    d(a, 12592, 12677);
    d(a, 43360, 43391);
    d(a, 44032, 55215);
    d(a, 55216, 55295);
    d(a, 65441, 65500);
    d(a, 12449, 12538);
    d(a, 12540, 12542);
    d(a, 65382, 65439);
    d(a, 65392, 65392);
    d(a, 65296, 65305);
    d(a, 65313, 65338);
    d(a, 65345, 65370);
    d(a, 12353, 12438);
    d(a, 12441, 12446);
    d(a, 13312, 19903);
    d(a, 19968, 40959);
    d(a, 173824, 177983);
    d(a, 177984, 178207);
    d(a, 194560, 195103);
    d(a, 12291, 12291);
    d(a, 12293, 12293);
    d(a, 12347, 12347);
    twttr.txt.regexen.nonLatinHashtagChars = g(a.join(""));
    var q = [];
    d(q, 192, 214);
    d(q, 216, 246);
    d(q, 248, 255);
    d(q, 256, 591);
    d(q, 595, 596);
    d(q, 598, 599);
    d(q, 601, 601);
    d(q, 603, 603);
    d(q, 611, 611);
    d(q, 616, 616);
    d(q, 623, 623);
    d(q, 626, 626);
    d(q, 649, 649);
    d(q, 651, 651);
    d(q, 699, 699);
    d(q, 768, 879);
    d(q, 7680, 7935);
    twttr.txt.regexen.latinAccentChars = g(q.join(""));
    twttr.txt.regexen.hashSigns = /[#＃]/;
    twttr.txt.regexen.hashtagAlpha = g(/[a-z_#{latinAccentChars}#{nonLatinHashtagChars}]/i);
    twttr.txt.regexen.hashtagAlphaNumeric = g(/[a-z0-9_#{latinAccentChars}#{nonLatinHashtagChars}]/i);
    twttr.txt.regexen.endHashtagMatch = g(/^(?:#{hashSigns}|:\/\/)/);
    twttr.txt.regexen.hashtagBoundary = g(/(?:^|$|[^&a-z0-9_#{latinAccentChars}#{nonLatinHashtagChars}])/);
    twttr.txt.regexen.validHashtag = g(/(#{hashtagBoundary})(#{hashSigns})(#{hashtagAlphaNumeric}*#{hashtagAlpha}#{hashtagAlphaNumeric}*)/gi);
    twttr.txt.regexen.validMentionPrecedingChars = /(?:^|[^a-zA-Z0-9_!#$%&*@＠]|RT:?)/;
    twttr.txt.regexen.atSigns = /[@＠]/;
    twttr.txt.regexen.validMentionOrList = g("(#{validMentionPrecedingChars})(#{atSigns})([a-zA-Z0-9_]{1,20})(/[a-zA-Z][a-zA-Z0-9_-]{0,24})?", "g");
    twttr.txt.regexen.validReply = g(/^(?:#{spaces})*#{atSigns}([a-zA-Z0-9_]{1,20})/);
    twttr.txt.regexen.endMentionMatch = g(/^(?:#{atSigns}|[#{latinAccentChars}]|:\/\/)/);
    twttr.txt.regexen.validUrlPrecedingChars = g(/(?:[^A-Za-z0-9@＠$#＃#{invalid_chars_group}]|^)/);
    twttr.txt.regexen.invalidUrlWithoutProtocolPrecedingChars = /[-_.\/]$/;
    twttr.txt.regexen.invalidDomainChars = j("#{punct}#{spaces_group}#{invalid_chars_group}", twttr.txt.regexen);
    twttr.txt.regexen.validDomainChars = g(/[^#{invalidDomainChars}]/);
    twttr.txt.regexen.validSubdomain = g(/(?:(?:#{validDomainChars}(?:[_-]|#{validDomainChars})*)?#{validDomainChars}\.)/);
    twttr.txt.regexen.validDomainName = g(/(?:(?:#{validDomainChars}(?:-|#{validDomainChars})*)?#{validDomainChars}\.)/);
    twttr.txt.regexen.validGTLD = g(/(?:(?:aero|asia|biz|cat|com|coop|edu|gov|info|int|jobs|mil|mobi|museum|name|net|org|pro|tel|travel|xxx)(?=[^0-9a-zA-Z]|$))/);
    twttr.txt.regexen.validCCTLD = g(/(?:(?:ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cx|cy|cz|dd|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)(?=[^0-9a-zA-Z]|$))/);
    twttr.txt.regexen.validPunycode = g(/(?:xn--[0-9a-z]+)/);
    twttr.txt.regexen.validDomain = g(/(?:#{validSubdomain}*#{validDomainName}(?:#{validGTLD}|#{validCCTLD}|#{validPunycode}))/);
    twttr.txt.regexen.validAsciiDomain = g(/(?:(?:[a-z0-9#{latinAccentChars}]+)\.)+(?:#{validGTLD}|#{validCCTLD}|#{validPunycode})/gi);
    twttr.txt.regexen.invalidShortDomain = g(/^#{validDomainName}#{validCCTLD}$/);
    twttr.txt.regexen.validPortNumber = g(/[0-9]+/);
    twttr.txt.regexen.validGeneralUrlPathChars = g(/[a-z0-9!\*';:=\+,\.\$\/%#\[\]\-_~|&#{latinAccentChars}]/i);
    twttr.txt.regexen.validUrlBalancedParens = g(/\(#{validGeneralUrlPathChars}+\)/i);
    twttr.txt.regexen.validUrlPathEndingChars = g(/[\+\-a-z0-9=_#\/#{latinAccentChars}]|(?:#{validUrlBalancedParens})/i);
    twttr.txt.regexen.validUrlPath = g("(?:(?:#{validGeneralUrlPathChars}*(?:#{validUrlBalancedParens}#{validGeneralUrlPathChars}*)*#{validUrlPathEndingChars})|(?:@#{validGeneralUrlPathChars}+/))", "i");
    twttr.txt.regexen.validUrlQueryChars = /[a-z0-9!?\*'\(\);:&=\+\$\/%#\[\]\-_\.,~|]/i;
    twttr.txt.regexen.validUrlQueryEndingChars = /[a-z0-9_&=#\/]/i;
    twttr.txt.regexen.extractUrl = g("((#{validUrlPrecedingChars})((https?:\\/\\/)?(#{validDomain})(?::(#{validPortNumber}))?(\\/#{validUrlPath}*)?(\\?#{validUrlQueryChars}*#{validUrlQueryEndingChars})?))", "gi");
    twttr.txt.regexen.validTcoUrl = /^https?:\/\/t\.co\/[a-z0-9]+/i;
    twttr.txt.regexen.cashtag = /[a-z]{1,6}(?:[._][a-z]{1,2})?/i;
    twttr.txt.regexen.validCashtag = g("(?:^|#{spaces})\\$(#{cashtag})(?=$|\\s|[#{punct}])", "gi");
    twttr.txt.regexen.validateUrlUnreserved = /[a-z0-9\-._~]/i;
    twttr.txt.regexen.validateUrlPctEncoded = /(?:%[0-9a-f]{2})/i;
    twttr.txt.regexen.validateUrlSubDelims = /[!$&'()*+,;=]/i;
    twttr.txt.regexen.validateUrlPchar = g("(?:#{validateUrlUnreserved}|#{validateUrlPctEncoded}|#{validateUrlSubDelims}|[:|@])", "i");
    twttr.txt.regexen.validateUrlScheme = /(?:[a-z][a-z0-9+\-.]*)/i;
    twttr.txt.regexen.validateUrlUserinfo = g("(?:#{validateUrlUnreserved}|#{validateUrlPctEncoded}|#{validateUrlSubDelims}|:)*", "i");
    twttr.txt.regexen.validateUrlDecOctet = /(?:[0-9]|(?:[1-9][0-9])|(?:1[0-9]{2})|(?:2[0-4][0-9])|(?:25[0-5]))/i;
    twttr.txt.regexen.validateUrlIpv4 = g(/(?:#{validateUrlDecOctet}(?:\.#{validateUrlDecOctet}){3})/i);
    twttr.txt.regexen.validateUrlIpv6 = /(?:\[[a-f0-9:\.]+\])/i;
    twttr.txt.regexen.validateUrlIp = g("(?:#{validateUrlIpv4}|#{validateUrlIpv6})", "i");
    twttr.txt.regexen.validateUrlSubDomainSegment = /(?:[a-z0-9](?:[a-z0-9_\-]*[a-z0-9])?)/i;
    twttr.txt.regexen.validateUrlDomainSegment = /(?:[a-z0-9](?:[a-z0-9\-]*[a-z0-9])?)/i;
    twttr.txt.regexen.validateUrlDomainTld = /(?:[a-z](?:[a-z0-9\-]*[a-z0-9])?)/i;
    twttr.txt.regexen.validateUrlDomain = g(/(?:(?:#{validateUrlSubDomainSegment]}\.)*(?:#{validateUrlDomainSegment]}\.)#{validateUrlDomainTld})/i);
    twttr.txt.regexen.validateUrlHost = g("(?:#{validateUrlIp}|#{validateUrlDomain})", "i");
    twttr.txt.regexen.validateUrlUnicodeSubDomainSegment = /(?:(?:[a-z0-9]|[^\u0000-\u007f])(?:(?:[a-z0-9_\-]|[^\u0000-\u007f])*(?:[a-z0-9]|[^\u0000-\u007f]))?)/i;
    twttr.txt.regexen.validateUrlUnicodeDomainSegment = /(?:(?:[a-z0-9]|[^\u0000-\u007f])(?:(?:[a-z0-9\-]|[^\u0000-\u007f])*(?:[a-z0-9]|[^\u0000-\u007f]))?)/i;
    twttr.txt.regexen.validateUrlUnicodeDomainTld = /(?:(?:[a-z]|[^\u0000-\u007f])(?:(?:[a-z0-9\-]|[^\u0000-\u007f])*(?:[a-z0-9]|[^\u0000-\u007f]))?)/i;
    twttr.txt.regexen.validateUrlUnicodeDomain = g(/(?:(?:#{validateUrlUnicodeSubDomainSegment}\.)*(?:#{validateUrlUnicodeDomainSegment}\.)#{validateUrlUnicodeDomainTld})/i);
    twttr.txt.regexen.validateUrlUnicodeHost = g("(?:#{validateUrlIp}|#{validateUrlUnicodeDomain})", "i");
    twttr.txt.regexen.validateUrlPort = /[0-9]{1,5}/;
    twttr.txt.regexen.validateUrlUnicodeAuthority = g("(?:(#{validateUrlUserinfo})@)?(#{validateUrlUnicodeHost})(?::(#{validateUrlPort}))?", "i");
    twttr.txt.regexen.validateUrlAuthority = g("(?:(#{validateUrlUserinfo})@)?(#{validateUrlHost})(?::(#{validateUrlPort}))?", "i");
    twttr.txt.regexen.validateUrlPath = g(/(\/#{validateUrlPchar}*)*/i);
    twttr.txt.regexen.validateUrlQuery = g(/(#{validateUrlPchar}|\/|\?)*/i);
    twttr.txt.regexen.validateUrlFragment = g(/(#{validateUrlPchar}|\/|\?)*/i);
    twttr.txt.regexen.validateUrlUnencoded = g("^(?:([^:/?#]+):\\/\\/)?([^/?#]*)([^?#]*)(?:\\?([^#]*))?(?:#(.*))?$", "i");
    var s = "tweet-url list-slug";
    var i = "tweet-url username";
    var o = "tweet-url hashtag";
    var f = "tweet-url cashtag";
    var e = {
        urlClass: true,
        listClass: true,
        usernameClass: true,
        hashtagClass: true,
        cashtagClass: true,
        usernameUrlBase: true,
        listUrlBase: true,
        hashtagUrlBase: true,
        cashtagUrlBase: true,
        usernameUrlBlock: true,
        listUrlBlock: true,
        hashtagUrlBlock: true,
        linkUrlBlock: true,
        usernameIncludeSymbol: true,
        suppressLists: true,
        suppressNoFollow: true,
        suppressDataScreenName: true,
        urlEntities: true,
        symbolTag: true,
        textWithSymbolTag: true,
        urlTarget: true,
        invisibleTagAttrs: true,
        linkAttributeBlock: true,
        linkTextBlock: true
    };
    var r = {disabled: true, readonly: true, multiple: true, checked: true};

    function p(w) {
        var v = {};
        for (var u in w) {
            if (w.hasOwnProperty(u)) {
                v[u] = w[u]
            }
        }
        return v
    }

    twttr.txt.tagAttrs = function (x) {
        var y = "";
        for (var w in x) {
            var u = x[w];
            if (r[w]) {
                u = u ? w : null
            }
            if (u == null) {
                continue
            }
            y += " " + twttr.txt.htmlEscape(w) + '="' + twttr.txt.htmlEscape(u.toString()) + '"'
        }
        return y
    };
    twttr.txt.linkToText = function (v, y, u, w) {
        if (!w.suppressNoFollow) {
            u.rel = "nofollow"
        }
        if (w.linkAttributeBlock) {
            w.linkAttributeBlock(v, u)
        }
        if (w.linkTextBlock) {
            y = w.linkTextBlock(v, y)
        }
        var x = {text: y, attr: twttr.txt.tagAttrs(u)};
        return j("<a#{attr}>#{text}</a>", x)
    };
    twttr.txt.linkToTextWithSymbol = function (v, y, A, u, w) {
        var z = w.symbolTag ? "<" + w.symbolTag + ">" + y + "</" + w.symbolTag + ">" : y;
        A = twttr.txt.htmlEscape(A);
        var x = w.textWithSymbolTag ? "<" + w.textWithSymbolTag + ">" + A + "</" + w.textWithSymbolTag + ">" : A;
        if (w.usernameIncludeSymbol || !y.match(twttr.txt.regexen.atSigns)) {
            return twttr.txt.linkToText(v, z + x, u, w)
        } else {
            return z + twttr.txt.linkToText(v, x, u, w)
        }
    };
    twttr.txt.linkToHashtag = function (u, z, w) {
        var y = z.substring(u.indices[0], u.indices[0] + 1);
        var x = twttr.txt.htmlEscape(u.hashtag);
        var v = p(w.htmlAttrs || {});
        v.href = w.hashtagUrlBase + x;
        v.title = "#" + x;
        v["class"] = w.hashtagClass;
        return twttr.txt.linkToTextWithSymbol(u, y, x, v, w)
    };
    twttr.txt.linkToCashtag = function (v, y, x) {
        var u = twttr.txt.htmlEscape(v.cashtag);
        var w = p(x.htmlAttrs || {});
        w.href = x.cashtagUrlBase + u;
        w.title = "$" + u;
        w["class"] = x.cashtagClass;
        return twttr.txt.linkToTextWithSymbol(v, "$", u, w, x)
    };
    twttr.txt.linkToMentionAndList = function (w, A, y) {
        var u = A.substring(w.indices[0], w.indices[0] + 1);
        var v = twttr.txt.htmlEscape(w.screenName);
        var B = twttr.txt.htmlEscape(w.listSlug);
        var z = w.listSlug && !y.suppressLists;
        var x = p(y.htmlAttrs || {});
        x["class"] = (z ? y.listClass : y.usernameClass);
        x.href = z ? y.listUrlBase + v + B : y.usernameUrlBase + v;
        if (!z && !y.suppressDataScreenName) {
            x["data-screen-name"] = v
        }
        return twttr.txt.linkToTextWithSymbol(w, u, z ? v + B : v, x, y)
    };
    twttr.txt.linkToUrl = function (u, A, x) {
        var w = u.url;
        var B = w;
        var y = twttr.txt.htmlEscape(B);
        var z = (x.urlEntities && x.urlEntities[w]) || u;
        if (z.display_url) {
            y = twttr.txt.linkTextWithEntity(z, x)
        }
        var v = p(x.htmlAttrs || {});
        v.href = w;
        if (x.urlClass) {
            v["class"] = x.urlClass
        }
        if (x.urlTarget) {
            v.target = x.urlTarget
        }
        if (!x.title && z.display_url) {
            v.title = z.expanded_url
        }
        return twttr.txt.linkToText(u, y, v, x)
    };
    twttr.txt.linkTextWithEntity = function (y, z) {
        var C = y.display_url;
        var x = y.expanded_url;
        var A = C.replace(/…/g, "");
        if (x.indexOf(A) != -1) {
            var B = x.indexOf(A);
            var w = {
                displayUrlSansEllipses: A,
                beforeDisplayUrl: x.substr(0, B),
                afterDisplayUrl: x.substr(B + A.length),
                precedingEllipsis: C.match(/^…/) ? "…" : "",
                followingEllipsis: C.match(/…$/) ? "…" : ""
            };
            for (var u in w) {
                if (w.hasOwnProperty(u)) {
                    w[u] = twttr.txt.htmlEscape(w[u])
                }
            }
            w.invisible = z.invisibleTagAttrs;
            return j("<span class='tco-ellipsis'>#{precedingEllipsis}<span #{invisible}>&nbsp;</span></span><span #{invisible}>#{beforeDisplayUrl}</span><span class='js-display-url'>#{displayUrlSansEllipses}</span><span #{invisible}>#{afterDisplayUrl}</span><span class='tco-ellipsis'><span #{invisible}>&nbsp;</span>#{followingEllipsis}</span>", w)
        }
        return C
    };
    twttr.txt.autoLinkEntities = function (A, y, B) {
        B = p(B || {});
        B.hashtagClass = B.hashtagClass || o;
        B.hashtagUrlBase = B.hashtagUrlBase || "https://twitter.com/#!/search?q=%23";
        B.cashtagClass = B.cashtagClass || f;
        B.cashtagUrlBase = B.cashtagUrlBase || "https://twitter.com/#!/search?q=%24";
        B.listClass = B.listClass || s;
        B.usernameClass = B.usernameClass || i;
        B.usernameUrlBase = B.usernameUrlBase || "https://twitter.com/";
        B.listUrlBase = B.listUrlBase || "https://twitter.com/";
        B.htmlAttrs = twttr.txt.extractHtmlAttrsFromOptions(B);
        B.invisibleTagAttrs = B.invisibleTagAttrs || "style='position:absolute;left:-9999px;'";
        var u, w, z;
        if (B.urlEntities) {
            u = {};
            for (w = 0, z = B.urlEntities.length; w < z; w++) {
                u[B.urlEntities[w].url] = B.urlEntities[w]
            }
            B.urlEntities = u
        }
        var C = "";
        var x = 0;
        y.sort(function (E, D) {
            return E.indices[0] - D.indices[0]
        });
        for (var w = 0; w < y.length; w++) {
            var v = y[w];
            C += A.substring(x, v.indices[0]);
            if (v.url) {
                C += twttr.txt.linkToUrl(v, A, B)
            } else {
                if (v.hashtag) {
                    C += twttr.txt.linkToHashtag(v, A, B)
                } else {
                    if (v.screenName) {
                        C += twttr.txt.linkToMentionAndList(v, A, B)
                    } else {
                        if (v.cashtag) {
                            C += twttr.txt.linkToCashtag(v, A, B)
                        }
                    }
                }
            }
            x = v.indices[1]
        }
        C += A.substring(x, A.length);
        return C
    };
    twttr.txt.autoLinkWithJSON = function (z, x, u) {
        var y = [];
        for (var w in x) {
            y = y.concat(x[w])
        }
        for (var v = 0; v < y.length; v++) {
            entity = y[v];
            if (entity.screen_name) {
                entity.screenName = entity.screen_name
            } else {
                if (entity.text) {
                    entity.hashtag = entity.text
                }
            }
        }
        twttr.txt.modifyIndicesFromUnicodeToUTF16(z, y);
        return twttr.txt.autoLinkEntities(z, y, u)
    };
    twttr.txt.extractHtmlAttrsFromOptions = function (x) {
        var y = {};
        for (var w in x) {
            var u = x[w];
            if (e[w]) {
                continue
            }
            if (r[w]) {
                u = u ? w : null
            }
            if (u == null) {
                continue
            }
            y[w] = u
        }
        return y
    };
    twttr.txt.autoLink = function (w, u) {
        var v = twttr.txt.extractEntitiesWithIndices(w, {extractUrlWithoutProtocol: false});
        return twttr.txt.autoLinkEntities(w, v, u)
    };
    twttr.txt.autoLinkUsernamesOrLists = function (w, u) {
        var v = twttr.txt.extractMentionsOrListsWithIndices(w);
        return twttr.txt.autoLinkEntities(w, v, u)
    };
    twttr.txt.autoLinkHashtags = function (w, u) {
        var v = twttr.txt.extractHashtagsWithIndices(w);
        return twttr.txt.autoLinkEntities(w, v, u)
    };
    twttr.txt.autoLinkCashtags = function (w, u) {
        var v = twttr.txt.extractCashtagsWithIndices(w);
        return twttr.txt.autoLinkEntities(w, v, u)
    };
    twttr.txt.autoLinkUrlsCustom = function (w, u) {
        var v = twttr.txt.extractUrlsWithIndices(w, {extractUrlWithoutProtocol: false});
        return twttr.txt.autoLinkEntities(w, v, u)
    };
    twttr.txt.removeOverlappingEntities = function (w) {
        w.sort(function (y, x) {
            return y.indices[0] - x.indices[0]
        });
        var v = w[0];
        for (var u = 1; u < w.length; u++) {
            if (v.indices[1] > w[u].indices[0]) {
                w.splice(u, 1);
                u--
            } else {
                v = w[u]
            }
        }
    };
    twttr.txt.extractEntitiesWithIndices = function (w, u) {
        var v = twttr.txt.extractUrlsWithIndices(w, u).concat(twttr.txt.extractMentionsOrListsWithIndices(w)).concat(twttr.txt.extractHashtagsWithIndices(w, {checkUrlOverlap: false})).concat(twttr.txt.extractCashtagsWithIndices(w));
        if (v.length == 0) {
            return []
        }
        twttr.txt.removeOverlappingEntities(v);
        return v
    };
    twttr.txt.extractMentions = function (x) {
        var y = [], u = twttr.txt.extractMentionsWithIndices(x);
        for (var w = 0; w < u.length; w++) {
            var v = u[w].screenName;
            y.push(v)
        }
        return y
    };
    twttr.txt.extractMentionsWithIndices = function (x) {
        var u = [];
        var w = twttr.txt.extractMentionsOrListsWithIndices(x);
        for (var v = 0; v < w.length; v++) {
            mentionOrList = w[v];
            if (mentionOrList.listSlug == "") {
                u.push({screenName: mentionOrList.screenName, indices: mentionOrList.indices})
            }
        }
        return u
    };
    twttr.txt.extractMentionsOrListsWithIndices = function (w) {
        if (!w || !w.match(twttr.txt.regexen.atSigns)) {
            return []
        }
        var v = [], u = 0;
        w.replace(twttr.txt.regexen.validMentionOrList, function (D, E, A, z, x, B, F) {
            var y = F.slice(B + D.length);
            if (!y.match(twttr.txt.regexen.endMentionMatch)) {
                x = x || "";
                var C = w.indexOf(A + z + x, u);
                u = C + z.length + x.length + 1;
                v.push({screenName: z, listSlug: x, indices: [C, u]})
            }
        });
        return v
    };
    twttr.txt.extractReplies = function (v) {
        if (!v) {
            return null
        }
        var u = v.match(twttr.txt.regexen.validReply);
        if (!u || RegExp.rightContext.match(twttr.txt.regexen.endMentionMatch)) {
            return null
        }
        return u[1]
    };
    twttr.txt.extractUrls = function (y, v) {
        var x = [], u = twttr.txt.extractUrlsWithIndices(y, v);
        for (var w = 0; w < u.length; w++) {
            x.push(u[w].url)
        }
        return x
    };
    twttr.txt.extractUrlsWithIndices = function (D, G) {
        if (!G) {
            G = {extractUrlsWithoutProtocol: true}
        }
        if (!D || (G.extractUrlsWithoutProtocol ? !D.match(/\./) : !D.match(/:/))) {
            return []
        }
        var B = [];
        while (twttr.txt.regexen.extractUrl.exec(D)) {
            var C = RegExp.$2, w = RegExp.$3, E = RegExp.$4, z = RegExp.$5, F = RegExp.$7;
            var x = twttr.txt.regexen.extractUrl.lastIndex, A = x - w.length;
            if (!E) {
                if (!G.extractUrlsWithoutProtocol || C.match(twttr.txt.regexen.invalidUrlWithoutProtocolPrecedingChars)) {
                    continue
                }
                var v = null, y = false, u = 0;
                z.replace(twttr.txt.regexen.validAsciiDomain, function (I) {
                    var H = z.indexOf(I, u);
                    u = H + I.length;
                    v = {url: I, indices: [A + H, A + u]};
                    y = I.match(twttr.txt.regexen.invalidShortDomain);
                    if (!y) {
                        B.push(v)
                    }
                });
                if (v == null) {
                    continue
                }
                if (F) {
                    if (y) {
                        B.push(v)
                    }
                    v.url = w.replace(z, v.url);
                    v.indices[1] = x
                }
            } else {
                if (w.match(twttr.txt.regexen.validTcoUrl)) {
                    w = RegExp.lastMatch;
                    x = A + w.length
                }
                B.push({url: w, indices: [A, x]})
            }
        }
        return B
    };
    twttr.txt.extractHashtags = function (x) {
        var w = [], v = twttr.txt.extractHashtagsWithIndices(x);
        for (var u = 0; u < v.length; u++) {
            w.push(v[u].hashtag)
        }
        return w
    };
    twttr.txt.extractHashtagsWithIndices = function (A, w) {
        if (!w) {
            w = {checkUrlOverlap: true}
        }
        if (!A || !A.match(twttr.txt.regexen.hashSigns)) {
            return []
        }
        var v = [], u = 0;
        A.replace(twttr.txt.regexen.validHashtag, function (C, F, G, E, I, B) {
            var H = B.slice(I + C.length);
            if (H.match(twttr.txt.regexen.endHashtagMatch)) {
                return
            }
            var D = A.indexOf(G + E, u);
            u = D + E.length + 1;
            v.push({hashtag: E, indices: [D, u]})
        });
        if (w.checkUrlOverlap) {
            var y = twttr.txt.extractUrlsWithIndices(A);
            if (y.length > 0) {
                var z = v.concat(y);
                twttr.txt.removeOverlappingEntities(z);
                v = [];
                for (var x = 0; x < z.length; x++) {
                    if (z[x].hashtag) {
                        v.push(z[x])
                    }
                }
            }
        }
        return v
    };
    twttr.txt.extractCashtags = function (x) {
        var u = [], v = twttr.txt.extractCashtagsWithIndices(x);
        for (var w = 0; w < v.length; w++) {
            u.push(v[w].cashtag)
        }
        return u
    };
    twttr.txt.extractCashtagsWithIndices = function (w) {
        if (!w || w.indexOf("$") == -1) {
            return []
        }
        var v = [], u = 0;
        w.replace(twttr.txt.regexen.validCashtag, function (z, x, B, y) {
            var A = w.indexOf(x, u) - 1;
            u = A + x.length + 1;
            v.push({cashtag: x, indices: [A, u]})
        });
        return v
    };
    twttr.txt.modifyIndicesFromUnicodeToUTF16 = function (v, u) {
        twttr.txt.convertUnicodeIndices(v, u, false)
    };
    twttr.txt.modifyIndicesFromUTF16ToUnicode = function (v, u) {
        twttr.txt.convertUnicodeIndices(v, u, true)
    };
    twttr.txt.convertUnicodeIndices = function (C, z, v) {
        if (z.length == 0) {
            return
        }
        var u = 0;
        var w = 0;
        z.sort(function (E, D) {
            return E.indices[0] - D.indices[0]
        });
        var y = 0;
        var x = z[0];
        while (u < C.length) {
            if (x.indices[0] == (v ? u : w)) {
                var A = x.indices[1] - x.indices[0];
                x.indices[0] = v ? w : u;
                x.indices[1] = x.indices[0] + A;
                y++;
                if (y == z.length) {
                    break
                }
                x = z[y]
            }
            var B = C.charCodeAt(u);
            if (55296 <= B && B <= 56319 && u < C.length - 1) {
                B = C.charCodeAt(u + 1);
                if (56320 <= B && B <= 57343) {
                    u++
                }
            }
            w++;
            u++
        }
    };
    twttr.txt.splitTags = function (A) {
        var u = A.split("<"), z, y = [], x;
        for (var w = 0; w < u.length; w += 1) {
            x = u[w];
            if (!x) {
                y.push("")
            } else {
                z = x.split(">");
                for (var v = 0; v < z.length; v += 1) {
                    y.push(z[v])
                }
            }
        }
        return y
    };
    twttr.txt.hitHighlight = function (F, H, x) {
        var D = "em";
        H = H || [];
        x = x || {};
        if (H.length === 0) {
            return F
        }
        var w = x.tag || D, G = ["<" + w + ">", "</" + w + ">"], E = twttr.txt.splitTags(F), K, J, A = "", u = 0, B = E[0], C = 0, v = 0, O = false, y = B, I = [], z, L, P, N, M;
        for (K = 0; K < H.length; K += 1) {
            for (J = 0; J < H[K].length; J += 1) {
                I.push(H[K][J])
            }
        }
        for (z = 0; z < I.length; z += 1) {
            L = I[z];
            P = G[z % 2];
            N = false;
            while (B != null && L >= C + B.length) {
                A += y.slice(v);
                if (O && L === C + y.length) {
                    A += P;
                    N = true
                }
                if (E[u + 1]) {
                    A += "<" + E[u + 1] + ">"
                }
                C += y.length;
                v = 0;
                u += 2;
                B = E[u];
                y = B;
                O = false
            }
            if (!N && B != null) {
                M = L - C;
                A += y.slice(v, M) + P;
                v = M;
                if (z % 2 === 0) {
                    O = true
                } else {
                    O = false
                }
            } else {
                if (!N) {
                    N = true;
                    A += P
                }
            }
        }
        if (B != null) {
            if (v < y.length) {
                A += y.slice(v)
            }
            for (z = u + 1; z < E.length; z += 1) {
                A += (z % 2 === 0 ? E[z] : "<" + E[z] + ">")
            }
        }
        return A
    };
    var n = 140;
    var k = [m(65534), m(65279), m(65535), m(8234), m(8235), m(8236), m(8237), m(8238)];
    twttr.txt.getTweetLength = function (y, v) {
        if (!v) {
            v = {short_url_length: 20, short_url_length_https: 21}
        }
        var x = y.length;
        var u = twttr.txt.extractUrlsWithIndices(y);
        for (var w = 0; w < u.length; w++) {
            x += u[w].indices[0] - u[w].indices[1];
            if (u[w].url.toLowerCase().match(/^https:\/\//)) {
                x += v.short_url_length_https
            } else {
                x += v.short_url_length
            }
        }
        return x
    };
    twttr.txt.isInvalidTweet = function (v) {
        if (!v) {
            return "empty"
        }
        if (twttr.txt.getTweetLength(v) > n) {
            return "too_long"
        }
        for (var u = 0; u < k.length; u++) {
            if (v.indexOf(k[u]) >= 0) {
                return "invalid_characters"
            }
        }
        return false
    };
    twttr.txt.isValidTweetText = function (u) {
        return !twttr.txt.isInvalidTweet(u)
    };
    twttr.txt.isValidUsername = function (v) {
        if (!v) {
            return false
        }
        var u = twttr.txt.extractMentions(v);
        return u.length === 1 && u[0] === v.slice(1)
    };
    var l = g(/^#{validMentionOrList}$/);
    twttr.txt.isValidList = function (v) {
        var u = v.match(l);
        return !!(u && u[1] == "" && u[4])
    };
    twttr.txt.isValidHashtag = function (v) {
        if (!v) {
            return false
        }
        var u = twttr.txt.extractHashtags(v);
        return u.length === 1 && u[0] === v.slice(1)
    };
    twttr.txt.isValidUrl = function (u, z, C) {
        if (z == null) {
            z = true
        }
        if (C == null) {
            C = true
        }
        if (!u) {
            return false
        }
        var v = u.match(twttr.txt.regexen.validateUrlUnencoded);
        if (!v || v[0] !== u) {
            return false
        }
        var w = v[1], x = v[2], B = v[3], A = v[4], y = v[5];
        if (!((!C || (t(w, twttr.txt.regexen.validateUrlScheme) && w.match(/^https?$/i))) && t(B, twttr.txt.regexen.validateUrlPath) && t(A, twttr.txt.regexen.validateUrlQuery, true) && t(y, twttr.txt.regexen.validateUrlFragment, true))) {
            return false
        }
        return (z && t(x, twttr.txt.regexen.validateUrlUnicodeAuthority)) || (!z && t(x, twttr.txt.regexen.validateUrlAuthority))
    };
    function t(v, w, u) {
        if (!u) {
            return ((typeof v === "string") && v.match(w) && RegExp["$&"] === v)
        }
        return (!v || (v.match(w) && RegExp["$&"] === v))
    }

    if (typeof module != "undefined" && module.exports) {
        module.exports = twttr.txt
    }
}());