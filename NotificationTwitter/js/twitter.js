function NotFoundError(message) {
    this.name = "NotFoundError";
    this.message = (message || "");
}
NotFoundError.prototype = Error.prototype;

var twitter = {};
var accounts = store.get("accounts") || [];
twitter.version = "1.0";
twitter.apiRoot = "https://api.twitter.com/1.1/";
twitter.consumerKey = "nCcyyapPaxA1zxHYZoKElUYHt";
twitter.consumerSecret = "R3g4yCiM3AMv1w1pLgZdnsGLHqxAjPNKTWjSEShEwlIqxxZMqB";
twitter.setup = function () {
    for (var a in accounts) {
        if (!accounts[a].accessToken) {
            logInConsole("Account without accessToken! Splice, updateStore and reload.", true);
            accounts.splice(a, 1);
            twitter.updateAccountsToStore();
            document.location.reload();
        } else {
            twitter.loadAccount(accounts[a])
        }
    }
};
twitter.oauthRequest = function (d) {
    accessor = {consumerSecret: twitter.consumerSecret};
    message = {
        action: d.url,
        method: d.method || "GET",
        parameters: [["oauth_consumer_key", twitter.consumerKey], ["oauth_signature_method", "HMAC-SHA1"], ["oauth_version", "1.0"]]
    };
    d.token = d.token || d.account.accessToken;
    d.tokenSecret = d.tokenSecret || d.account.accessTokenSecret;
    if (d.token !== true) {
        message.parameters.push(["oauth_token", d.token])
    }
    if (d.tokenSecret !== true) {
        accessor.tokenSecret = d.tokenSecret
    }
    for (var a in d.parameters) {
        if (d.parameters[a][0] == "oauth_callback") {
            d.parameters[a][1] = unescape(d.parameters[a][1])
        }
        message.parameters.push(d.parameters[a])
    }
    OAuth.setTimestampAndNonce(message);
    OAuth.SignatureMethod.sign(message, accessor);
    if (d.stream) {
        logInConsole("Opening user stream for user " + d.account.screenName, true);
        $.ajax({
            url: message.action,
            type: message.method,
            data: OAuth.getParameterMap(message.parameters),
            dataType: d.type || "json",
            xhr: function () {
                d.account.stream.connection = new XMLHttpRequest();
                d.account.stream.connection.addEventListener("progress", function () {
                    twitter.processResponse(d.account);
                }, false);
                return d.account.stream.connection;
            },
            complete: function (f, g) {
                twitter.streamComplete(f.status, d.account);
            }
        });
        twitter.intervalTrigger(d);
    } else {
        logInConsole("Send request authorization...");
        logInConsole(d, true);
        var e = true;
        if (d.media) {
            e = false;
            var c = new FormData();
            c.append("media[]", d.media);
            c.append("status", d.status);
            if (d.replyID) {
                c.append("in_reply_to_status_id", d.replyID)
            }
            var b = function (f) {
                f.setRequestHeader("Authorization", OAuth.getAuthorizationHeader("", message.parameters))
            }
        }
        $.ajax({
            url: message.action,
            type: message.method,
            data: c || OAuth.getParameterMap(message.parameters),
            dataType: d.type || "json",
            contentType: (d.media) ? false : "application/x-www-form-urlencoded",
            processData: e,
            beforeSend: b || d.beforeSend || function () {
            },
            xhr: d.xhr,
            success: function (g, f, h) {
                if (d.success) {
                    d.success(g);
                }
            },
            error: function (g, f, h) {
                if (d.error) {
                    d.error(g.responseText, d.parameters, g);
                }
            }
        })
    }
};
twitter.processResponse = function(f) {
    logInConsole("Process response...", true);
    logInConsole(f, true);
    f.stream.response = f.stream.connection.responseText.split("\r");
    while (f.stream.response[f.stream.index + 1]) {
        if (f.stream.response[f.stream.index].length > 1) {
            var e;
            if (e = jQuery.parseJSON(f.stream.response[f.stream.index])) {
                if (e.text) {
                    f.stream.timeline.unshift(e);
                    if (e.entities.user_mentions) {
                        var b = false;
                        for (var c in e.entities.user_mentions) {
                            if (e.entities.user_mentions[c].screen_name == f.screenName) {
                                b = true;
                                f.stream.mentions.unshift(e);
                                var d = false;
                                var c = 0;
                                while (f.stream.friends[c] && !d) {
                                    if (f.stream.friends[c] == e.user.id) {
                                        d = true
                                    }
                                    c++
                                }
                                if (!d) {
                                    f.stream.timeline.splice(0, 1)
                                }
                            }
                        }
                    }
                    if (e.user.id == f.id) {
                        f.stream.userTimeline.unshift(e)
                    }
                    if (f.notifyTweet || (f.notifyMention && b)) {
                        twitter.notify(f, f.stream.index)
                    }
                    while (f.stream.timeline.length > 100) {
                        f.stream.timeline.pop()
                    }
                    while (f.stream.mentions.length > 100) {
                        f.stream.mentions.pop()
                    }
                    while (f.stream.userTimeline.length > 100) {
                        f.stream.mentions.pop()
                    }
                    var a = chrome.extension.getViews({});
                    for (var c in a) {
                        if (a[c].updateTimeline) {
                            a[c].updateTimeline("home", f);
                            if (b) {
                                a[c].updateTimeline("mentions", f)
                            }
                            if (e.user.id == f.id) {
                                a[c].updateTimeline("userTimeline", f)
                            }
                        }
                    }
                } else {
                    if (e.direct_message) {
                        if (f.notifyDm) {
                            twitter.notify(f, f.stream.index)
                        }
                        var a = chrome.extension.getViews({});
                        for (var c in a) {
                            if (a[c].updateMessage) {
                                a[c].updateMessage(f.id, e.direct_message)
                            }
                        }
                    } else {
                        if (e["delete"]) {
                            var c = 0;
                            if (f.stream.timeline[c]) {
                                while (f.stream.timeline[c].id_str > e["delete"].status.id_str || f.stream.timeline[c].retweeted_by && f.stream.timeline[c].id_str != e["delete"].status.id_str) {
                                    c++;
                                    if (!f.stream.timeline[c]) {
                                        break
                                    }
                                }
                            }
                            if (f.stream.timeline[c]) {
                                if (f.stream.timeline[c].id_str == e["delete"].status.id_str) {
                                    f.stream.timeline.splice(c, 1)
                                }
                            }
                            var a = chrome.extension.getViews({});
                            for (var c in a) {
                                if (a[c].deleteStatus) {
                                    a[c].deleteStatus(f.id, e["delete"].status.id_str)
                                }
                            }
                        } else {
                            if (e.friends) {
                                f.stream.friends = e.friends
                            }
                        }
                    }
                }
            } else {
                f.stream.connection.abort();
                logInConsole("Abort Stream by: " + e, true);
            }
        }
        f.stream.index++
    }
}
twitter.checkStream = function(account) {
    logInConsole("Checking stream activity...", true);
    if (account.stream.connection) {
        if (account.stream.connection.responseText.length > 5000000 || account.stream.lastIndex == account.stream.index) {
            account.stream.connection.abort();
            logInConsole("Abort Stream by checkStream(){ responseText: " + account.stream.connection.responseText.length
                + ", lastIndex: " + ", index: " + account.stream.index + " }", true);
        }
        account.stream.lastIndex = account.stream.index;
    } else {
        var msg = "Connection not found!";
        logInConsole(msg);
        throw new NotFoundError(msg);
    }
}
twitter.intervalTrigger = function(d) {
    idTriggerCheckInterval = setInterval(function () {
        twitter.checkStream(d.account);
    }, 45000);
    logInConsole("Create trigger check stream: id = " + idTriggerCheckInterval);
}
twitter.updateAccountsToStore = function() {
    logInConsole("updateAccountsToStore", true);
    var a = [];
    for (var b in accounts) {
        a[b] = jQuery.extend({}, accounts[b]);
        delete a[b].stream;
    }
    if (!a.length) {
        store.set("accounts", "");
    } else {
        store.set("accounts", a);
    }
}
twitter.clearTriggerCheckStream = function() {
    if (idTriggerCheckInterval) {
        logInConsole("Clear trigger check stream: id = " + idTriggerCheckInterval);
        clearInterval(idTriggerCheckInterval);
    }
}
twitter.stream = function (a) {
    this.response = "";
    this.timeline = [];
    this.mentions = [];
    this.userTimeline = [];
    this.connection = "";
    this.index = 0;
    this.lastIndex = 0;
    this.wait = a || 0;
    this.start = function (b) {
        logInConsole("Fetching timelines...", true);
        twitter.getTimeline({
            accountId: b.id, type: "home", success: function (c) {
                b.stream.timeline = c;
                logInConsole("Fetched home", true);
            }
        });
        twitter.getUserTimeline({
            accountId: b.id, success: function (c) {
                b.stream.userTimeline = c;
                logInConsole("Fetched user", true);
            }
        });
        twitter.getMentions({
            accountId: b.id, success: function (c) {
                b.stream.mentions = c;
                logInConsole("Fetched mentions", true);
            }
        });
        twitter.getMessages({
            accountId: b.id, success: function (c) {
                b.stream.messages = c;
                logInConsole("Fetched messages", true);
            }
        });
        twitter.oauthRequest({url: "https://userstream.twitter.com/1.1/user.json", stream: true, account: b})
    }
};
twitter.startStream = function(index) {
    accounts[index].stream.start(accounts[index]);
}
twitter.streamComplete = function(a, b) {
    logInConsole("Stream complete...", true);
    logInConsole(a, true);
    logInConsole(b, true);
    delete b.stream.connection;//why???????
    if (!b.disabled) {
        if (a > 200) {
            if (b.stream.wait < 240000) {
                setTimeout(function () {
                    b.stream = new twitter.stream(b.stream.wait);
                    b.stream.start(b)
                }, b.stream.wait);
                if (b.stream.wait == 0) {
                    b.stream.wait = 5000
                }
                b.stream.wait *= 2
            } else {
                notif = new Notification("Twitter Error", {
                    icon: "images/error",
                    body: "Twitter Notification couldn't connect to Twitter - maybe Twitter's down, or maybe you aren't logged in properly. Go to the options page to logout and in again."
                });
                setTimeout(function () {
                    b.stream = new twitter.stream(b.stream.wait);
                    b.stream.start(b)
                }, 10000)
            }
        } else {
            if (b.stream.wait < 30000) {
                setTimeout(function () {
                    b.stream = new twitter.stream(b.stream.wait);
                    b.stream.start(b)
                }, b.stream.wait);
                b.stream.wait += 10000
            } else {
                twitter.connectionError()
            }
        }
        b.waitReset = setTimeout(function () {
            b.wait = 0
        }, 320000)
    }
}
twitter.connectionError = function() {
    var a = new Notification("Connection Error", {
        icon: "images/error",
        body: "Twitter Notification couldn't connect to Twitter - are you sure you're connected to the internet? Connection will be tried again in 1 minute."
    });
    setTimeout(function () {
        if (a) {
            a.close();
        }
        document.location.reload();
    }, 60000)
}
twitter.abortStream = function (index) {
    logInConsole("Abort Stream", true)
    if (accounts[index].stream && accounts[index].stream.connection) {
        accounts[index].stream.connection.abort();
    } else {
        var msg = "Stream not yet open. Try again.";
        logInConsole(msg);
        twitter.clearTriggerCheckStream();
        throw new NotFoundError(msg);
    }
    twitter.clearTriggerCheckStream();
}

