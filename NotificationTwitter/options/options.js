$(document).ready(function () {
    setVersionTitle();
    accounts = store.get("accounts");
    if (!accounts) {
        accounts = [];
    }
    loadAccountsToScreen();
    setInterval(function () {
        chrome.extension.getBackgroundPage().logInConsole("Find new Accounts...", true);
        var listAccounts = store.get("accounts");
        if (listAccounts) {
            for (var accountJson in listAccounts) {
                if (!listAccounts[accountJson].image) {
                    listAccounts.splice(accountJson, 1);
                }
            }
            if (accounts.length != listAccounts.length) {
                chrome.extension.getBackgroundPage().logInConsole("New Account found!", true);
                accounts = listAccounts;
                loadAccountsToScreen();
            }
        }
    }, 3000);
    $("input").each(function () {
        if (this.id) {
            this.checked = store.get(this.id);
        }
    });
    $("input").on("change", function () {
        if (this.id) {
            store.set(this.id, this.checked);
            chrome.extension.getBackgroundPage().logInConsole(this.id + "checked = " + this.checked, false);
        } else {
            var index = getSelectedAccountToChange.call(this).id.substring(7);
            accounts = chrome.extension.getBackgroundPage().accounts;
            accounts[index][this.className] = this.checked;
            chrome.extension.getBackgroundPage().updateAccountsToStore();
            chrome.extension.getBackgroundPage().logInConsole(this.id + "checked = " + this.checked, false);
        }
    });
    $("button#connect").on("click", function () {
        try {
            chrome.extension.getBackgroundPage().twitter.requestToken();
        } catch (exception) {
            showError(exception);
        }
    });

    function getSelectedAccountToChange() {
        return this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
    }
});

function loadAccountsToScreen() {
    $("#accountList").html("");
    try {
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
            chrome.extension.getBackgroundPage().logInConsole("Add to screen Account: @" + accounts[a].screenName, true);
        }
    } catch (exception) {
        showError(exception);
    }
    function getSelectedAccountToAction() {
        return this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
    }

    if (accounts.length == 0) {
        $("#accountList").html('<div class="container jumbotron account"><span>No accounts connected</span></div>');
    } else {
        $("button.btn-confirm-logout").on("click", function () {
            if (typeof componentToLogout == "undefined") {
                chrome.extension.getBackgroundPage().logInConsole("Account not found!", false);
                return;
            }
            try {
                var indexToLogout = componentToLogout.id.substr(7);
                accounts[indexToLogout].disabled = true;
                chrome.extension.getBackgroundPage().logInConsole("@" + accounts[indexToLogout].screenName + " removed", true);
                chrome.extension.getBackgroundPage().twitter.abortStream(accounts[indexToLogout]);
                accounts.splice(indexToLogout, 1);
                chrome.extension.getBackgroundPage().updateAccountsToStore();
                componentToLogout.parentNode.removeChild(componentToLogout);
                componentToLogout == undefined;
                loadAccountsToScreen();
                showSuccess("Account logout with success!")
            } catch (exception) {
                chrome.extension.getBackgroundPage().logInConsole(exception, false);
                showError(exception);
            }
            accounts = chrome.extension.getBackgroundPage().accounts;
        });
        $("button.btn-disable").on("click", function () {
            try {
                var component = getSelectedAccountToAction.call(this);
                var index = component.id.substr(7);
                accounts = chrome.extension.getBackgroundPage().accounts;
                if (accounts[index].disabled) {
                    enableAccount(this, component, index);
                } else {
                    disableAccount(this, component, index);
                }
                chrome.extension.getBackgroundPage().updateAccountsToStore();
            } catch (exception) {
                showError(exception);
            }
        });
        $("button.btn-logout").on("click", function () {
            componentToLogout = getSelectedAccountToAction.call(this);
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
    showSuccess("Account disabled with success!")
}

function enableAccount(button, component, index) {
    accounts[index].disabled = false;
    accounts[index].stream.start(accounts[index]);
    button.innerText = "Disable";
    button.classList.remove("btn-success");
    button.classList.add("btn-warning");
    component.style.opacity = 1;
    chrome.extension.getBackgroundPage().logInConsole(accounts[index].screenName + " enabled", true);
    showSuccess("Account enabled with success!")
}

function setVersionTitle() {
    $(".navbar-brand").append(" " + chrome.extension.getBackgroundPage().twitter.version);
}

function showError(message) {
    $.notify({
        icon: '/images/error_48.png',
        title: '<strong>Error!</strong>',
        message: message
    }, {
        icon_type: 'image',
        delay: 3000,
        type: 'danger',
        placement: {
            align: 'center'
        },
        animate: {
            enter: "animated fadeInUp",
            exit: "animated fadeOutDown"
        }
    });
}

function showSuccess(message) {
    $.notify({
        icon: '/images/48.png',
        title: '<strong>Sucess!</strong>',
        message: message
    }, {
        icon_type: 'image',
        delay: 2000,
        type: 'success',
        placement: {
            align: 'center'
        },
        animate: {
            enter: "animated fadeInUp",
            exit: "animated fadeOutDown"
        }
    });
}