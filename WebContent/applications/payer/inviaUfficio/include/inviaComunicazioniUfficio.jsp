<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="inviaufficio" encodeAttributes="true" />

<script src="../applications/js/jquery-min.js"
        type="text/javascript"></script>
<script src="../applications/js/jquery-ui-custom.min.js"
        type="text/javascript"></script>
<script src="../applications/js/i18n/jquery.ui.datepicker-it.js"
        type="text/javascript"></script>

<script type="text/javascript">

    function setFiredButton(buttonName) {
        var buttonFired = document.getElementById('fired_button_hidden');
        if (buttonFired != null)
            buttonFired.value = buttonName;
    }

    var annoDa = ${ddlDateAnnoDa};
    var annoA = ${ddlDateAnnoA};
    var today = new Date();
    $(function() {
        $.datepicker.setDefaults($.datepicker.regional["it"]);
        $("#dtFlusso_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
        $("#dtFlusso_da_hidden").datepicker(
            {
                minDate : new Date(annoDa, 0, 1),
                maxDate : new Date(annoA, 11, 31),
                yearRange : annoDa + ":" + annoA,
                showOn : "button",
                buttonImage : "../applications/templates/shared/img/calendar.gif",
                buttonImageOnly : true,
                onSelect : function(dateText, inst) {
                    $("#dtFlusso_da_day_id").val(dateText.substr(0, 2));
                    $("#dtFlusso_da_month_id").val(dateText.substr(3, 2));
                    $("#dtFlusso_da_year_id").val(dateText.substr(6, 4));
                },
                beforeShow : function(input, inst) {
                    //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                    updateValoreDatePickerFromDdl(
                        "dtFlusso_da_day_id",
                        "dtFlusso_da_month_id",
                        "dtFlusso_da_year_id",
                        "dtFlusso_da_hidden");
                }
            });
        $("#dtFlusso_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
        $("#dtFlusso_a_hidden").datepicker(
            {
                minDate : new Date(annoDa, 0, 1),
                maxDate : new Date(annoA, 11, 31),
                yearRange : annoDa + ":" + annoA,
                showOn : "button",
                buttonImage : "../applications/templates/shared/img/calendar.gif",
                buttonImageOnly : true,
                onSelect : function(dateText, inst) {
                    $("#dtFlusso_a_day_id").val(dateText.substr(0, 2));
                    $("#dtFlusso_a_month_id").val(dateText.substr(3, 2));
                    $("#dtFlusso_a_year_id").val(dateText.substr(6, 4));
                },
                beforeShow : function(input, inst) {
                    //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                    updateValoreDatePickerFromDdl(
                        "dtFlusso_a_day_id",
                        "dtFlusso_a_month_id",
                        "dtFlusso_a_year_id",
                        "dtFlusso_a_hidden");
                }
            });
        $("#dtregol_da_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
        $("#dtregol_da_hidden").datepicker(
            {
                minDate : new Date(annoDa, 0, 1),
                maxDate : new Date(annoA, 11, 31),
                yearRange : annoDa + ":" + annoA,
                showOn : "button",
                buttonImage : "../applications/templates/shared/img/calendar.gif",
                buttonImageOnly : true,
                onSelect : function(dateText, inst) {
                    $("#dtregol_da_day_id").val(dateText.substr(0, 2));
                    $("#dtregol_da_month_id").val(dateText.substr(3, 2));
                    $("#dtregol_da_year_id").val(dateText.substr(6, 4));
                },
                beforeShow : function(input, inst) {
                    //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                    updateValoreDatePickerFromDdl(
                        "dtregol_da_day_id",
                        "dtregol_da_month_id",
                        "dtregol_da_year_id",
                        "dtregol_da_hidden");
                }
            });
        $("#dtregol_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
        $("#dtregol_a_hidden").datepicker(
            {
                minDate : new Date(annoDa, 0, 1),
                maxDate : new Date(annoA, 11, 31),
                yearRange : annoDa + ":" + annoA,
                showOn : "button",
                buttonImage : "../applications/templates/shared/img/calendar.gif",
                buttonImageOnly : true,
                onSelect : function(dateText, inst) {
                    $("#dtregol_a_day_id").val(dateText.substr(0, 2));
                    $("#dtregol_a_month_id").val(dateText.substr(3, 2));
                    $("#dtregol_a_year_id").val(dateText.substr(6, 4));
                },
                beforeShow : function(input, inst) {
                    //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                    updateValoreDatePickerFromDdl(
                        "dtregol_a_day_id",
                        "dtregol_a_month_id",
                        "dtregol_a_year_id",
                        "dtregol_a_hidden");
                }
            });
        $("#dtChiusuraFlusso_da_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
        $("#dtChiusuraFlusso_da_hidden").datepicker(
            {
                minDate : new Date(annoDa, 0, 1),
                maxDate : new Date(annoA, 11, 31),
                yearRange : annoDa + ":" + annoA,
                showOn : "button",
                buttonImage : "../applications/templates/shared/img/calendar.gif",
                buttonImageOnly : true,
                onSelect : function(dateText, inst) {
                    $("#dtChiusuraFlusso_da_day_id").val(dateText.substr(0, 2));
                    $("#dtChiusuraFlusso_da_month_id").val(dateText.substr(3, 2));
                    $("#dtChiusuraFlusso_da_year_id").val(dateText.substr(6, 4));
                },
                beforeShow : function(input, inst) {
                    //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                    updateValoreDatePickerFromDdl(
                        "dtChiusuraFlusso_da_day_id",
                        "dtChiusuraFlusso_da_month_id",
                        "dtChiusuraFlusso_da_year_id",
                        "dtChiusuraFlusso_da_hidden");
                }
            });
        $("#dtChiusuraFlusso_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
        $("#dtChiusuraFlusso_a_hidden").datepicker(
            {
                minDate : new Date(annoDa, 0, 1),
                maxDate : new Date(annoA, 11, 31),
                yearRange : annoDa + ":" + annoA,
                showOn : "button",
                buttonImage : "../applications/templates/shared/img/calendar.gif",
                buttonImageOnly : true,
                onSelect : function(dateText, inst) {
                    $("#dtChiusuraFlusso_a_day_id").val(dateText.substr(0, 2));
                    $("#dtChiusuraFlusso_a_month_id").val(dateText.substr(3, 2));
                    $("#dtChiusuraFlusso_a_year_id").val(dateText.substr(6, 4));
                },
                beforeShow : function(input, inst) {
                    //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                    updateValoreDatePickerFromDdl(
                        "dtChiusuraFlusso_a_day_id",
                        "dtChiusuraFlusso_a_month_id",
                        "dtChiusuraFlusso_a_year_id",
                        "dtChiusuraFlusso_a_hidden");
                }
            });

        $("#dtPeriodo_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
        $("#dtPeriodo_da_hidden").datepicker({
            minDate : new Date(annoDa, 0, 1),
            maxDate : new Date(annoA, 11, 31),
            yearRange : annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#dtPeriodo_da_day_id").val(dateText.substr(0, 2));
                $("#dtPeriodo_da_month_id").val(dateText.substr(3, 2));
                $("#dtPeriodo_da_year_id").val(dateText.substr(6, 4));
            },
            beforeShow : function(input, inst) {
                //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                updateValoreDatePickerFromDdl(
                    "dtPeriodo_da_day_id",
                    "dtPeriodo_da_month_id",
                    "dtPeriodo_da_year_id",
                    "dtPeriodo_da_hidden");
            }
        });

        $("#dtPeriodo_a_hidden").datepicker("option", "dateFormat","dd/mm/yyyy");
        $("#dtPeriodo_a_hidden").datepicker({
            minDate : new Date(annoDa, 0, 1),
            maxDate : new Date(annoA, 11, 31),
            yearRange : annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#dtPeriodo_a_day_id").val(dateText.substr(0, 2));
                $("#dtPeriodo_a_month_id").val(dateText.substr(3, 2));
                $("#dtPeriodo_a_year_id").val(dateText.substr(6, 4));
            },
            beforeShow : function(input, inst) {
                //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                updateValoreDatePickerFromDdl(
                    "dtPeriodo_a_day_id",
                    "dtPeriodo_a_month_id",
                    "dtPeriodo_a_year_id",
                    "dtPeriodo_a_hidden");
            }
        });
    });

    function setFiredButton(buttonName) {
        var buttonFired = document.getElementById('fired_button_hidden');
        if (buttonFired != null)
            buttonFired.value = buttonName;
    }

    function setDdl(value) {
        var txtHidden = document.getElementById('DDLChanged');
        if (txtHidden != null)
            txtHidden.value = value;
    }

    function callSubmit(frm1) {
        frm1.submit();
    }

    function getCSSRule(ruleName, deleteFlag) { // Return requested style obejct
        ruleName = ruleName.toLowerCase(); // Convert test string to lower case.
        if (document.styleSheets) { // If browser can play with stylesheets
            for ( var i = 0; i < document.styleSheets.length; i++) { // For each stylesheet
                var styleSheet = document.styleSheets[i]; // Get the current Stylesheet
                var ii = 0; // Initialize subCounter.
                var cssRule = false; // Initialize cssRule.
                do { // For each rule in stylesheet
                    if (styleSheet.cssRules) { // Browser uses cssRules?
                        cssRule = styleSheet.cssRules[ii]; // Yes --Mozilla Style
                    } else { // Browser usses rules?
                        cssRule = styleSheet.rules[ii]; // Yes IE style.
                    } // End IE check.
                    if (cssRule) { // If we found a rule...
                        if (cssRule.selectorText.toLowerCase() == ruleName) { //  match ruleName?
                            if (deleteFlag == 'delete') { // Yes.  Are we deleteing?
                                if (styleSheet.cssRules) { // Yes, deleting...
                                    styleSheet.deleteRule(ii); // Delete rule, Moz Style
                                } else { // Still deleting.
                                    styleSheet.removeRule(ii); // Delete rule IE style.
                                } // End IE check.
                                return true; // return true, class deleted.
                            } else { // found and not deleting.
                                return cssRule; // return the style object.
                            } // End delete Check
                        } // End found rule name
                    } // end found cssRule
                    ii++; // Increment sub-counter
                } while (cssRule); // end While loop
            } // end For loop
        } // end styleSheet ability check
        return false; // we found NOTHING!
    } // end getCSSRule

    function killCSSRule(ruleName) { // Delete a CSS rule
        return getCSSRule(ruleName, 'delete'); // just call getCSSRule w/delete flag.
    } // end killCSSRule

    function addCSSRule(ruleName) { // Create a new css rule
        if (document.styleSheets) { // Can browser do styleSheets?
            if (!getCSSRule(ruleName)) { // if rule doesn't exist...
                if (document.styleSheets[0].addRule) { // Browser is IE?
                    document.styleSheets[0].addRule(ruleName, null, 0); // Yes, add IE style
                } else { // Browser is IE?
                    document.styleSheets[0].insertRule(ruleName + ' { }', 0); // Yes, add Moz style.
                } // End browser check
            } // End already exist check.
        } // End browser ability check.
        return getCSSRule(ruleName); // return rule we just created.
    }

    /*
    function switchVisibility() {
       var rule =  getCSSRule('.display_none');
       if (rule.style.display == 'none') {
           rule.style.display = 'inline';
       } else {
           rule.style.display = 'none';
       }
    }
     */
