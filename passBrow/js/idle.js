var lastState = "active";
function addAutoBlock() {
	if(localStorage["use_timeBlock"] == "true" 
		&& ((localStorage["password"] != null || localStorage["password"] != '') 
				|| localStorage["use_authenticator"] == "true")){
		chrome.idle.queryState(parseInt(localStorage["time_block"]), function(state) {
			if(state == "idle" && lastState == "active"){
				lockBrowser();
			}
			lastState = state;
		});
	}
}

