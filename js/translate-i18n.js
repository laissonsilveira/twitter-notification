$(document).ready(function () {
    localizeI18n();
});

translate = function (messageID, args) {
    return chrome.i18n.getMessage(messageID, args);
};

localizeI18n = function () {
    //translate a page into the users language
    $("[i18n]").each(function () {
        $(this).append(translate($(this).attr("i18n")));
    });
};