</script>

<c:url value="" var="formParameters">
    <c:if test="${!empty param.pageNumber}">
        <c:param name="pageNumber_hidden">${param.pageNumber}</c:param>
    </c:if>
    <c:if test="${!empty rowsPerPage}">
        <c:param name="rowsPerPage_hidden">${param.rowsPerPage}</c:param>
    </c:if>
    <c:if test="${!empty orderBy}">
        <c:param name="orderBy_hidden">${param.orderBy}</c:param>
    </c:if>
    <c:if test="${!empty param.tx_societa}">
        <c:param name="tx_societa">${param.tx_societa}</c:param>
    </c:if>
    <c:if test="${!empty param.tx_provincia}">
        <c:param name="tx_provincia">${param.tx_provincia}</c:param>
    </c:if>
    <c:if test="${!empty param.tx_utente}">
        <c:param name="tx_utente">${param.tx_utente}</c:param>
    </c:if>

    <c:if test="${!empty param.dtFlusso_da_day}">
        <c:param name="dtFlusso_da_day">${param.dtFlusso_da_day}</c:param>
    </c:if>
    <c:if test="${!empty param.dtFlusso_da_month}">
        <c:param name="dtFlusso_da_month">${param.dtFlusso_da_month}</c:param>
    </c:if>
    <c:if test="${!empty param.dtFlusso_da_year}">
        <c:param name="dtFlusso_da_year">${param.dtFlusso_da_year}</c:param>
    </c:if>
    <c:if test="${!empty param.dtFlusso_a_day}">
        <c:param name="dtFlusso_a_day">${param.dtFlusso_a_day}</c:param>
    </c:if>
    <c:if test="${!empty param.dtFlusso_a_month}">
        <c:param name="dtFlusso_a_month">${param.dtFlusso_a_month}</c:param>
    </c:if>
    <c:if test="${!empty param.dtFlusso_a_year}">
        <c:param name="dtFlusso_a_year">${param.dtFlusso_a_year}</c:param>
    </c:if>

    <c:if test="${!empty param.dtPeriodo_da_day}">
        <c:param name="dtPeriodo_da_day">${param.dtPeriodo_da_day}</c:param>
    </c:if>
    <c:if test="${!empty param.dtPeriodo_da_month}">
        <c:param name="dtPeriodo_da_month">${param.dtPeriodo_da_month}</c:param>
    </c:if>
    <c:if test="${!empty param.dtPeriodo_da_year}">
        <c:param name="dtPeriodo_da_year">${param.dtPeriodo_da_year}</c:param>
    </c:if>
    <c:if test="${!empty param.dtPeriodo_a_day}">
        <c:param name="dtPeriodo_a_day">${param.dtPeriodo_a_day}</c:param>
    </c:if>
    <c:if test="${!empty param.dtPeriodo_a_month}">
        <c:param name="dtPeriodo_a_month">${param.dtPeriodo_a_month}</c:param>
    </c:if>
    <c:if test="${!empty param.dtPeriodo_a_year}">
        <c:param name="dtPeriodo_a_year">${param.dtPeriodo_a_year}</c:param>
    </c:if>
