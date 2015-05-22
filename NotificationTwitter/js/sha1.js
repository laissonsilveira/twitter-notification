var hexcase = 0;
var b64pad = "";
var chrsz = 8;
function hex_sha1(b) {
    return binb2hex(core_sha1(str2binb(b), b.length * chrsz))
}
function b64_sha1(b) {
    return binb2b64(core_sha1(str2binb(b), b.length * chrsz))
}
function str_sha1(b) {
    return binb2str(core_sha1(str2binb(b), b.length * chrsz))
}
function hex_hmac_sha1(d, c) {
    return binb2hex(core_hmac_sha1(d, c))
}
function b64_hmac_sha1(d, c) {
    return binb2b64(core_hmac_sha1(d, c))
}
function str_hmac_sha1(d, c) {
    return binb2str(core_hmac_sha1(d, c))
}
function sha1_vm_test() {
    return hex_sha1("abc") == "a9993e364706816aba3e25717850c26c9cd0d89d"
}
function core_sha1(D, d) {
    D[d >> 5] |= 128 << (24 - d % 32);
    D[((d + 64 >> 9) << 4) + 15] = d;
    var C = Array(80);
    var E = 1732584193;
    var F = -271733879;
    var a = -1732584194;
    var b = 271733878;
    var c = -1009589776;
    for (var j = 0; j < D.length; j += 16) {
        var e = E;
        var i = F;
        var t = a;
        var w = b;
        var A = c;
        for (var x = 0; x < 80; x++) {
            if (x < 16) {
                C[x] = D[j + x]
            } else {
                C[x] = rol(C[x - 3] ^ C[x - 8] ^ C[x - 14] ^ C[x - 16], 1)
            }
            var B = safe_add(safe_add(rol(E, 5), sha1_ft(x, F, a, b)), safe_add(safe_add(c, C[x]), sha1_kt(x)));
            c = b;
            b = a;
            a = rol(F, 30);
            F = E;
            E = B
        }
        E = safe_add(E, e);
        F = safe_add(F, i);
        a = safe_add(a, t);
        b = safe_add(b, w);
        c = safe_add(c, A)
    }
    return Array(E, F, a, b, c)
}
function sha1_ft(h, b, c, d) {
    if (h < 20) {
        return (b & c) | ((~b) & d)
    }
    if (h < 40) {
        return b ^ c ^ d
    }
    if (h < 60) {
        return (b & c) | (b & d) | (c & d)
    }
    return b ^ c ^ d
}
function sha1_kt(b) {
    return (b < 20) ? 1518500249 : (b < 40) ? 1859775393 : (b < 60) ? -1894007588 : -899497514
}
function core_hmac_sha1(n, k) {
    var l = str2binb(n);
    if (l.length > 16) {
        l = core_sha1(l, n.length * chrsz)
    }
    var i = Array(16), m = Array(16);
    for (var h = 0; h < 16; h++) {
        i[h] = l[h] ^ 909522486;
        m[h] = l[h] ^ 1549556828
    }
    var j = core_sha1(i.concat(str2binb(k)), 512 + k.length * chrsz);
    return core_sha1(m.concat(j), 512 + 160)
}
function safe_add(f, g) {
    var h = (f & 65535) + (g & 65535);
    var e = (f >> 16) + (g >> 16) + (h >> 16);
    return (e << 16) | (h & 65535)
}
function rol(d, c) {
    return (d << c) | (d >>> (32 - c))
}
function str2binb(g) {
    var h = Array();
    var f = (1 << chrsz) - 1;
    for (var e = 0; e < g.length * chrsz; e += chrsz) {
        h[e >> 5] |= (g.charCodeAt(e / chrsz) & f) << (32 - chrsz - e % 32)
    }
    return h
}
function binb2str(h) {
    var g = "";
    var f = (1 << chrsz) - 1;
    for (var e = 0; e < h.length * 32; e += chrsz) {
        g += String.fromCharCode((h[e >> 5] >>> (32 - chrsz - e % 32)) & f)
    }
    return g
}
function binb2hex(h) {
    var e = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";
    var g = "";
    for (var f = 0; f < h.length * 4; f++) {
        g += e.charAt((h[f >> 2] >> ((3 - f % 4) * 8 + 4)) & 15) + e.charAt((h[f >> 2] >> ((3 - f % 4) * 8)) & 15)
    }
    return g
}
function binb2b64(k) {
    var l = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    var i = "";
    for (var g = 0; g < k.length * 4; g += 3) {
        var j = (((k[g >> 2] >> 8 * (3 - g % 4)) & 255) << 16) | (((k[g + 1 >> 2] >> 8 * (3 - (g + 1) % 4)) & 255) << 8) | ((k[g + 2 >> 2] >> 8 * (3 - (g + 2) % 4)) & 255);
        for (var h = 0; h < 4; h++) {
            if (g * 8 + h * 6 > k.length * 32) {
                i += b64pad
            } else {
                i += l.charAt((j >> 6 * (3 - h)) & 63)
            }
        }
    }
    return i
};