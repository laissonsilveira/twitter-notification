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
        } else {
            var index = getSelectedAccountToChange.call(this).id.substring(7);
            accounts = chrome.extension.getBackgroundPage().accounts;
            accounts[index][this.className] = this.checked;
            chrome.extension.getBackgroundPage().updateAccountsToStore();
        }
        chrome.extension.getBackgroundPage().logInConsole(this.id + "checked = " + this.checked, true);
    });
    $("button#connect").on("click", function () {
        try {
            chrome.extension.getBackgroundPage().twitter.requestToken();
        } catch (exception) {
            showError(exception);
        }
    });
    $("li").on("click", function () {
        try {
            loadPage(this);
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
                if (accounts[a].disabled == true) {
                    disableAccount(document.getElementById("account" + a))
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
                    enableAccount(component);
                } else {
                    disableAccount(component);
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

function loadPage(page) {
    $("li.active").removeClass("active");
    page.classList.add("active");

    var pageID = $(page).find("a").attr("href");

    switch (pageID) {
        case "#add":
            $("#add").show();
            $("#releases").hide();
            $("#contact").hide();
            break;

        case "#releases":
            $("#add").hide();
            $("#releases").show();
            $("#contact").hide();
            break;

        case "#contact":
            $("#add").hide();
            $("#releases").hide();
            $("#contact").show();
            break;

        default:
            break;
    }

}

function disableAccount(component) {

    var index = component.id.substr(7);
    var buttonDisable = $(component).find(".btn-disable")[0];

    accounts[index].disabled = true;
    chrome.extension.getBackgroundPage().twitter.abortStream(accounts[index]);

    $("#" + component.id).find(" :input").attr("disabled", true);
    $("#" + component.id).find("img,label,h2,h5,b").css("opacity", "0.2");
    buttonDisable.disabled = false;
    buttonDisable.innerText = "Enable";
    buttonDisable.classList.remove("btn-warning");
    buttonDisable.classList.add("btn-success");

    chrome.extension.getBackgroundPage().logInConsole(accounts[index].screenName + " disabled", true);
    showSuccess("Account disabled with success!")
}

function enableAccount(component) {

    var index = component.id.substr(7);
    var buttonEnable = $(component).find(".btn-disable")[0];

    accounts[index].disabled = false;
    accounts[index].stream.start(accounts[index]);

    $("#" + component.id).find(" :input").attr("disabled", false);
    $("#" + component.id).find("img,label,h2,h5,b").css("opacity", "1");
    buttonEnable.innerText = "Disable";
    buttonEnable.classList.remove("btn-success");
    buttonEnable.classList.add("btn-warning");

    chrome.extension.getBackgroundPage().logInConsole(accounts[index].screenName + " enabled", true);
    showSuccess("Account enabled with success!")
}

function setVersionTitle() {
    $(".navbar-brand").append(" " + chrome.extension.getBackgroundPage().twitter.version);
}

function showWarning(message) {
    $.notify({
        icon: '/images/warning_32.png',
        title: '<strong>Warning:</strong>',
        message: message
    }, {
        icon_type: 'image',
        delay: 3000,
        type: 'warning',
        placement: {
            align: 'center'
        },
        animate: {
            enter: "animated fadeInUp",
            exit: "animated fadeOutDown"
        }
    });
}

function showError(message) {
    chrome.extension.getBackgroundPage().logInConsole(message, false);

    var msg = message;

    if (message.message) {
        msg = message.message;
        if (message.name == chrome.extension.getBackgroundPage().NotFoundError.name) {
            showWarning(msg);
            return;
        }
    }

    $.notify({
        icon: '/images/error_32.png',
        title: '<strong>Error:</strong>',
        message: msg
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
        icon: '/images/32.png',
        title: '<strong>Sucess:</strong>',
        message: message
    }, {
        icon_type: 'image',
        delay: 1000,
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