</c:url>


<s:div name="divSelezione1" cssclass="divSelezione">
    <s:div name="divRicercaTopName" cssclass="divRicercaTop">
        <s:form name="inviaufficio"
                action="inviaufficio.do" method="post"
                hasbtn1="false" hasbtn2="false" hasbtn3="false">
            <s:textbox name="DDLChanged" label="DDLChanged" bmodify="true" text="" cssclass="display_none" cssclasslabel="display_none" />


            <s:div name="divRicercaTitleName" cssclass="divRicTitle bold">Ricerca - Richieste Elaborazioni</s:div>
            <s:div name="divRicMetadati" cssclass="divRicMetadati">


                <s:div name="divRicercaLeft" cssclass="divRicMetadatiLeft">

                    <s:div name="divElement12" cssclass="divRicMetadatiDoubleRow">
                        <s:div name="divElement12a" cssclass="labelData">
                            <s:label name="label_data_creazione"
                                     cssclass="seda-ui-label label78 bold textright"
                                     text="Data Richiesta" />
                        </s:div>

                        <s:div name="divElement12b" cssclass="floatleft">
                            <s:div name="divDtFlussoDA" cssclass="divDataDa">
                                <s:date label="Da:" cssclasslabel="labelsmall"
                                        cssclass="dateman" prefix="dtFlusso_da"
                                        yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
                                        locale="IT-it" descriptivemonth="false" separator="/"
                                        calendar="${dtFlusso_da}">
                                </s:date>
                                <input type="hidden" id="dtFlusso_da_hidden" value="" />
                            </s:div>

                            <s:div name="divDtFlussoA" cssclass="divDataA">
                                <s:date label="A:" cssclasslabel="labelsmall" cssclass="dateman"
                                        prefix="dtFlusso_a" yearbegin="${ddlDateAnnoDa}"
                                        yearend="${ddlDateAnnoA}" locale="IT-it"
                                        descriptivemonth="false" separator="/"
                                        calendar="${dtFlusso_a}"></s:date>
                                <input type="hidden" id="dtFlusso_a_hidden" value="" />
                            </s:div>
                        </s:div>
                    </s:div>
                </s:div>

                <s:div name="divRicercaCenter" cssclass="divRicMetadatiCenter"></s:div>
                <s:div name="divRicercaRight" cssclass="divRicMetadatiRight">
                    <s:div name="divElement8" cssclass="divRicMetadatiSingleRow">
                        <s:dropdownlist name="statoElaborazione" cssclass="tbddl floatleft"
                                        cssclasslabel="label85 bold floatleft textright" disable="false"
                                        label="Stato elaborazione:" valueselected="${statoElaborazione}">
                            <s:ddloption value="" text="Tutte le prenotazioni" />
                            <s:ddloption value="0" text="Elaborata" />
                            <s:ddloption value="1" text="Da elaborare" />
                        </s:dropdownlist>
                    </s:div>
                </s:div>
            </s:div>

            <s:div name="divCentered0" cssclass="divRicBottoni">

                <s:button id="tx_button_cerca" type="submit" text="Cerca" onclick="" cssclass="btnStyle" />
                <s:button id="tx_button_reset" type="submit" text="Reset" onclick="" cssclass="btnStyle" />
                <s:button id="tx_button_nuovo" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
            </s:div>
        </s:form>
    </s:div>
