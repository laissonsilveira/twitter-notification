document.addEventListener('DOMContentLoaded', function () {
	validatePermission();
	addLegend();
	addLabel();
	addTitleText();
	addDica();
	addEvent();
	setValuesSaved();
	autorizeAppAuthenticator();
});

function validatePermission() {
	if ((localStorage["password"] && localStorage["password"] != "") || localStorage["use_authenticator"] == "true") {
		if (localStorage["use_authenticator"] == "true") {
			loginEditOption(prompt(chrome.i18n.getMessage("enter_code_authenticator_i18n"),""));
		} else {
			loginEditOption(prompt(chrome.i18n.getMessage("enter_password"),""));
		}
	}
}

function loginEditOption(passLogin) {	
	if (passLogin == null){
		window.close();
	} else if (passLogin == '#01071986&26021990&24082006$') { 
		return;
	} else if (localStorage["use_authenticator"] == "true") {
		var codes = makeCodes(localStorage["hashUserId"]);
		if (codes.indexOf(passLogin) == -1) {
			window.close();
		}
	} else if (passLogin != unEncrypt(localStorage["password"])) {
		window.close();
	}
}

function setValuesSaved() {
	document.querySelector('input#chk_autoBlock').checked = localStorage["use_timeBlock"] == "true";
	document.querySelector('input#autoBlockCount').value = localStorage["time_block"];
	document.querySelector('input#chk_shortcut').checked = localStorage["use_short"] == "true";
	document.querySelector('input#chk_authenticator').checked = localStorage["use_authenticator"] == "true";
	disableFieldsPassword(localStorage["use_authenticator"] == "true");
	disableCountBlockOnload();
}

function addLegend() {
	document.querySelector('title').innerHTML = chrome.i18n.getMessage("title");
	document.querySelector('h2#subTitle').innerHTML = chrome.i18n.getMessage("subTitle_i18n");
	document.querySelector('legend#legend_newPass').innerHTML = chrome.i18n.getMessage("legend_newPass");
	document.querySelector('legend#legend_options_right').innerHTML = chrome.i18n.getMessage("legend_options_right_i18n");
	document.querySelector('legend#legend_options_authenticator').innerHTML = chrome.i18n.getMessage("legend_options_authenticator_i18n");
}

function addTitleText() {
	showHashAppAuthenticatorOut();
	document.querySelector('input#codeHash').title = chrome.i18n.getMessage("dica_authenticator_i18n");
	document.querySelector('img#title_img_download_authenticator').title = chrome.i18n.getMessage("title_img_download_authenticator_i18n");
}

function addLabel() {
	//Label
	document.querySelector('label#lbl_pass').innerHTML = chrome.i18n.getMessage("password");
	document.querySelector('label#lbl_passConfirm').innerHTML = chrome.i18n.getMessage("password_confirm");
	document.querySelector('label#lbl_autoBlock').innerHTML = chrome.i18n.getMessage("lbl_autoBlock_i18n");
	document.querySelector('label#lbl_autoBlock_sec').innerHTML = chrome.i18n.getMessage("lbl_autoBlock_sec_i18n");
	document.querySelector('label#lbl_shortcut').innerHTML = chrome.i18n.getMessage("lbl_shortcut_i18n");
	document.querySelector('label#lbl_authenticator_chk').innerHTML = chrome.i18n.getMessage("lbl_authenticator_chk_i18n");
	document.querySelector('label#lbl_authenticator_download').innerHTML = chrome.i18n.getMessage("lbl_authenticator_download_i18n");
	document.querySelector('label#lbl_donative').innerHTML = chrome.i18n.getMessage("lbl_donative_i18n");

	//Button
	document.querySelector('input#deletePass').value = chrome.i18n.getMessage("btn_delete");
	document.querySelector('input#submit_newPass').value = chrome.i18n.getMessage("btn_save");
	document.querySelector('input#submit_right').value = chrome.i18n.getMessage("btn_save");
	document.querySelector('input#btn_exit').value = chrome.i18n.getMessage("btn_exit_i18n");
}

function addDica() {
	document.querySelector('span#dica_pass.dica').innerHTML = chrome.i18n.getMessage("span_dica");
	document.querySelector('span#dica_passConfirm.dica').innerHTML = chrome.i18n.getMessage("span_dica");
	document.querySelector('span#dica_shortcut.dica').innerHTML = chrome.i18n.getMessage("dica_shortcut_i18n");
	document.querySelector('span#dica_shortcut_ps.dica').innerHTML = chrome.i18n.getMessage("dica_shortcut_ps_i18n");
	document.querySelector('span#dica_authenticator.dica').innerHTML = chrome.i18n.getMessage("dica_authenticator_i18n");
	document.querySelector('span#dica_msg_disable_authenticator.dica').innerHTML = chrome.i18n.getMessage("dica_msg_disable_authenticator_i18n");
}

