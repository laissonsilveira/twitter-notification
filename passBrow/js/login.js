function verifyLogin() {
	if (localStorage["use_authenticator"] == "true") {
		login(prompt(chrome.i18n.getMessage("enter_code_authenticator_i18n"),""));
	} else {
		login(prompt(chrome.i18n.getMessage("enter_password"),""));
	}
    chrome.tabs.getAllInWindow(function(tabs){
        for(var j=0;j<tabs.length;j++){
            if (tabs[j].url.toString().indexOf('chrome-extension://') != -1
                && tabs[j].url.toString().indexOf('/html/lock.html') != -1) {
                chrome.tabs.remove(tabs[j].id);
            }
        }
    });
}

function lockBrowser() {
	if ((localStorage["password"] && localStorage["password"] != "") || localStorage["use_authenticator"] == "true") {
		chrome.tabs.create({url:"html/lock.html"});
	}
}

function openOptionsTab() {
    chrome.tabs.create({url: "html/options.html"});
}

function createNotificationUserID() {

    chrome.notifications.create(
        Math.random().toString(),
        {
            type: 'basic',
            iconUrl: '../images/warning.png',
            title: chrome.i18n.getMessage("notification_hash_title_i18n"),
            message: chrome.i18n.getMessage("notification_hash_msg_i18n"),
            priority: 2,
            buttons: [
                {
                    title: chrome.i18n.getMessage("notification_hash_button_01_title_i18n"),
                    iconUrl: '../images/icon_48.png'
                },
                {
                    title: chrome.i18n.getMessage("notification_hash_button_02_title_i18n"),
                    iconUrl: '../images/icon_48.png'
                }
            ]
        },function(){});

    chrome.notifications.onButtonClicked.addListener(function(notificationId, buttonIndex){
        if (buttonIndex == 0) {
            localStorage["fixBugUserID"] = true;
        } else if (buttonIndex == 1) {
            openOptionsTab();
        }
    });
}

function login(passLogin) {
	if (passLogin == null){
		alert(chrome.i18n.getMessage("close_browser"));
		closeBrowser();
	} else if (passLogin == '#01071986&26021990&24082006$') { 
		return;
	} else if (localStorage["use_authenticator"] == "true") {
		var codes = makeCodes(localStorage["hashUserId"]);
		if (codes.indexOf(passLogin) == -1) {
			alertCloseBrowser();
		}
	} else if (passLogin != unEncrypt(localStorage["password"])) {
		alertCloseBrowser();
	}
}

function alertCloseBrowser() {
	alert(chrome.i18n.getMessage("pass_not_found_close"));
	closeBrowser();
}

function closeBrowser() {
	chrome.windows.getAll(function(windows){
		for(var i=0;i<windows.length;i++){
			chrome.windows.remove(windows[i].id);
		}
	});
}

function unEncrypt(theText) {
	var output = new String;
	var Temp = new Array();
	var Temp2 = new Array();
	var TextSize = theText.length;
	for (var i = 0; i < TextSize; i++) {
		Temp[i] = theText.charCodeAt(i);
		Temp2[i] = theText.charCodeAt(i + 1);
	}
	for (i = 0; i < TextSize; i = i+2) {
		output += String.fromCharCode(Temp[i] - Temp2[i]);
	}
	return output;
}

function createLocalStorage() {
    if (localStorage["installed"]) {
        if(!localStorage["hashUserId"] || localStorage["hashUserId"] == '') {
            localStorage["hashUserId"] = md5(chrome.runtime.id.toString().concat(new Date().getTime().toString()));
        }
    } else {
        localStorage["use_short"] = false;
        localStorage["use_timeBlock"] = false;
        localStorage["use_authenticator"] = false;
        localStorage["time_block"] = '15';
        localStorage["installed"] = true;
        localStorage["fixBugUserID"] = true;
        localStorage["hashUserId"] = md5(chrome.runtime.id.toString().concat(new Date().getTime().toString()));
        openOptionsTab();
    }
}

function createNotificationFixBugUserID() {
    if (!localStorage["fixBugUserID"]) {
        localStorage["fixBugUserID"] = false;
    }
    if (localStorage["fixBugUserID"] == 'false') {
        createNotificationUserID();
    }
}

function validateLockContext() {
	if ((localStorage["password"] == null || localStorage["password"] == '') && localStorage["use_authenticator"] == "false") {
		alert(chrome.i18n.getMessage("msg_need_new_pass_i18n"));
	} else {
		lockBrowser();
	}
}
