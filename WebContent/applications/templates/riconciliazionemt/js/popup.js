/***************************/
//@Author: Adrian "yEnS" Mato Gondelle
//@website: www.yensdesign.com
//@email: yensamg@gmail.com
//@license: Feel free to use it, but keep this credits please!
/***************************/

//SETTING UP OUR POPUP
//0 means disabled; 1 means enabled;
var popupStatus = 0;


//loading popup with jQuery magic!
function loadPopup(){
	//loads popup only if it is disabled
	if(popupStatus==0){
		$("#backgroundPopup").css({
			"opacity": "0.8"
		});
		$("#backgroundPopup").fadeIn("slow");
		$("#popupDettaglio").fadeIn("slow");
		popupStatus = 1;
	}
}

//disabling popup with jQuery magic!
function disablePopup(){
	//disables popup only if it is enabled
	if(popupStatus==1){
		$("#backgroundPopup").fadeOut("slow");
		$("#popupDettaglio").fadeOut("slow");
		popupStatus = 0;
	}
}

function getScrollXY() {
	var scrOfX = 0, scrOfY = 0;
	if( typeof( window.pageYOffset ) == 'number' ) {
		//Netscape compliant
		scrOfY = window.pageYOffset;
		scrOfX = window.pageXOffset;
	} 
	else if( document.body && ( document.body.scrollLeft || document.body.scrollTop ) ) {
		//DOM compliant
		scrOfY = document.body.scrollTop;
		scrOfX = document.body.scrollLeft;
	} 
	else if( document.documentElement && ( document.documentElement.scrollLeft || document.documentElement.scrollTop ) ) {
		//IE6 standards compliant mode
		scrOfY = document.documentElement.scrollTop;
		scrOfX = document.documentElement.scrollLeft;
	}
	return {X:scrOfX, Y:scrOfY};
}

function getWindowSize() {
	var myWidth = 0, myHeight = 0;
	if( typeof( window.innerWidth ) == 'number' ) {
		 //Non-IE
		 myWidth = window.innerWidth;
		 myHeight = window.innerHeight;
	} 
	else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
		 //IE 6+ in 'standards compliant mode'
		 myWidth = document.documentElement.clientWidth;
		 myHeight = document.documentElement.clientHeight;
	} 
	else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
		//IE 4 compatible
		myWidth = document.body.clientWidth;
		myHeight = document.body.clientHeight;
	}
	return{X:myWidth, Y:myHeight}
}

//centering popup
function centerPopup(dim){
	//request data for centering
	
	var windowDim = getWindowSize();
//	var popupHeight = 300 * numBol;
	var popupHeight = dim;

//    alert("numero bollettini = " + numBol + " altezza = " + popupHeight);

	var popupWidth = $("#popupDettaglio").width();
	var scroll = getScrollXY();
	//centering
    var windTop = windowDim.Y/2-popupHeight/2 + scroll.Y-15;
    if (windTop < 150) 
    	windTop = 150;
	$("#popupDettaglio").css({
		"position": "absolute",
		"top": windTop,
		"left": windowDim.X/2-popupWidth/2 + scroll.X-15,
		"height": popupHeight,
		"text-align": "center"
	});

	//only need force for IE6
	$("#backgroundPopup").css({
		"height": windowDim.Y
	}); 
}

function showAnomalia(errore) {
	var dim;
	populatePopup(errore);
	centerPopup(60);
    loadPopup();
}

function populatePopup(errore) {
	document.getElementById("error").innerHTML=errore;
}

$(document).ready(function(){

	$("#chiudiPopupDettaglio").click(function(){
		disablePopup();
	});

	$(document).keypress(function(e){
		if(e.keyCode==27 && popupStatus==1){
			disablePopup();
		}
	});

});

function number_format (number, decimals, dec_point, thousands_sep) {
    var n = number, prec = decimals;

    var toFixedFix = function (n,prec) {
        var k = Math.pow(10,prec);
        return (Math.round(n*k)/k).toString();
    };

    n = !isFinite(+n) ? 0 : +n;
    prec = !isFinite(+prec) ? 0 : Math.abs(prec);
    var sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep;
    var dec = (typeof dec_point === 'undefined') ? '.' : dec_point;

    var s = (prec > 0) ? toFixedFix(n, prec) : toFixedFix(Math.round(n), prec); //fix for IE parseFloat(0.55).toFixed(0) = 0;

    var abs = toFixedFix(Math.abs(n), prec);
    var _, i;

    if (abs >= 1000) {
        _ = abs.split(/\D/);
        i = _[0].length % 3 || 3;

        _[0] = s.slice(0,i + (n < 0)) +
              _[0].slice(i).replace(/(\d{3})/g, sep+'$1');
        s = _.join(dec);
    } else {
        s = s.replace('.', dec);
    }

    var decPos = s.indexOf(dec);
    if (prec >= 1 && decPos !== -1 && (s.length-decPos-1) < prec) {
        s += new Array(prec-(s.length-decPos-1)).join(0)+'0';
    }
    else if (prec >= 1 && decPos === -1) {
        s += dec+new Array(prec).join(0)+'0';
    }
    return s;
}


function dataIsoToIta(data) 
{
	var ris;
	ris = data.substring(8,10) + "/" + data.substring(5,7) + "/" + data.substring(0,4);
	if (ris == "01/01/1000")
			ris = "";
	return ris;
}