twitter.requestToken = function () {
    var a = Date.now();
    if (accounts.length > 0) {
        var tagUpdate = "tag_wait";
        new Notification("Wait!", {
            icon: "images/update.png",
            body: "Please wait, in the next version will be possible to add more than one account!",
            tag: tagUpdate
        });
        return;
    }
    twitter.oauthRequest({
        url: "https://api.twitter.com/oauth/request_token",
        type: "text",
        method: "POST",
        token: true,
        tokenSecret: true,
        parameters: [["oauth_callback", escape(window.top.location + "?t=" + a)]],
        success: function (d) {
            d = d.split("&");
            var b = [];
            for (var c in d) {
                var e = d[c].split("=");
                b[e[0]] = e[1]
            }
            accounts.push({requestToken: b.oauth_token, requestTokenSecret: b.oauth_token_secret, timestamp: a});
            twitter.updateAccountsToStore();
            if (b.oauth_token) {
                chrome.tabs.create({url: "https://api.twitter.com/oauth/authorize?oauth_token=" + b.oauth_token})
            } else {
                setTimeout(function () {
                    new Notification("An error occurred!", {
                        icon: "images/error.png",
                        body: "Please check you have internet access and your clock is set correctly and try again."
                    });
                }, 5000);
            }
        },
        error: function (d, c) {
            new Notification("Failed to authenticate with Twitter", {
                icon: "images/error.png",
                body: "Check you have internet access and your computer's clock is set correctly (common cause of error) and try again."
            });
        }
    })
};
twitter.accessToken = function (a, e) {
    logInConsole("accessToken", true)
    accounts = chrome.extension.getBackgroundPage().accounts;
    for (var b in accounts) {
        if (accounts[b].timestamp == e) {
            var d = accounts[b];
            var c = b
        }
    }
    twitter.oauthRequest({
        url: "https://api.twitter.com/oauth/access_token",
        type: "text",
        method: "POST",
        token: d.requestToken,
        tokenSecret: d.requestTokenSecret,
        parameters: [["oauth_verifier", a]],
        success: function (h) {
            h = h.split("&");
            var f = [];
            for (var g in h) {
                var j = h[g].split("=");
                f[j[0]] = j[1]
            }
            for (var g in accounts) {
                if (accounts[g].accessToken == f.oauth_token) {
                    accounts.splice(c, 1);
                    c = false
                }
            }
            if (c) {
                delete d.timestamp;
                delete d.requestToken;
                delete d.requestTokenSecret;
                d.accessToken = f.oauth_token;
                d.accessTokenSecret = f.oauth_token_secret;
                d.notifyDm = true;
                d.notifyTweet = true;
                d.notifyMention = true;
                chrome.extension.getBackgroundPage().twitter.loadNewAccount(d);
                new Notification("Authorised!", {
                    icon: "images/128.png",
                    body: "Twitter Notification is now authorised and running."
                });
            } else {
                new Notification("Not Authorised", {
                    icon: "images/error.png",
                    body: "That account has already been connected to Twitter Notification."
                });
            }
            twitter.updateAccountsToStore();
            chrome.tabs.getCurrent(function (i) {
                chrome.tabs.remove(i.id)
            })
        }
    })
};
twitter.loadAccount = function (a) {
    logInConsole("Load Account...", true);
    twitter.oauthRequest({
        url: twitter.apiRoot + "account/verify_credentials.json", account: a, success: function (c) {
            try {
                a.id = c.id;
                a.name = c.name;
                a.screenName = c.screen_name;
                a.image = c.profile_image_url;
                a.stream = new twitter.stream();
                if (a.disabled === undefined) {
                    a.disabled = false;
                }
                logInConsole("Updating accounts store", true);
                logInConsole("Account @" + c.screen_name + " load", true);
                twitter.updateAccountsToStore();
                if (!a.disabled) {
                    a.stream.start(a)
                }
            } catch (b) {
                twitter.connectionError()
            }
        }, error: function (c, b) {
            new Notification("Could not authenticate user @" + a.screenName, {
                icon: "images/error.png",
                body: "Twitter Notification failed to authorise this account with Twitter. Try logging out and logging in of this account on the accounts page."
            });
        }
    })
};
twitter.loadNewAccount = function (b) {
    for (var a in accounts) {
        if (accounts[a].accessToken == b.accessToken) {
            twitter.loadAccount(accounts[a])
        }
    }
};
twitter.getAccountById = function (b) {
    for (var a in accounts) {
        if (accounts[a].id == b) {
            return accounts[a]
        }
    }
};
twitter.favourite = function (d, c, b) {
    var a = twitter.getAccountById(c);
    twitter.oauthRequest({
        method: "POST",
        url: twitter.apiRoot + "favorites/create.json",
        account: a,
        parameters: [["id", d]],
        success: b || function () {
        },
        error: function (e) {
            e = jQuery.parseJSON(e);
            new Notification("Could not favourite", {icon: "images/error.png", body: "Error: " + e.errors.message})
        }
    })
};
twitter.unfavourite = function (d, c, b) {
    var a = twitter.getAccountById(c);
    twitter.oauthRequest({
        method: "POST",
        url: twitter.apiRoot + "favorites/destroy.json",
        parameters: [["id", d]],
        account: a,
        success: b || function () {
        },
        error: function (e) {
            e = jQuery.parseJSON(e);
            new Notification("Could not unfavourite", {icon: "images/error.png", body: "Error: " + e.errors.message})
        }
    })
};
twitter.retweet = function (d, c, b) {
    var a = twitter.getAccountById(c);
    twitter.oauthRequest({
        method: "POST",
        url: twitter.apiRoot + "statuses/retweet/" + d + ".json",
        account: a,
        success: b || function () {
        },
        error: function (e) {
            e = jQuery.parseJSON(e);
            new Notification("Retweet failed", {icon: "images/error.png", body: "Error: " + e.errors.message})
        }
    })
};
twitter.deleteStatus = function (d, c, b) {
    var a = twitter.getAccountById(c);
    twitter.oauthRequest({
        method: "POST",
        url: twitter.apiRoot + "statuses/destroy/" + d + ".json",
        account: a,
        success: b || function () {
        },
        error: function (e) {
            e = jQuery.parseJSON(e);
            new Notification("Retweet failed", {icon: "images/error.png", body: "Error: " + e.errors.message})
        }
    })
};
twitter.update = function (f, c, e, d) {
    var b = twitter.getAccountById(f);
    var a = [["status", c]];
    if (e) {
        a.push(["in_reply_to_status_id", e])
    }
    twitter.oauthRequest({
        method: "POST",
        url: twitter.apiRoot + "statuses/update.json",
        parameters: a,
        account: b,
        success: d || function () {
        },
        beforeSend: function (h, g) {
            g.data = g.data.replace(/\-/g, "%2D").replace(/\_/g, "%5F").replace(/\./g, "%2E").replace(/\!/g, "%21").replace(/\*/g, "%2A").replace(/\'/g, "%27").replace(/\(/g, "%28").replace(/\)/g, "%29")
        },
        error: function (g, j) {
            g = jQuery.parseJSON(g);
            logInConsole(g, false);
            new Notification("Status could not be sent", {
                icon: "images/error.png",
                body: "Error: " + g.errors.message
            });
            for (var h in j) {
                if (j[h][0] == "status") {
                    j = j[h][1]
                }
            }
            chrome.tabs.create({url: "http://twitter.com/?status=" + j})
        }
    })
};
twitter.updateWithMedia = function (h, e, d, g, f, a) {
    var c = twitter.getAccountById(h);
    var b = [];
    twitter.oauthRequest({
        method: "POST",
        url: "https://upload.twitter.com/1/statuses/update_with_media.json",
        parameters: b,
        account: c,
        media: d,
        status: e,
        replyID: g || false,
        xhr: function () {
            var i = new XMLHttpRequest();
            i.upload.addEventListener("progress", a || function () {
                }, false);
            return i
        },
        success: f || function () {
        },
        error: function (j, l) {
            j = jQuery.parseJSON(j);
            logInConsole(j, false);
            new Notification("Status could not be sent", {
                icon: "images/error.png",
                body: "Error: " + j.errors.message
            });
            for (var k in l) {
                if (l[k][0] == "status") {
                    l = l[k][1]
                }
            }
            chrome.tabs.create({url: "http://twitter.com/?status=" + l})
        }
    })
};
twitter.dm = function (e, b, d, c) {
    var a = twitter.getAccountById(e);
    twitter.oauthRequest({
        method: "POST",
        url: twitter.apiRoot + "direct_messages/new.json",
        parameters: [["text", b], ["user_id", d]],
        account: a,
        error: function (f) {
            new Notification("DM Could not be sent", {
                icon: "images/error.png",
                body: "Error: " + jQuery.parseJSON(f).errors.message
            })
        },
        success: c || function () {
        }
    })
};
twitter.getTimeline = function (c) {
    var b = twitter.getAccountById(c.accountId);
    var a = [["include_rts", "true"], ["include_entities", "true"], ["include_my_retweet", "true"]];
    if (c.parameters) {
        a = a.concat(c.parameters)
    }
    twitter.oauthRequest({
        method: "GET",
        url: twitter.apiRoot + "statuses/home_timeline.json",
        account: b,
        parameters: a,
        success: c.success || function () {
        },
        error: c.error || function () {
        }
    })
};
twitter.getUserTimeline = function (c) {
    var b = twitter.getAccountById(c.accountId);
    var a = [["include_rts", "true"], ["include_entities", "true"], ["include_my_retweet", "true"]];
    if (c.parameters) {
        a = a.concat(c.parameters)
    }
    twitter.oauthRequest({
        method: "GET",
        url: twitter.apiRoot + "statuses/user_timeline.json",
        account: b,
        parameters: a,
        success: c.success || function () {
        },
        error: c.error || function () {
        }
    })
};
twitter.getMentions = function (c) {
    var b = twitter.getAccountById(c.accountId);
    var a = [["include_rts", "true"], ["include_entities", "true"]];
    if (c.parameters) {
        a = a.concat(c.parameters)
    }
    twitter.oauthRequest({
        method: "GET",
        url: twitter.apiRoot + "statuses/mentions_timeline.json",
        account: b,
        parameters: a,
        success: c.success || function () {
        },
        error: c.error || function () {
        }
    })
};
twitter.getMessages = function (d) {
    var c = twitter.getAccountById(d.accountId);
    var b = [["skip_status", "true"]];
    if (d.parameters) {
        b = b.concat(d.parameters)
    }
    if (b.sent) {
        var a = twitter.apiRoot + "direct_messages/sent.json";
        delete b.sent
    }
    twitter.oauthRequest({
        method: "GET", url: twitter.apiRoot + "direct_messages.json", account: c, parameters: b, success: function (e) {
            twitter.oauthRequest({
                method: "GET",
                url: twitter.apiRoot + "direct_messages/sent.json",
                account: c,
                parameters: b,
                success: function (f) {
                    e = e.concat(f);
                    e.sort(function (h, g) {
                        return new Date(g.created_at) - new Date(h.created_at)
                    });
                    logInConsole(e, true);
                    d.success(e)
                },
                error: d.error || function () {
                }
            })
        }, error: d.error || function () {
        }
    })
};
twitter.getUsers = function (b) {
    var a = twitter.getAccountById(b.accountId);
    twitter.oauthRequest({
        method: "POST",
        url: twitter.apiRoot + "users/lookup.json",
        account: a,
        parameters: b.parameters,
        success: b.success || function () {
        },
        error: b.error || function () {
        }
    })
};
twitter.getFriendships = function (b) {
    var a = twitter.getAccountById(b.accountId);
    twitter.oauthRequest({
        method: "GET",
        url: twitter.apiRoot + "friendships/show.json",
        account: a,
        parameters: b.parameters,
        success: b.success || function () {
        },
        error: b.error || function () {
        }
    })
};
twitter.getStatus = function (b) {
    var a = twitter.getAccountById(b.accountId);
    b.parameters.push(["include_entities", "true"]);
    twitter.oauthRequest({
        method: "GET",
        url: twitter.apiRoot + "statuses/show.json",
        account: a,
        parameters: b.parameters,
        success: b.success || function () {
        },
        error: b.error || function () {
        }
    })
};
twitter.notify = function (d, b) {
    var e = {};
    var f = jQuery.parseJSON(d.stream.response[b]);
    var a;
    if (f.direct_message) {
        if (f.direct_message.sender_id != d.id) {
            logInConsole("Twitter: @" + f.direct_message.sender.screen_name + ", message: " + f.direct_message.text);
            var a = new Notification(f.direct_message.sender.name, {
                icon: f.direct_message.sender.profile_image_url_https,
                body: f.direct_message.text
            });
            a.onclick = function () {
                chrome.tabs.create({url: "http://twitter.com/messages"})
            };
        }
    } else {
        logInConsole("ACCOUNT: @" + d.screenName + " - Twitter: @" + f.user.screen_name + ", message: " + f.text);
        var a = new Notification(f.user.name, {
            icon: f.user.profile_image_url_https,
            body: f.text
        });
        a.onclick = function () {
            chrome.tabs.create({url: "https://twitter.com/" + f.user.screen_name + "/status/" + f.id_str})
        };
    }

    var c = store.get("timeout");
    if (c && a) {
        setTimeout(function () {
            a.close()
        }, c * 1000)
    }
};
twitter.decrementID = function (b) {
    var a = -1;
    while (b.substr(a, 1) == "0") {
        a--
    }
    return b.slice(0, a) + (b.substr(a) - 1)
};

$(document).ready(function () {
    var d = window.location.href.split("?");
    if (d[1]) {
        d = d[1].split("&") || null;
        for (var b in d) {
            d[b] = d[b].split("=");
            if (d[b][0] == "oauth_verifier") {
                var a = d[b][1]
            }
            if (d[b][0] == "t") {
                var c = d[b][1]
            }
        }
    }
    if (a) {
        twitter.accessToken(a, c)
    } else {
        if (!localStorage.getItem("firstRun")) {
            logInConsole("FirstRun", true);
            if (!localStorage.getItem("version")) {
                chrome.tabs.create({url: "html/options.html"});
                localStorage.setItem("timeout", 10);
                //localStorage.setItem("fontSize", 13);
                localStorage.setItem("version", twitter.version);
                localStorage.setItem("connectionError", true);
                localStorage.setItem("debug", true);
            } else {
                accounts = [{
                    accessToken: localStorage.getItem("accessToken"),
                    accessTokenSecret: localStorage.getItem("accessTokenSecret"),
                    id: parseInt(localStorage.getItem("id"), 10),
                    image: localStorage.getItem("image"),
                    name: localStorage.getItem("name"),
                    notifyDm: localStorage.getItem("notifyDm"),
                    notifyMention: localStorage.getItem("notifyMention"),
                    notifyTweet: localStorage.getItem("notifyTweet"),
                    screenName: localStorage.getItem("screenName")
                }];
                for (var b in accounts[0]) {
                    if (accounts[0][b] == "true") {
                        accounts[0][b] = true
                    }
                    if (accounts[0][b] == "false") {
                        accounts[0][b] = false
                    }
                }
                localStorage.removeItem("screenName");
                localStorage.removeItem("id");
                localStorage.removeItem("accessToken");
                localStorage.removeItem("accessTokenSecret");
                localStorage.removeItem("image");
                localStorage.removeItem("name");
                localStorage.removeItem("notifyDm");
                localStorage.removeItem("notifyTweet");
                localStorage.removeItem("notifyMention");
                twitter.updateAccountsToStore()
            }
            localStorage.setItem("firstRun", true)
        }
        if (localStorage.getItem("version") < twitter.version) {
            localStorage.setItem("version", twitter.version);
            new Notification("Twitter Notification Updated!", {
                icon: "images/update.png",
                body: "Twitter Notification has been updated to the latest and greatest version!"
            })
        }
        twitter.setup()
    }
});

logInConsole = function (message, isDebug) {
    if (message != null) {
        if (isDebug) {
            if (localStorage.debug == "true") {
                console.log(message);
            }
        } else {
            console.log(message);
        }
    }
}