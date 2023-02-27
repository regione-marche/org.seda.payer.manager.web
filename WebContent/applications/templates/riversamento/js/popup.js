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
		"height": popupHeight
	});

	//only need force for IE6
	$("#backgroundPopup").css({
		"height": windowDim.Y
	}); 
}
/*
function showDettaglio(testata, nomeCampo, valoreCampo) {
//	alert("populatePopup");
	populatePopup(testata, nomeCampo, valoreCampo, '','');
    centerPopup(50);
//	alert(url);
    loadPopup();
}
*/
function showDettaglio(testata, nomeCampo1, valoreCampo1, nomeCampo2, valoreCampo2) {
//	alert("populatePopup");
	var dim;
	populatePopup(testata, nomeCampo1, valoreCampo1, nomeCampo2, valoreCampo2);
    if (nomeCampo2 == "")
    	dim = 60;
    else
    	dim = 100;
	centerPopup(dim);
//	alert(url);
    loadPopup();
}

function populatePopup(testata, nomeCampo1, valoreCampo1, nomeCampo2, valoreCampo2) {
	
//	alert("1 " + document.getElementById("chiave_transazione").innerHTML);
	var data;
	document.getElementById("testata").innerHTML=testata;
	document.getElementById("nomeCampo").innerHTML=nomeCampo1;
	document.getElementById("valoreCampo").innerHTML=valoreCampo1;
	if (nomeCampo2 != "")
	{
		document.getElementById("nomeCampo2").innerHTML=nomeCampo2;
		
		if (nomeCampo2.indexOf("Data",0)!=-1)
			data=dataIsoToIta(valoreCampo2);
		else 
			data=valoreCampo2;
		
		document.getElementById("valoreCampo2").innerHTML=data;
	}
//	alert("2 " + document.getElementById("chiave_transazione").innerHTML);
	
/*
	var url = url + '&csrfToken=' +$("input[name=csrfToken]").val();
	$.getJSON(url, function(jsonObject) {
        $('#dt_chiave_transazione').html(jsonObject.beanTransazioni.beanTransazioni.chiave_transazione).show();
		$('#dt_importo_totale_transazione').html(jsonObject.beanTransazioni.beanTransazioni.importo_totale_transazione).show();
//		var importo_costo = parseFloat((parseFloat(jsonObject.beanTransazioni.beanTransazioni.importo_costo_transazione) + parseFloat(jsonObject.beanTransazioni.beanTransazioni.importo_costo_spese_di_notifica)).toFixed(2));

        var numBol = jsonObject.listFreccia.length + jsonObject.listIci.length + jsonObject.listIV.length;
        var dimICI = 0;
        $.each(jsonObject.listIci, function(index, ici)  
        	{	
             var tipoBollICI = ici.tipo_bollettino.substring(0,3);
             switch (tipoBollICI) 
            	{
                 case 'ICI': dimICI = dimICI + 280;break;
                 case 'ISC': dimICI = dimICI + 240;break;
            	}
        	});
        
        var dimTFR = 150;

        
        var dimTDT = 0;
        $.each(jsonObject.listIV, function(index, iv)  
        	{
            var tipoBollIV = iv.tipo_bollettino.substring(0,3);
            switch (tipoBollIV) 
	            {
	                case 'BOL': dimTDT = dimTDT + 220;break;
	                case 'SPO': dimTDT = dimTDT + 160;break;
	                case 'CDS': dimTDT = dimTDT + 200;break;
	                case 'MAV': dimTDT = dimTDT + 160;break;
	                case 'PRE': dimTDT = dimTDT + 200;break;
	            }
        	});

        var dimBol = 100 + jsonObject.listFreccia.length * dimTFR + dimICI + dimTDT;

//        alert('dim tfr = ' + dimTFR + ' dim ici = ' + dimICI + ' dim dtd = ' + dimTDT + ' dim totale = ' + dimBol);

        centerPopup(dimBol);
        
		var importo_costo_tra = jsonObject.beanTransazioni.beanTransazioni.importo_costo_transazione.replace(',','.');
		var importo_costo_not = jsonObject.beanTransazioni.beanTransazioni.importo_costo_spese_di_notifica.replace(',','.');
		var importo_costo = parseFloat(importo_costo_tra) + parseFloat(importo_costo_not);

	    $('#dt_importo_costo_transazione').html(number_format(importo_costo,2,',','.')).show();

// aggiunta in modifica
	    $('#dt_id_banca').html(jsonObject.beanTransazioni.beanTransazioni.codice_identificativo_banca);
	    $('#dt_autorizzazione_banca').html(jsonObject.beanTransazioni.beanTransazioni.codice_autorizzazione_banca);
	    
	    $.each(jsonObject.listFreccia, function(index, freccia)  {
            $('#fr_importo_totale_bollettino').html(freccia.importo_totale_bollettino);
            $('#fr_chiave_spedizione').html(freccia.chiave_spedizione.length==0?'No':'Si');
            $('#fr_codice_identificativo_pagamento').html(freccia.codice_identificativo_pagamento);
            $('#fr_motivo_del_pagamento').html(freccia.motivo_del_pagamento);
            $('#fr_codice_iban').html(freccia.codice_iban);
            $('#fr_descrizione_intestatario_conto_corrente').html(freccia.descrizione_intestatario_conto_corrente);
            $('#fr_codice_freccia').html(freccia.codice_freccia);
            $('#fr_codice_servizio').html(freccia.codice_servizio);
            $('#fr_eseguito_da').html(freccia.denominazione_contribuente+' '+freccia.indirizzo+' '+freccia.codice_cap+' '+freccia.citta+' '+freccia.provincia);
            $('#dt_table_freccia').clone(true).attr('id','').appendTo('#div_table_freccia');
	    });
        $('#dt_table_freccia').hide();
        $.each(jsonObject.listFreccia, function(index, freccia)  {
            var clone = $('#dt_table_freccia').clone(true);
            $('#fr_importo_totale_bollettino',clone).html(freccia.importo_totale_bollettino);
            $('#fr_chiave_spedizione',clone).html(freccia.chiave_spedizione.length==0?'No':'Si');
            $('#fr_codice_identificativo_pagamento',clone).html(freccia.codice_identificativo_pagamento);
            $('#fr_motivo_del_pagamento',clone).html(freccia.motivo_del_pagamento);
            $('#fr_codice_iban',clone).html(freccia.codice_iban);
            $('#fr_descrizione_intestatario_conto_corrente',clone).html(freccia.descrizione_intestatario_conto_corrente);
            $('#fr_codice_freccia',clone).html(freccia.codice_freccia);
            $('#fr_codice_servizio',clone).html(freccia.codice_servizio);
            $('#fr_eseguito_da',clone).html(freccia.denominazione_contribuente+' '+freccia.indirizzo+' '+freccia.codice_cap+' '+freccia.citta+' '+freccia.provincia);
            clone.attr('id', '').appendTo('#div_table_freccia');
        });
        $('#dt_table_freccia').hide();

        $.each(jsonObject.listIV, function(index, iv)  {

            var clone = $('#dt_table_iv').clone(true);
            var tipoBollettino = iv.tipo_bollettino.substring(0,3);
            var descBollettino;
            switch (tipoBollettino) {
                case 'BOL': descBollettino = 'Bollettino BOLLO';break;
                case 'SPO': descBollettino = 'Bollettino Spontaneo';break;
                case 'CDS': descBollettino = 'Bollettino CDS';break;
                case 'MAV': descBollettino = 'Bollettino MAV';break;
                case 'PRE': descBollettino = 'Bollettino Premarcato';break;
            }
            $('.tipo_bollettino_'+tipoBollettino,clone).attr('class','');
//            $('#dt_thead_iv',clone).html(iv.descrizione_bollettino);
            $('#dt_thead_iv',clone).html(descBollettino);

            $('#iv_importo_totale_bollettino',clone).html(iv.importo_totale_bollettino);
            $('#iv_rendicontato',clone).html(iv.chiave_spedizione.length==0?'No':'Si');
            $('#iv_numero_conto_corrente',clone).html(iv.numero_conto_corrente);
            $('#iv_descrizione_intestatario_conto_corrente',clone).html(iv.descrizione_intestatario_conto_corrente);
            // Bollo
            if(tipoBollettino=='BOL')  {
                $('#bol_importo_oneri',clone).html(iv.importo_oneri);
                $('#bol_targa',clone).html(iv.targa);
                $('#bol_importo_residuo_totale',clone).html(iv.importo_residuo_totale);
                $('#bol_categoria_bollo_auto',clone).html(iv.categoria_bollo_auto);
                $('#bol_importo_residuo_compenso',clone).html(iv.importo_residuo_compenso);
                $('#bol_mesi_riduzione_bollo_auto',clone).html(iv.mesi_riduzione_bollo_auto);
//                $('#bol_scadenza_bollo_auto',clone).html(iv.mese_scadenza_bollo_auto+ ' '+iv.anno_scadenza_bollo_auto);
                $('#bol_dettaglio_scadenza_bollo_auto',clone).html(iv.mese_scadenza_bollo_auto+ ' '+iv.anno_scadenza_bollo_auto);     
                $('#bol_mesi_validita_bollo_auto',clone).html(iv.mesi_validita_bollo_auto);
                $('#bol_codice_fiscale',clone).html(iv.codice_fiscale);
                $('#bol_codice_servizio',clone).html(iv.codice_servizio);
            }
            if(tipoBollettino=='CDS')  {
                $('#cds_codice_bollettino_premarcato_mav',clone).html(iv.codice_bollettino_premarcato_mav);
                $('#cds_tipo_documento_host',clone).html(iv.tipo_documento_host);
                $('#cds_numero_documento',clone).html(iv.numero_documento);
                $('#cds_data_sanzione',clone).html(iv.data_sanzione);
                $('#cds_codice_fiscale',clone).html(iv.codice_fiscale);
                $('#cds_targa',clone).html(iv.targa);
                $('#cds_codice_servizio',clone).html(iv.codice_servizio);
            }
            if(tipoBollettino=='SPO')  {
                var text = $('#spo_note_premarcato',clone).html()+iv.note_premarcato;
                $('#spo_note_premarcato',clone).html(text);
                $('#spo_codice_servizio',clone).html(iv.codice_servizio);
            }
            if(tipoBollettino=='MAV')  {
                $('#mav_codice_bollettino_premarcato_mav',clone).html(iv.codice_bollettino_premarcato_mav);
                $('#mav_tipo_documento_host',clone).html(iv.tipo_documento_host);
                $('#mav_numero_documento',clone).html(iv.numero_documento);
                $('#mav_codice_servizio',clone).html(iv.codice_servizio);
            }
            if(tipoBollettino=='PRE')  {
                $('#pre_codice_bollettino_premarcato_mav',clone).html(iv.codice_bollettino_premarcato_mav);
                $('#pre_tipo_documento_host',clone).html(iv.tipo_documento_host);
                $('#pre_numero_documento',clone).html(iv.numero_documento);
                $('#pre_data_scadenza_rata',clone).html(iv.data_scadenza_rata);
                $('#pre_codice_fiscale',clone).html(iv.codice_fiscale);
                $('#pre_progressivo_rata',clone).html(iv.progressivo_rata);
                $('#pre_codice_servizio',clone).html(iv.codice_servizio);
            }
            $('#iv_denominazione',clone).html(iv.denominazione + ' ' + iv.indirizzo+' '+iv.cap+' '+iv.citta+' '+iv.provincia);
            clone.attr('id', '').appendTo('#div_table_iv');
        });
        $('#dt_table_iv').hide();
// ICI controllato
        $.each(jsonObject.listIci, function(index, ici)  {	

            var clone = $('#dt_table_ici').clone(true);
            var tipoBollettino;
            var codiceBollettino = ici.tipo_bollettino.substring(0,3);
            switch (codiceBollettino) {
                case 'ICI': tipoBollettino = 'Bollettino ICI';break;
                case 'ISC': tipoBollettino = 'Bollettino ISCOP';break;
            }
            $('.tipo_bollettino_'+ici.tipo_bollettino,clone).attr('class','');
            $('#dt_thead_ici',clone).html(tipoBollettino);
            $('#ici_importo_movimento',clone).html(ici.importo_movimento);
            $('#ici_chiave_spedizione',clone).html(ici.chiave_spedizione.length==0?'No':'Si');
            $('#ici_numero_conto_corrente',clone).html(ici.numero_conto_corrente);
            $('#ici_descrizione_intestatario_conto_corrente',clone).html(ici.descrizione_intestatario_conto_corrente);
            $('#ici_comune_di_ubicazione_immobile',clone).html(ici.comune_di_ubicazione_immobile);
            $('#ici_cap_comune_ubicazione_immobile',clone).html(ici.cap_comune_ubicazione_immobile);

            // ISCOP
            if(codiceBollettino=='ISC')  {
                $('#ici_imponibile_ici_per_iscop',clone).html(ici.imponibile_ici_per_iscop);
                $('#ici_detrazione_per_iscop',clone).html(ici.detrazione_per_iscop);
                $('#ici_riduzione_per_iscop',clone).html(ici.riduzione_per_iscop);
                $('#iscop_anno_imposta',clone).html(ici.anno_imposta);

                $('#iscop_versamento_acconto',clone).html((ici.flag_rata == '0' || ici.flag_rata == '3')?'Si':'No');
            	$('#iscop_versamento_saldo',clone).html((ici.flag_rata == '1' || ici.flag_rata == '3')?'Si':'No');
                $('#iscop_flag_ravvedimento',clone).html(ici.flag_ravvedimento=='N'?'No':'Si');
                $('#iscop_codice_fiscale',clone).html(ici.codice_fiscale);
                }
            //ICI
            else if (codiceBollettino=='ICI')  {
                $('#ici_importo_terreni_agricoli',clone).html(ici.importo_terreni_agricoli);
                $('#ici_numero_fabbricati',clone).html(ici.numero_fabbricati);
                $('#ici_importo_aree_fabbricabili',clone).html(ici.importo_aree_fabbricabili);
                $('#ici_anno_imposta',clone).html(ici.anno_imposta);
                $('#ici_importo_abitazione_principale',clone).html(ici.importo_abitazione_principale);
                $('#ici_importo_altri_fabbricati',clone).html(ici.importo_altri_fabbricati);
                $('#ici_importo_detrazione_statale',clone).html(ici.importo_detrazione_statale);
                $('#ici_importo_detrazione_comunale',clone).html(ici.importo_detrazione_comunale);

                $('#ici_versamento_acconto',clone).html((ici.flag_rata == '0' || ici.flag_rata == '3')?'Si':'No');
                $('#ici_versamento_saldo',clone).html((ici.flag_rata == '1' || ici.flag_rata == '3')?'Si':'No');
                $('#ici_flag_ravvedimento',clone).html(ici.flag_ravvedimento=='N'?'No':'Si');
                $('#ici_codice_fiscale',clone).html(ici.codice_fiscale);
               }
//            $('#ici_codice_fiscale',clone).html(ici.codice_fiscale);
//            $('#ici_flag_ravvedimento',clone).html(ici.flag_ravvedimento=='N'?'No':'Si');
//            $('#ici_versamento_acconto',clone).html((ici.flag_rata == '0' || ici.flag_rata == '3')?'Si':'No');
//            $('#ici_versamento_saldo',clone).html((ici.flag_rata == '1' || ici.flag_rata == '3')?'Si':'No');
            $('#ici_codice_servizio',clone).html(ici.codice_servizio);
            $('#ici_denominazione',clone).html(ici.denominazione.replace("|"," ")+' '+ici.indirizzo_domicilio_fiscale+' '+ici.domicilio_fiscale);
            clone.attr('id', '').appendTo('#div_table_ici');
        });
        $('#dt_table_ici').hide();

	});
	*/
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