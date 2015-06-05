document.onclick = function(event) {
	if (event.srcElement.id == 'cancel') {
		window.close();
	}
	else if (event.srcElement.id == 'options') {
		chrome.tabs.create({url:"html/options.html"});
		window.close();
	}
	else if (event.srcElement.id == 'block') {
		if ((localStorage["password"] == null || localStorage["password"] == '') && localStorage["use_authenticator"] == "false") {
			setMsg(chrome.i18n.getMessage("msg_need_new_pass_i18n"));
		}
		else {
			lockBrowser();
			window.close();
		}
	}
}

document.addEventListener('DOMContentLoaded', function () {
	document.querySelector('title').innerHTML = chrome.i18n.getMessage("title");
	document.querySelector('legend#legend_passBrow').innerHTML = chrome.i18n.getMessage("leg_passBrow_i18n");
	document.querySelector('input#options').value = chrome.i18n.getMessage("btn_options_i18n");
	document.querySelector('input#block').value = chrome.i18n.getMessage("btn_block_i18n");
	document.querySelector('input#cancel').value = chrome.i18n.getMessage("btn_cancel_i18n");
});

function clearFields() {
	document.getElementById("pass").value = "";
	document.getElementById("passConfirm").value = "";
}

function hideAlerts() {
	document.getElementById("alertGeral").style.display = "none";
}

function setMsg(msg) {
	document.getElementById("alertGeral").innerHTML = msg;
	document.getElementById("alertGeral").style.display = "inline";
	setTimeout(function() {hideAlerts()}, 2000);
}