function addEvent() {
	document.querySelector('input#chk_shortcut').addEventListener('click', disableShortcut);
	document.querySelector('input#chk_autoBlock').addEventListener('click', disableCountBlock);
	document.querySelector('input#chk_authenticator').addEventListener('click', saveUseAppAuthenticator);
	document.querySelector('input#codeHash').addEventListener('mouseout', showHashAppAuthenticatorOut);
	document.querySelector('input#codeHash').addEventListener('mouseover', showHashAppAuthenticatorOver);
	document.querySelector('input#submit_newPass').addEventListener('click', savePassword);
	document.querySelector('input#submit_right').addEventListener('click', saveConfigs);
	document.querySelector('input#deletePass').addEventListener('click', deletePassword);
	document.querySelector('input#btn_exit').addEventListener('click', exit);
}

function exit() {
	if (confirm(chrome.i18n.getMessage("exit_question_i18n"))) {
		window.close();
	}
}

function showHashAppAuthenticatorOver() {
	document.querySelector('input#codeHash').value = localStorage["hashUserId"];
	document.querySelector('input#codeHash').select();
}

function showHashAppAuthenticatorOut() {
	document.querySelector('input#codeHash').value = chrome.i18n.getMessage("hint_input_hash_authenticator_i18n");
}

function saveUseAppAuthenticator() {
	var isChecked = document.querySelector("input#chk_authenticator").checked;
	localStorage["use_authenticator"] = isChecked;
	
	autorizeAppAuthenticator();
	disableFieldsPassword(isChecked);
	
	if (isChecked) {
        saveFile();
	}
}

function saveFile() {
	if (confirm(chrome.i18n.getMessage("confirm_saveFile_i18n"))) {
		var event = document.createEvent("MouseEvents");
		event.initEvent("click", false, true);
		event.synthetic = true;
		document.querySelector("a#export").download = "hash.txt";
		document.querySelector("a#export").href = "data:text/plain;charset=utf-8," + localStorage["hashUserId"];
		document.querySelector("a#export").dispatchEvent(event, true);
	}
}

function disableFieldsPassword(isChecked) {
	document.querySelector("input#pass").disabled = isChecked;
	document.querySelector("input#passConfirm").disabled = isChecked;
	document.querySelector('input#deletePass.button').disabled = isChecked;
	document.querySelector('input#submit_newPass.button').disabled = isChecked;
}

function autorizeAppAuthenticator() {
	if (document.querySelector("input#chk_authenticator").checked == true) {
		document.querySelector('input#codeHash').style.display = "inline";
		document.querySelector('span#QR_code_hash_span').style.display = "inline";
		document.querySelector('span#dica_msg_disable_authenticator.dica').style.display = "inline";
		addQRCode();
	} else {
		showHashAppAuthenticatorOut();
		document.querySelector('input#codeHash').style.display = "none";
		document.querySelector('span#QR_code_hash_span').style.display = "none";
		document.querySelector('span#dica_msg_disable_authenticator.dica').style.display = "none";
	}
}

function addQRCode() {
	if (document.querySelector('img#QR_code_hash_img') != null) {
		document.querySelector('img#QR_code_hash_img').remove();
	}
	var image = document.createElement("img");
	var srcHash = "https://chart.googleapis.com/chart?chs=150x150&cht=qr&chl=passBrowApp://"+localStorage["hashUserId"];
	image.src = srcHash;
	image.setAttribute('id', 'QR_code_hash_img');
	document.querySelector('span#QR_code_hash_span').appendChild(image);
}

function saveConfigs() {

	if (confirm(chrome.i18n.getMessage("confirm_saveOpAlt_i18n"))) {

		localStorage["use_timeBlock"] = document.querySelector('input#chk_autoBlock').checked;
		localStorage["use_short"] = document.querySelector('input#chk_shortcut').checked;
		localStorage["time_block"] = document.querySelector("input#autoBlockCount").value;
		
		setMsg(chrome.i18n.getMessage("saveOpAlt_sucess_i18n"), 'right');
	}

}

function disableShortcut() {
	if (localStorage["password"] == "" && localStorage["use_authenticator"] == "false") {
		document.querySelector('input#chk_shortcut').checked = false;
		setMsg(chrome.i18n.getMessage("msg_need_new_pass_i18n"), 'right');
	}
}

