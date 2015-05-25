$(document).ready(function () {
    accounts = store.get("accounts");
    if (!accounts) {
        accounts = [];
    }
    loadAccounts();
    setInterval(function () {
        var listAccounts = store.get("accounts");
        if (listAccounts) {
            for (var accountJson in listAccounts) {
                if (!listAccounts[accountJson].image) {
                    listAccounts.splice(accountJson, 1);
                }
            }
            if (accounts.length != listAccounts.length) {
                accounts = listAccounts;
                loadAccounts();
            }
        }
    }, 3000);
    $("input").each(function () {
        if (this.id) {
            this.checked = store.get(this.id)
        }
    });
    $("button").on("click", function () {
        switch (this.id) {
            case "connect":
                chrome.extension.getBackgroundPage().twitter.requestToken();
                break;
            default:
                var c = this.parentNode.parentNode.parentNode.parentNode;
                var a = c.id.substr(7);
                accounts = chrome.extension.getBackgroundPage().accounts;
                if (this.class === "btn-confirm-logout") {
                    try {
                        accounts[a].disabled = true;
                        console.log(accounts[a].screenName + " removed");
                        chrome.extension.getBackgroundPage().twitter.abortStream(accounts[a]);
                    } catch (d) {
                        console.log(d);
                    }
                    accounts.splice(a, 1);
                    chrome.extension.getBackgroundPage().updateAccounts();
                    c.parentNode.removeChild(c);
                    loadAccounts();
                }
                if (this.class === "btn-disable") {
                    if (accounts[a].disabled) {
                        accounts[a].disabled = false;
                        accounts[a].stream.start(accounts[a]);
                        this.innerText = "Disable";
                        c.style.opacity = "1";
                        console.log(accounts[a].screenName + " enabled");
                    } else {
                        accounts[a].disabled = true;
                        chrome.extension.getBackgroundPage().twitter.abortStream(accounts[a]);
                        this.innerText = "Enable";
                        c.style.opacity = "0.2";
                        console.log(accounts[a].screenName + " disabled");
                    }
                    chrome.extension.getBackgroundPage().updateAccounts();
                }
                break;
        }
    });
    $("input").on("change", function () {
        if (this.id) {
            store.set(this.id, this.checked)
        } else {
            var a = this.parentNode.parentNode.parentNode.parentNode.parentNode.id.substr(7);
            accounts = chrome.extension.getBackgroundPage().accounts;
            accounts[a][this.className] = this.checked;
            chrome.extension.getBackgroundPage().updateAccounts()
        }
    });
});
function loadAccounts() {
    $("#accountList").html("");
    for (var a in accounts) {
        if (accounts[a].image) {
            $("#accountTemplate").clone().appendTo("#accountList").attr("id", "account" + a).show();
            $("#account" + a).find("img").attr("src", accounts[a].image.replace("normal", "reasonably_small"));
            $("#account" + a).find("h5").text("@" + accounts[a].screenName);
            $("#account" + a).find("h2").text(accounts[a].name);
            if (accounts[a].disabled) {
                $("#account" + a).find(".disable").text("Enable");
                document.getElementById("account" + a).style.opacity = "0.2";
            }
            $("#account" + a).find("input").each(function () {
                this.checked = accounts[a][this.className];
            });
        } else {
            accounts.splice(a, 1);
        }
    }
    if (accounts.length == 0) {
        $("#accountList").html('<div class="container jumbotron account"><span>No accounts connected</span></div>');
    }
}