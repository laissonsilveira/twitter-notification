var Releases = {};
Releases.loadReleases = function () {
    function load(json) {
        var html = "";
        $.each(json.releases_notes, function (index, release) {
            html += "<b>" + release.version + " - " + release.date + "</b>";
            html += "<span class=\"help-block\">";
            html += "<ul>";
            html = Releases.eachChanges(release.changes, html);
            html += "</ul>";
            html += "</span>";
            html += "<hr>";
        });
        $("#releases_notes").append(html);
    }

    function loadDefault() {
        $.getJSON("../releases_note/en.json")
            .done(function (json) {
                load(json);
            })
            .fail(function (jqxhr, textStatus, error) {
                showError("JSON - Releases Notes not found!");
            });
    }

    var uiLanguage = chrome.i18n.getUILanguage().substr(0, 2);
    if (uiLanguage) {
        var path = "../releases_note/"+ uiLanguage + ".json";
        $.getJSON(path)
            .done(function (json) {
                load(json);
            })
            .fail(function (jqxhr, textStatus, error) {
                loadDefault();
            });
    } else {
        loadDefault();
    }
}
Releases.eachChanges = function (changes, html) {
    $.each(changes, function (index1, change) {
        if (typeof change != 'object') {
            html += "<li>" + change + "</li>";
        }
        if (change.changes) {
            html += "<ul>";
            html = Releases.eachChanges(change.changes, html);
            html += "</ul>";
        }
    });
    return html;
}

$(document).ready(function () {
    setVersionTitle();
    Releases.loadReleases();
    accounts = store.get("accounts") || [];
    loadAccountsToScreen();
    setInterval(function () {
        chrome.extension.getBackgroundPage().logInConsole("Find new Accounts...", true);
        var listAccounts = store.get("accounts");
        if (listAccounts) {
            for (var index in listAccounts) {
                if (!listAccounts[index].image) {
                    listAccounts.splice(index, 1);
                }
            }
            if (accounts.length != listAccounts.length) {
                chrome.extension.getBackgroundPage().logInConsole("New Account found!", true);
                accounts = listAccounts;
                loadAccountsToScreen();
            }
        }
    }, 3000);
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
});

function loadAccountsToScreen() {
    $("button.btn-confirm-logout").off();
    $("button.btn-disable").off();
    $("button.btn-logout").off();
    $("input").off();

    $("#accountList").html("");

    if (accounts.length == 0) {
        var msgAccountNotFound = chrome.i18n.getMessage("msg_account_not_found");
        $("#accountList").html('<div class="container jumbotron account"><span>' + msgAccountNotFound + '</span></div>');
    } else {
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

        $("button.btn-confirm-logout").on("click", function () {
            accounts = getBackgroundAccount();
            if (typeof componentToLogout == "undefined") {
                var msg = chrome.i18n.getMessage("msg_account_not_found");
                chrome.extension.getBackgroundPage().logInConsole(msg, false);
                showWarning(msg);
                return;
            }
            try {
                var indexToLogout = componentToLogout.id.substr(7);
                accounts[indexToLogout].disabled = true;
                chrome.extension.getBackgroundPage().logInConsole("@" + accounts[indexToLogout].screenName + " removed", true);
                chrome.extension.getBackgroundPage().twitter.abortStream(indexToLogout);
                accounts.splice(indexToLogout, 1);
                chrome.extension.getBackgroundPage().twitter.updateAccountsToStore();
                componentToLogout.parentNode.removeChild(componentToLogout);
                componentToLogout == undefined;
                loadAccountsToScreen();
                showSuccess(chrome.i18n.getMessage("msg_account_logout_success"))
            } catch (exception) {
                showError(exception);
            }
        });
        $("button.btn-disable").on("click", function () {
            try {
                var component = getSelectedAccountToChange.call(this);
                var index = component.id.substr(7);
                accounts = getBackgroundAccount();
                if (accounts[index].disabled) {
                    enableAccount(component);
                } else {
                    disableAccount(component);
                }
                chrome.extension.getBackgroundPage().twitter.updateAccountsToStore();
            } catch (exception) {
                showError(exception);
            }
        });
        $("button.btn-logout").on("click", function () {
            componentToLogout = getSelectedAccountToChange.call(this);
        });
        $("input").each(function () {
            if (this.id) {
                this.checked = store.get(this.id);
            }
        });
        $("input").on("change", function () {
            if (this.id) {
                store.set(this.id, this.checked);
            } else {
                var index = this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.id.substring(7);
                accounts = getBackgroundAccount();
                accounts[index][this.className] = this.checked;
                chrome.extension.getBackgroundPage().twitter.updateAccountsToStore();
            }
            chrome.extension.getBackgroundPage().logInConsole(this.id + "checked = " + this.checked, true);
        });
        function getSelectedAccountToChange() {
            return this.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode;
        }
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
    chrome.extension.getBackgroundPage().twitter.abortStream(index);

    $("#" + component.id).find(" :input").attr("disabled", true);
    $("#" + component.id).find("img,label,h2,h5,b").css("opacity", "0.2");
    buttonDisable.disabled = false;
    buttonDisable.innerText = "Enable";
    buttonDisable.classList.remove("btn-default");
    buttonDisable.classList.add("btn-success");

    chrome.extension.getBackgroundPage().logInConsole(accounts[index].screenName + " disabled", true);
    showSuccess(chrome.i18n.getMessage("msg_account_disabled_success"));
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
    buttonEnable.classList.add("btn-default");

    chrome.extension.getBackgroundPage().logInConsole(accounts[index].screenName + " enabled", true);
    showSuccess(chrome.i18n.getMessage("msg_account_enabled_success"));
}

function setVersionTitle() {
    $(".navbar-brand").append(chrome.i18n.getMessage("name_extension") + " " + chrome.extension.getBackgroundPage().twitter.version);
}

function showWarning(message) {
    $.notify({
        icon: '/images/warning_32.png',
        title: '<strong>' + chrome.i18n.getMessage("title_msg_warning") + '</strong>',
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
        title: '<strong>' + chrome.i18n.getMessage("title_msg_error") + '</strong>',
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
        title: '<strong>' + chrome.i18n.getMessage("title_msg_success") + '</strong>',
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

function getBackgroundAccount() {
    return chrome.extension.getBackgroundPage().accounts;
}