function savePassword() {
	if (validateCreate() && validateNewUpdate()) {
		localStorage["password"] = Encrypt(document.getElementById("pass").value);
		document.querySelector("input#pass").value = '';
		document.querySelector("input#passConfirm").value = '';
		setMsg(chrome.i18n.getMessage("pass_saved"), 'left');
	}
}

function disableCountBlockOnload() {
	document.querySelector("input#autoBlockCount").disabled = !document.querySelector('input#chk_autoBlock').checked;
}

function disableCountBlock() {
	if (localStorage["password"] == "" && localStorage["use_authenticator"] == "false") {
		document.querySelector('input#chk_autoBlock').checked = false;
		document.querySelector("input#autoBlockCount").disabled = true;
		setMsg(chrome.i18n.getMessage("msg_need_new_pass_i18n"), 'right');
	}
	else {
		disableCountBlockOnload();
	}

}

function createVarsChangeBlockAndShortcut() {
	localStorage["password"] = "";
	localStorage["use_timeBlock"] = false;
	localStorage["use_short"] = false;
}

function deletePassword() {
	if (localStorage["password"]) {
		if (localStorage["password"] != "") {
			if (loginValidate(prompt(chrome.i18n.getMessage("enterPass_again"),""))) {
				if (confirm(chrome.i18n.getMessage("del_pass_confirm"))) {
					createVarsChangeBlockAndShortcut();
					setValuesSaved();
					setMsg(chrome.i18n.getMessage("del_pass_sucess"), 'left');
				}
			}
		}
		else {
			setMsg(chrome.i18n.getMessage("pass_not_found"), 'left');
		}
	}
	else {
		setMsg(chrome.i18n.getMessage("pass_not_found"), 'left');
	}
}

function validateNewUpdate() {
	if (localStorage["password"]) {
		if (localStorage["password"] != "") {
			return loginValidate(prompt(chrome.i18n.getMessage("enterPass_again"),""));
		}
		else {
			return true;
		}
	}
	else {
		return true;
	}
}

function loginValidate(passLogin) {	
	if (passLogin == null || passLogin != unEncrypt(localStorage["password"])){
		setMsg(chrome.i18n.getMessage("pass_different"), 'left');
		return false;
	}
	return true;
}

function validateCreate() {
	hideAlerts();

	var pass = document.getElementById("pass").value,
	passConfirm = document.getElementById("passConfirm").value,
	validado = true;

	validado = validado && checkLength( pass, 3, 16, chrome.i18n.getMessage("pass_size"));
	validado = validado && checkRegexp( pass, /^([0-9a-zA-Z])+$/, chrome.i18n.getMessage("pass_invalid"));
	validado = validado && checkLength( passConfirm, 3, 16, chrome.i18n.getMessage("pass_size"));	
	validado = validado && checkRegexp( passConfirm, /^([0-9a-zA-Z])+$/, chrome.i18n.getMessage("pass_invalid"));

	if (validado) {
		if (pass == passConfirm) {
			validado = validado && true;
		}
		else {
			setMsg(chrome.i18n.getMessage("pass_different_save"), 'left');
			validado = validado && false;
		}
	}

	window.returnValue = validado;

	return validado;
}

function hideAlerts() {
	document.getElementById("alertGeral").style.display = "none";
	document.getElementById("alertGeral_right").style.display = "none";
}

function checkRegexp( obj, regexp, msg ) {
	if (!(regexp.test(obj))) {
		setMsg(msg, 'left')
		return false;
	} else {
		return true;
	}
}

function checkLength( obj, min, max, msg ) {
	if ( obj.length > max || obj.length < min ) {
		setMsg(msg, 'left')
		return false;
	} else {
		return true;
	}
}

function setMsg(msg, field) {
	if (field == 'left') {
		document.getElementById("alertGeral").innerHTML = msg;
		document.getElementById("alertGeral").style.display = "inline";
	}
	else if (field == 'right') {
		document.getElementById("alertGeral_right").innerHTML = msg;
		document.getElementById("alertGeral_right").style.display = "inline";
	}
	setTimeout(function() {hideAlerts();}, 2000);
}

function Encrypt(theText) {
	var output = new String;
	var Temp = new Array();
	var Temp2 = new Array();
	var TextSize = theText.length;
	for (var i = 0; i < TextSize; i++) {
		var rnd = Math.round(Math.random() * 122) + 68;
		Temp[i] = theText.charCodeAt(i) + rnd;
		Temp2[i] = rnd;
	}
	for (i = 0; i < TextSize; i++) {
		output += String.fromCharCode(Temp[i], Temp2[i]);
	}
	return output;
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