
function loadAutoCompletion(txtName) {
	var url = "autocompleteJson.do?csrfToken=" +$("input[name=csrfToken]").val();
	$.getJSON(url, function(jsonObject) {
		$("#" + txtName).autocomplete({
		    data: jsonObject,
		    maxItemsToShow: 100
	    });
	});
}

/** da richiamare all'onchange della textbox**/
function loadAutoCompletionPartial(txtName) {
	var url = "autocompleteJson.do?filter=" + $("#" + txtName).val() + "&csrfToken=" +$("input[name=csrfToken]").val();
	$.getJSON(url, function(jsonObject) {
		$("#" + txtName).autocomplete({
		    data: jsonObject,
		    maxItemsToShow: 100
	    });
	});
}