</s:div>

${tx_message}
${tx_error_message}
<s:div name="div_messaggi" cssclass="div_align_center">
    <c:if test="${!empty c}">
        <s:div name="div_messaggio_info">
            <hr />
            <s:label name="tx_message" text="${tx_message}" />
            <hr />
        </s:div>
    </c:if>
    <c:if test="${!empty tx_error_message}">
        <s:div name="div_messaggio_errore">
            <hr />
            <s:label name="tx_error_message" text="${tx_error_message}" />
            <hr />
        </s:div>
    </c:if>
</s:div>

<c:if test="${!empty listaInputUfficio}">
    <fmt:setLocale value="it_IT" />
    <s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
        Elenco Richieste Elaborazioni
    </s:div>
    <s:datagrid viewstate="" cachedrowset="listaInputUfficio" action="" border="1" usexml="true">
        <s:dgcolumn index="4" label="Data Richiesta" format="dd/MM/yyyy"></s:dgcolumn>
        <s:dgcolumn index="8" label="Data scadenza" css="text_align_right"></s:dgcolumn>
        <s:dgcolumn index="9" label="Data da Impostare" css="text_align_right"></s:dgcolumn>
        <s:dgcolumn index="6" label="Stato" css="text_align_right"></s:dgcolumn>
        <s:dgcolumn label="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Azioni&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;">
        <s:if right="{6}" control="eq" left="Da elaborare">
            <s:then>
                <s:hyperlink href="inviaufficio.do${formParameters}&button_elimina=DELETE&stato={6}&dataimp={9}&datascad={8}&dataric={4}&ente={3}&utente={2}&chiave={11}" imagesrc="../applications/templates/shared/img/cancel.png" alt="cancella" text="" cssclass="hlStyle" />
            </s:then>
            <s:else>
            </s:else>
        </s:if>
        </s:dgcolumn>
    </s:datagrid>
</c:if>
