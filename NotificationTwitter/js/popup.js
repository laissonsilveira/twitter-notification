function onClickOpenOptionsButton() {
    $("#openOptions").on("click", function () {
        chrome.tabs.create({url: "html/options.html"});
    });
}

function setVersionTitlePopup() {
    $(".title-nav-bar").append(" " + chrome.extension.getBackgroundPage().twitter.version);
}

function addAccountsToScreenPopup(accounts) {
    try {
        $('.on-off-account').off();
        if (accounts.length == 0) {
            $("#accountListPopup").html('<div class="container jumbotron"><span>No accounts connected</span></div>');
        } else {
            $("#accountListPopup").html("");
            for (var index in accounts) {
                $("#accountPopupTemplate").clone().appendTo("#accountListPopup").attr("id", "account" + index).show();
                $("#account" + index).find(".name-account").text("@" + accounts[index].screenName);
                $("#account" + index).find("img").attr("src", accounts[index].image.replace("normal", "reasonably_small"));
                $("#accountPopupTemplate").find(".on-off-account").attr("id", "enableAccount" + index);
                var on = (accounts[index].disabled == false);
                $(".on-off-account").bootstrapSwitch("state", on);
            }
            $('.on-off-account').on('switchChange.bootstrapSwitch', function (e, onOff) {
                chrome.extension.getBackgroundPage().accounts[index].disabled = !onOff;
                if (onOff) {
                    chrome.extension.getBackgroundPage().twitter.startStream(index);
                } else {
                    chrome.extension.getBackgroundPage().twitter.abortStream(index);
                }
                chrome.extension.getBackgroundPage().twitter.updateAccountsToStore();

                var views = chrome.extension.getViews();
                for (var w in  views) {
                    var href = views[w].location.href;
                    if (href.search("/html/options.html") != -1) {
                        views[w].document.location.reload();
                    }
                }
                chrome.extension.getBackgroundPage().logInConsole("Changed account enable/disable: " + onOff, true);
            });
            chrome.extension.getBackgroundPage().logInConsole("Add to screen popup Account: @" + accounts[index].screenName, true);
        }
    } catch (exception) {
        chrome.extension.getBackgroundPage().logInConsole(exception, true);
    }

}

$(document).ready(function () {

    setVersionTitlePopup();

    var accounts = store.get("accounts") || [];
    onClickOpenOptionsButton();
    addAccountsToScreenPopup(accounts);

    setTimeout(function() {
        $("#openOptions").focus();
    }, 50);
});