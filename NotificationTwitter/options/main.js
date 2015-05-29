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
    $("button#connect").on("click", function () {
        chrome.extension.getBackgroundPage().twitter.requestToken();
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
    } else {
        $("button.btn-confirm-logout").on("click", function () {
            chrome.extension.getBackgroundPage().logInConsole("button.btn-confirm-logout", true);
            if (typeof componentToLogout == "undefined") {
                chrome.extension.getBackgroundPage().logInConsole("Account not found!", false);
                return;
            }
            accounts = chrome.extension.getBackgroundPage().accounts;
            try {
                var indexToLogout = componentToLogout.id.substr(7);
                accounts[indexToLogout].disabled = true;
                chrome.extension.getBackgroundPage().logInConsole(accounts[indexToLogout].screenName + " removed", true);
                chrome.extension.getBackgroundPage().twitter.abortStream(accounts[indexToLogout]);
            } catch (d) {
                chrome.extension.getBackgroundPage().logInConsole(d, false);
            }
            accounts.splice(indexToLogout, 1);
            chrome.extension.getBackgroundPage().updateAccounts();
            componentToLogout.parentNode.removeChild(componentToLogout);
            componentToLogout == undefined;
            loadAccounts();
        });
        $("button.btn-disable").on("click", function () {
            chrome.extension.getBackgroundPage().logInConsole("button.btn-disable", true);
            var component = this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
            var index = component.id.substr(7);
            accounts = chrome.extension.getBackgroundPage().accounts;
            if (accounts[index].disabled) {
                enableAccount(this,component,index);
            } else {
                disableAccount(this,component,index);
            }
            chrome.extension.getBackgroundPage().updateAccounts();
        });
        $("button.btn-logout").on("click", function () {
            chrome.extension.getBackgroundPage().logInConsole("button.btn-logout", true);
            componentToLogout = this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
        });
    }
}

function disableAccount(button, component, index) {
    accounts[index].disabled = true;
    chrome.extension.getBackgroundPage().twitter.abortStream(accounts[index]);
    component.style.opacity = 0.2;
    button.innerText = "Enable";
    button.classList.remove("btn-warning");
    button.classList.add("btn-success");
    chrome.extension.getBackgroundPage().logInConsole(accounts[index].screenName + " disabled", true);
}

function enableAccount(button, component, index) {
    accounts[index].disabled = false;
    accounts[index].stream.start(accounts[index]);
    button.innerText = "Disable";
    button.classList.remove("btn-success");
    button.classList.add("btn-warning");
    component.style.opacity = 1;
    chrome.extension.getBackgroundPage().logInConsole(accounts[index].screenName + " enabled", true);
}