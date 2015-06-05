createLocalStorage();
createNotificationFixBugUserID();
lockBrowser();
addShortcut();
addContext();
setInterval(addAutoBlock,1000);
msgIncognito();
addEvents();

function addEvents() {
    chrome.windows.onCreated.addListener(function () {
        lockBrowser();
    });
}

function addContext() {
	chrome.contextMenus.removeAll();
	chrome.contextMenus.create({
		title: chrome.i18n.getMessage("context_block_i18n"),
		onclick:function(){
			validateLockContext();
		}
	});
}

function addShortcut() {
	chrome.commands.onCommand.addListener(function(command) {
		if (command == 'block_browser' && localStorage["use_short"] == 'true') {
			lockBrowser();
		}
	});
}

function msgIncognito() {
	chrome.extension.isAllowedIncognitoAccess(function(isAllowedAccess) {
	    if (isAllowedAccess) return;
	    alert(chrome.i18n.getMessage("msg_allow_incognito_i18n"));
	    chrome.tabs.create({
	        url: 'chrome://extensions/?id=' + chrome.runtime.id
	    });
	});
}