function simulateButtonSubmit(buttonName) {
	// simulo il click sul bottone 
    $("button#" + buttonName).click();
}


function setValore(input, predefinito, flag) {
	var output = predefinito;
	var flag = flag;
	if ((input != null) && (input != ""))
	{
		 output = input;
		 if (flag == "Y")
			 output = output-1;
	}	 
	return output;
}


/********** METODI PER DATE PICKER *************/
function setValoreDatePicker(input, predefinito) {
	var output = predefinito;
	if ((input != null) && (input != "") && (input != "0"))
		 output = input;
	return output;
}

function updateValoreDatePickerFromDdl(day_field, month_field, year_field, hidden_field) {
	//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
	var day = $("#" + day_field).val();
    var month = $("#" + month_field).val();
    var year = $("#" + year_field).val();
    
    var date = null;
    if (day != null && day != "" && month != null && month != "" && year != null && year != "")
    {
        date = day + "/" + month + "/" + year;
    }
    else
    {
        //se la data non è stata impostata o è stata impostata in parte, la setto alla data di oggi
        var today = new Date();
        date = today.getDate() + "/" + (today.getMonth() + 1) + "/" + today.getFullYear();
    }
    $("#" + hidden_field).datepicker("setDate" , date);
}

function initDatePicker(minDate, maxDate, annoDa, annoA, day_field, month_field, year_field, hidden_field)
{
    $("#" + hidden_field).datepicker("option", "dateFormat", "dd/mm/yyyy");
	$("#" + hidden_field).datepicker({
		minDate: minDate,
		maxDate: maxDate,
		yearRange: annoDa + ":" + annoA,
		showOn: "button",
		buttonImage: "../applications/templates/shared/img/calendar.gif",
		buttonImageOnly: true,
		onSelect: function(dateText, inst) {$("#" + day_field).val(dateText.substr(0,2));
											$("#" + month_field).val(dateText.substr(3,2));
											$("#" + year_field).val(dateText.substr(6,4));
											},
		beforeShow: function(input, inst) {
			//imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
            updateValoreDatePickerFromDdl(day_field, month_field, year_field, hidden_field);
		}
	});
}



