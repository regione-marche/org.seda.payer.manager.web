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
	//ripulisce il css per la stampa della popup per permettere la stampa della pagina dei risultati 
    /*var linkCss = document.getElementById('printdetailtransazione_css');
    if (linkCss != null)
        linkCss.href = "";*/
    
	//disables popup only if it is enabled
	if(popupStatus==1){
		$("#backgroundPopup").fadeOut("slow");
		$("#popupDettaglio").fadeOut("slow");
		popupStatus = 0;

		var jsonObject = jsonObjectCopy;

		$.each(jsonObject.listFreccia, function(index, freccia)  
				 {
					$('#tf'+index).remove();
				 });
		$('#dt_table_freccia').show();

        $.each(jsonObject.listIV, function(index, iv)  
	    		{
	        		$('#tiv'+index).remove();
				 });
        $('#dt_table_iv').show();

		$.each(jsonObject.listIci, function(index, ici)  
	    		{	
	    			$('#tici'+index).remove();
				 });
		$('#dt_table_ici').show();	
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
function centerPopup(dimBol){
	//request data for centering
	
	var windowDim = getWindowSize();
//	var popupHeight = 300 * numBol;
	var popupHeight = dimBol;

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

function showDettaglio(url) {
	populatePopup(url);
//	alert('numero bollettini = ' + numeroBol);
//    centerPopup(numeroBol);
//    centerPopup();
    loadPopup();
}

var jsonObjectCopy;

function populatePopup(url) {
	var url = url + '&csrfToken=' +$("input[name=csrfToken]").val();

	$.getJSON(url, function(jsonObject) {
		jsonObjectCopy = jsonObject;
		//Giulia 2/07/2013
		var canalePagamento = jsonObject.beanTransazioni.beanTransazioni.canale_pagamento;
		var codAutorizzazioneBanca = jsonObject.beanTransazioni.beanTransazioni.codice_autorizzazione_banca;
		var codIdBanca = jsonObject.beanTransazioni.beanTransazioni.codice_identificativo_banca;
		
		
        $('#dt_chiave_transazione').html(jsonObject.beanTransazioni.beanTransazioni.chiave_transazione).show();
         if (canalePagamento == "TOT" || canalePagamento == "LOT" || canalePagamento == "SIS")
        //if (canalePagamento == "TSL")
          {
        	$('#dt_chiave_transazione_Atm_Totem').html("id Atm:");
        	$('#dt_chiave_transazione_Atm').html(codAutorizzazioneBanca + "-" + codIdBanca).show();
          }
        else
			$('#dt_chiave_transazione_Atm').html("").show();
		$('#dt_importo_totale_transazione').html(jsonObject.beanTransazioni.beanTransazioni.importo_totale_transazione).show();
		$('#dt_numeromav_transazione').html(jsonObject.beanTransazioni.beanTransazioni.codice_ordine_gateway).show();
		$('#dt_codice_autorizzazione_banca').html(jsonObject.beanTransazioni.beanTransazioni.codice_autorizzazione_banca).show();
		$('#dt_data_effettivo_pagamento_su_gateway').html(jsonObject.beanTransazioni.beanTransazioni.data_effettivo_pagamento_su_gateway).show();
		$('#dt_note_transazione').html(jsonObject.beanTransazioni.beanTransazioni.note_generiche).show();
		$('#dt_flag_riversamento_automatico').html(jsonObject.beanTransazioni.beanTransazioni.flag_riversamento_automatico == 'Y' ? "Si" : "No").show();
		$('#dt_id_terminale_pos').html(jsonObject.beanTransazioni.beanTransazioni.id_terminale_pos).show();
		var codiceIUV="";
	    //inizio LP PG190220
		var dimAnnullo = 0;
		var dt_stato_annullo = "";
		var dt_email_annullo = "";
		var dt_data_email_annullo = "";
		var rt = "";
	    //fine LP PG190220
		 $.each(jsonObject.listIV, function(index, iv)  {
			    codiceIUV = iv.nodoSpcIuv;
			    //inizio LP PG190220
			    if (iv.nodoSpcErEsito == "OK") {
			    	dt_stato_annullo = "Si";
			    	rt = iv.nodoSpcRt;
			    	if(rt.length == 0) {
				    	dt_stato_annullo = "Attesa RT";
			    	}
			    	if(iv.nodoSpcEsitoInvioRevocaEmailContribuente == "Y") {
			    		dt_email_annullo = "Inviata";
			    		dt_data_email_annullo = iv.nodoSpcDataInvioRevocaEmailContribuente;
			    	} else {
			    		dt_email_annullo = "Non Inviata";
			    	}
			    }
			    //fine LP PG190220
	        });
	   //inizio LP PG190220
	   if(codiceIUV == "") {
	   //fine LP PG190220
		 $.each(jsonObject.listFreccia, function(index, fre)  {
			    codiceIUV = fre.nodoSpcIuv;
	        });
	   //inizio LP PG190220
	   }
	   if(codiceIUV == "") {
	   //fine LP PG190220
		 $.each(jsonObject.listIci, function(index, ici)  {
			    codiceIUV = ici.nodoSpcIuv;
	        });
	   //inizio LP PG190220
	   }
	   //fine LP PG190220
		 $('#dt_id_iuv').html(codiceIUV);
		//inizio LP PG190220
		if(dt_stato_annullo != "") {
			dimAnnullo = 30;
			$('#dt_stato_annulloL').html("Annullo:").show();
			$('#dt_stato_annullo').html(dt_stato_annullo).show();
			$('#dt_email_annulloL').html("Email Annullo:").show();
			$('#dt_email_annullo').html(dt_email_annullo).show();
			$('#dt_data_email_annulloL').html("Data Invio:").show();
			$('#dt_data_email_annullo').html(dt_data_email_annullo).show();
		} else {
			$('#dt_stato_annulloL').html("").hide();
			$('#dt_stato_annullo').html("").hide();
			$('#dt_email_annulloL').html("").hide();
			$('#dt_email_annullo').html("").hide();
			$('#dt_data_email_annulloL').html("").hide();
			$('#dt_data_email_annullo').html("").hide();
		}
		//fine LP PG190220
		
		if (canalePagamento == "CCD" || canalePagamento == "POS")
			$('#dt_operatore').html(jsonObject.beanTransazioni.beanTransazioni.operatore_inserimento).show();
		else
			$('#dt_operatore').html("").show();
		if (canalePagamento == "POS")
			$('#dt_operatore_intestazione').html("Operatore POS fisico:").show();
		else
			$('#dt_operatore_intestazione').html("Operatore Call Center Dispositivo:").show();
		
		
		//		var importo_costo = parseFloat((parseFloat(jsonObject.beanTransazioni.beanTransazioni.importo_costo_transazione) + parseFloat(jsonObject.beanTransazioni.beanTransazioni.importo_costo_spese_di_notifica)).toFixed(2));

        var numBol = jsonObject.listFreccia.length + jsonObject.listIci.length + jsonObject.listIV.length;
        var dimICI = 0;
        $.each(jsonObject.listIci, function(index, ici)  
        	{	
             var tipoBollICI = ici.tipo_bollettino.substring(0,3);
             switch (tipoBollICI) 
            	{
                 case 'ICI': dimICI = dimICI + 280 + 50;break;
                 case 'ISC': dimICI = dimICI + 240 + 50;break;
            	}
        	});
        
        var dimTFR = 150 + 50;

        
        var dimTDT = 0;
        $.each(jsonObject.listIV, function(index, iv)  
        	{
            var tipoBollIV = iv.tipo_bollettino.substring(0,3);
            switch (tipoBollIV) 
	            {
	                case 'BOL': dimTDT = dimTDT + 220 + 50;break;
	                case 'SPO': dimTDT = dimTDT + 160 + 50;break;
	                case 'CDS': dimTDT = dimTDT + 200 + 50;break;
	                case 'MAV': dimTDT = dimTDT + 160 + 50;break;
	                case 'PRE': dimTDT = dimTDT + 200 + 50;break;
	            }
        	});

        //dimensione lista oneri (se presente)
        var dimOneri = 0;
        if (jsonObject.listDettaglioOnere != null && jsonObject.listDettaglioOnere.length > 0)
        	dimOneri = jsonObject.listDettaglioOnere.length * 43 + 64; //64 = intestazione, 43 = riga doppia (se il testo va a capo una sola volta)
        	
        var dimBol = 138 + 50 + jsonObject.listFreccia.length * dimTFR + dimICI + dimTDT + dimOneri; // + 50; (dim per STampa)
 	   //inizio LP PG190220
        dimBol = dimBol + dimAnnullo; 
  	   //fine LP PG190220

        
//        alert('dim tfr = ' + dimTFR + ' dim ici = ' + dimICI + ' dim dtd = ' + dimTDT + ' dim totale = ' + dimBol);

        centerPopup(dimBol);
        
		var importo_costo_tra = jsonObject.beanTransazioni.beanTransazioni.importo_costo_transazione.replace(',','.');
		var importo_costo_not = jsonObject.beanTransazioni.beanTransazioni.importo_costo_spese_di_notifica.replace(',','.');
		var importo_costo = parseFloat(importo_costo_tra) + parseFloat(importo_costo_not);

	    $('#dt_importo_costo_transazione').html(number_format(importo_costo,2,',','.')).show();

// aggiunta in modifica
	    $('#dt_id_banca').html(jsonObject.beanTransazioni.beanTransazioni.codice_identificativo_banca);
	    $('#dt_autorizzazione_banca').html(jsonObject.beanTransazioni.beanTransazioni.codice_autorizzazione_banca);

	   /* 
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

            $('#fr_ente').html(freccia.descrizione_ente + ' ' + freccia.descrizione_ufficio);
    		$('#fr_tipologia_servizio').html(freccia.codice_tipologia_servizio + ' ' + freccia.descrizione_tipologia_servizio);

            $('#dt_table_freccia').clone(true).attr('id','').appendTo('#div_table_freccia');


	    });
        $('#dt_table_freccia').hide();
	    */

	    
	    $.each(jsonObject.listFreccia, function(index, freccia)  {
            var clone = $('#dt_table_freccia').clone(true);
            $('#fr_importo_totale_bollettino',clone).html(freccia.importo_totale_bollettino);
            $('#fr_chiave_spedizione',clone).html(freccia.chiave_spedizione.length==0?'No':'Si');
            $('#fr_codice_identificativo_pagamento',clone).html(freccia.codice_identificativo_pagamento);
            $('#fr_motivo_del_pagamento',clone).html(freccia.motivo_del_pagamento);
            $('#fr_codice_iban',clone).html(freccia.codice_iban);
            $('#fr_descrizione_intestatario_conto_corrente',clone).html(freccia.descrizione_intestatario_conto_corrente);
            $('#fr_codice_freccia',clone).text(freccia.codice_freccia).html();
            $('#fr_codice_servizio',clone).html(freccia.codice_servizio);
            $('#fr_eseguito_da',clone).html(freccia.denominazione_contribuente+' '+freccia.indirizzo+' '+freccia.codice_cap+' '+freccia.citta+' '+freccia.provincia);

            $('#fr_ente',clone).html(freccia.descrizione_ente + ' ' + freccia.descrizione_ufficio);
    		$('#fr_tipologia_servizio',clone).html(freccia.codice_tipologia_servizio + ' ' + freccia.descrizione_tipologia_servizio);
            clone.attr('id', 'tf'+index).appendTo('#div_table_freccia');
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

            $('#iv_ente',clone).html(iv.descrizione_ente + ' ' + iv.descrizione_ufficio);
    		$('#iv_tipologia_servizio',clone).html(iv.codice_tipologia_servizio + ' ' + iv.descrizione_tipologia_servizio);
            
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
                
                // &deg;
                
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
            clone.attr('id', 'tiv'+index).appendTo('#div_table_iv');
        });
        $('#dt_table_iv').hide();
// ICI controllato
        $.each(jsonObject.listIci, function(index, ici)  {	

            var clone = $('#dt_table_ici').clone(true);
            var tipoBollettino;
            var codiceBollettino = ici.tipo_bollettino.substring(0,3);
            switch (codiceBollettino) {
                case 'ICI': tipoBollettino = 'Bollettino IMIS';break;
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
            
            $('#ici_ente',clone).html(ici.descrizione_ente + ' ' + ici.descrizione_ufficio);
    		$('#ici_tipologia_servizio',clone).html(ici.codice_tipologia_servizio + ' ' + ici.descrizione_tipologia_servizio);
            
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
                $('#ici_interessi_ici_sanzioni',clone).html(ici.interessi_ici_sanzioni);
                
                
               }
//            $('#ici_codice_fiscale',clone).html(ici.codice_fiscale);
//            $('#ici_flag_ravvedimento',clone).html(ici.flag_ravvedimento=='N'?'No':'Si');
//            $('#ici_versamento_acconto',clone).html((ici.flag_rata == '0' || ici.flag_rata == '3')?'Si':'No');
//            $('#ici_versamento_saldo',clone).html((ici.flag_rata == '1' || ici.flag_rata == '3')?'Si':'No');
            $('#ici_codice_servizio',clone).html(ici.codice_servizio);
            $('#ici_denominazione',clone).html(ici.denominazione.replace("|"," ")+' '+ici.indirizzo_domicilio_fiscale+' '+ici.domicilio_fiscale);
            clone.attr('id', 'tici'+index).appendTo('#div_table_ici');
        });
        $('#dt_table_ici').hide();

        //dettaglio oneri
        if (jsonObject.listDettaglioOnere != null && jsonObject.listDettaglioOnere.length > 0)
        {
            var colLabels = "Ente Destinatario Onere$$Beneficiario<br />[Cod.Utente - Cod.Ente - Tp.Uff - Cod.Uff]$$Importo Totale$$Importo Contabile Ingresso Totale$$Importo Contabile Uscita Totale$$Causale".split("$$");
            var keys = "descrizioneEntePortaleEsterno$$beneficiario$$importoTotale$$importoContabileIngresso$$importoContabileUscita$$causale".split("$$");
            var thcssclass= " $$ $$text_align_right$$text_align_right$$text_align_right$$ ".split("$$");
            var tdcssclass= " $$ $$text_align_right$$text_align_right$$text_align_right$$ ".split("$$");
            $('#div_tableoneri').html(ConvertJsonToTable(jsonObject.listDettaglioOnere,
                    colLabels,
                    keys,
                    thcssclass,
                    tdcssclass,
                    "tableoneri",
                    "seda-ui-datagrid"));
            $('#div_tableoneri_outer').show();
         }
         else
            $('#div_tableoneri_outer').hide();
        
	});
}

function ConvertJsonToTable(jsonData, colLabels, keys, thcssclass, tdcssclass, containerId, tableClassName) {    
	//Patterns for table thead & tbody    
	var tbl = "<table border=\"1\" cellpadding=\"0\" cellspacing=\"0\" id=\"" + containerId + "\" class=\"" + tableClassName + "\">{0}{1}</table>";    
	var th = "<thead>{0}</thead>";    
	var tb = "<tbody>{0}</tbody>";    
	var tr = "<tr>{0}</tr>";    
	var thRow = "<th class=\"seda-ui-datagridheadercell {0}\">{1}</th>";    
	var tdRow = "<td class=\"seda-ui-datagridcell {0}\">{1}</td>";    
	var thCon = "";    
	var tbCon = "";    
	var trCon = "";    
	//gli array di colLabel e keys devono avere la stessa lunghezza
	if (colLabels && keys && jsonData && (colLabels.length == keys.length)) {        
		//Creating all table headers        
		for (i = 0; i < colLabels.length; i++) {            
			thCon += thRow.replace("{1}", colLabels[i]).replace("{0}",thcssclass[i]);        
		}        
		th = th.replace("{0}",tr.replace("{0}",thCon));        
		//Creating all table rows from Json data        
		if (typeof(jsonData[0]) == "object") {           
			for (i = 0; i < jsonData.length; i++) {                
				for (j = 0; j < keys.length; j++) {                    
					tbCon += tdRow.replace("{1}",jsonData[i][keys[j]]).replace("{0}",tdcssclass[j]);                
				}                
				trCon += tr.replace("{0}",tbCon);                
				tbCon = "";            
			}        
		}        
		tb = tb.replace("{0}",trCon);        
		tbl = tbl.replace("{0}",th).replace("{1}", tb); //format(th, tb);        
		return tbl;    
	}    
	return null;
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

function setPopupForPrint(){
    //imposto il css per la stampa 
    //NB:non lo imposto fisso da jsp altrimenti non è più possibile stampare la lista risultati, ma solo e sempre la popup!
    var linkCss = document.getElementById('printdetailtransazione_css');
    if (linkCss != null)
        linkCss.href = "../applications/templates/monitoraggio/css/popup_print.css";
    
    if(popupStatus==1)
    {
	    $("#popupDettaglio").css({
		    "position": "absolute",
		    "top": 0,
		    "left": 0
	    });
    }
}