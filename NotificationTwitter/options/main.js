$(document).ready(function () {
    accounts = store.get("accounts");
    if (!accounts) {
        accounts = []
    }
    setInterval(function () {
        var b = store.get("accounts");
        if (b) {
            for (var a in b) {
                if (!b[a].image) {
                    b.splice(a, 1)
                }
            }
            if (accounts.length != b.length) {
                accounts = b;
            }
        }
    }, 3000);
    $("button").live("click", function () {
        switch (this.id) {
            case"connect":
                chrome.extension.getBackgroundPage().twitter.requestToken()
                break;
            case"logout":
                if (confirm("Logout of this account?")) {
                    try {
                        chrome.extension.getBackgroundPage().accounts[0].stream.connection.abort()
                    } catch (d) {
                    }
                    chrome.extension.getBackgroundPage().updateAccounts();
                }

                //var c = this.parentNode.parentNode.parentNode.parentNode;
                //var a = c.id.substr(7);
                //accounts = chrome.extension.getBackgroundPage().accounts;
                //if (this.className == "logout") {
                //    if (confirm("Logout of this account?")) {
                //        try {
                //            accounts[a].disabled = true;
                //            accounts[a].stream.connection.abort()
                //        } catch (d) {
                //        }
                //        accounts.splice(a, 1);
                //        chrome.extension.getBackgroundPage().updateAccounts();
                //        c.parentNode.removeChild(c)
                //    }
                //}
                //if (this.className === "disable") {
                //    if (accounts[a].disabled) {
                //        accounts[a].disabled = false;
                //        accounts[a].stream.start(accounts[a]);
                //        this.innerText = "Disable";
                //        c.style.opacity = "1"
                //    } else {
                //        accounts[a].disabled = true;
                //        accounts[a].stream.connection.abort();
                //        this.innerText = "Enable";
                //        c.style.opacity = "0.2"
                //    }
                //    chrome.extension.getBackgroundPage().updateAccounts()
                //}
                //if (this.className == "default") {
                //    for (var b in accounts) {
                //        if (accounts[b]["default"]) {
                //            delete accounts[b]["default"]
                //        }
                //    }
                //    accounts[this.parentNode.parentNode.id.substr(7)]["default"] = true;
                //    $(".default").removeAttr("disabled");
                //    $(".default").text("Make default");
                //    this.innerText = "Default";
                //    this.disabled = "disabled";
                //    chrome.extension.getBackgroundPage().updateAccounts()
                //}
                break;
            default:
                console.log("default");
                break
        }
    });
});