$(document).ready(() => localizeI18n());

const translate = (messageID, args) => chrome.i18n.getMessage(messageID, args);

const localizeI18n = () => {
    $('[i18n]').each(function () {
        $(this).append(translate($(this).attr('i18n')));
    });
};