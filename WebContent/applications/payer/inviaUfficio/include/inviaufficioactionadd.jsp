<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="inviaufficioactionadd" encodeAttributes="true" />

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
        $("#tx_data_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
        $("#tx_data_da_hidden").datepicker( {
            minDate: new Date(annoDa, 0, 1),
            maxDate: new Date(annoA, 11, 31),
            yearRange: annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#tx_data_da_day_id").val(dateText.substr(0, 2));
                $("#tx_data_da_month_id").val(dateText.substr(3, 2));
                $("#tx_data_da_year_id").val(dateText.substr(6, 4));
            },
            beforeShow: function(input, inst) {
                //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                updateValoreDatePickerFromDdl("tx_data_da_day_id",
                    "tx_data_da_month_id",
                    "tx_data_da_year_id",
                    "tx_data_da_hidden");
            }
        });
        $("#tx_data_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
        $("#tx_data_a_hidden").datepicker( {
            minDate: new Date(annoDa, 0, 1),
            maxDate: new Date(annoA, 11, 31),
            yearRange: annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#tx_data_a_day_id").val(dateText.substr(0, 2));
                $("#tx_data_a_month_id").val(dateText.substr(3, 2));
                $("#tx_data_a_year_id").val(dateText.substr(6, 4));
            },
            beforeShow: function(input, inst) {
                //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                updateValoreDatePickerFromDdl("tx_data_a_day_id",
                    "tx_data_a_month_id",
                    "tx_data_a_year_id",
                    "tx_data_a_hidden");
            }
        });


        $("#tx_data_obbl_da_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
        $("#tx_data_obbl_da_hidden").datepicker( {
            minDate: new Date(annoDa, 0, 1),
            maxDate: new Date(annoA, 11, 31),
            yearRange: annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#tx_data_obbl_da_day_id").val(dateText.substr(0, 2));
                $("#tx_data_obbl_da_month_id").val(dateText.substr(3, 2));
                $("#tx_data_obbl_da_year_id").val(dateText.substr(6, 4));
            },
            beforeShow: function(input, inst) {
                //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                updateValoreDatePickerFromDdl("tx_data_obbl_da_day_id",
                    "tx_data_obbl_da_month_id",
                    "tx_data_obbl_da_year_id",
                    "tx_data_obbl_da_hidden");
            }
        });
        $("#tx_data_obbl_a_hidden").datepicker("option", "dateFormat", "dd/mm/yyyy");
        $("#tx_data_obbl_a_hidden").datepicker( {
            minDate: new Date(annoDa, 0, 1),
            maxDate: new Date(annoA, 11, 31),
            yearRange: annoDa + ":" + annoA,
            showOn : "button",
            buttonImage : "../applications/templates/shared/img/calendar.gif",
            buttonImageOnly : true,
            onSelect : function(dateText, inst) {
                $("#tx_data_obbl_a_day_id").val(dateText.substr(0, 2));
                $("#tx_data_obbl_a_month_id").val(dateText.substr(3, 2));
                $("#tx_data_obbl_a_year_id").val(dateText.substr(6, 4));
            },
            beforeShow: function(input, inst) {
                //imposta il valore del calendario in base a quanto impostato nelle 3 dropdownlist
                updateValoreDatePickerFromDdl("tx_data_obbl_a_day_id",
                    "tx_data_obbl_a_month_id",
                    "tx_data_obbl_a_year_id",
                    "tx_data_obbl_a_hidden");
            }
        });


    });
</script>


<s:form name="inviaufficioactionaddForm" action="${do_command_name}"
        method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">

    <c:if test="${codop == 'add'}">
        <s:div name="divRicercaTitleName" cssclass="divRicTitle ">AGGIUNGI PARAMETRI</s:div>
        <input type="hidden" name="prova" value=0 />
    </c:if>

    <s:div name="divElement1" cssclass="divRicMetadatiLeft">
        <s:dropdownlist name="flagSubentro"
                        disable="false"
                        label="Batch : "
                        cssclasslabel="label85 bold textright"
                        cssclass="tbddl"
                        cachedrowset="" usexml="true"
                        validator="ignore" showrequired="true"
                        valueselected="${flagSubentro}">
            <s:ddloption  value="UFF" text="Chiusura di Ufficio"/>
        </s:dropdownlist>
    </s:div>

    <s:div name="divElement200" cssclass="divRicMetadatiRight">
    <s:div name="divElement180a" cssclass="labelData">
        <s:label name="label_data"
                 cssclass="seda-ui-label label85 bold textright"
                 text="Parametri" />
    </s:div>

        <s:div name="divElement12b" cssclass="divRicMetadatiCenter">
            <s:div name="dtscadenza" cssclass="divDataDa">
                <s:date label="Data Scadenza:" cssclasslabel="labelsmall"
                        cssclass="dateman" prefix="dtFlusso_da"
                        yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
                        locale="IT-it" descriptivemonth="false" separator="/"
                        calendar="${dtFlusso_da}">
                </s:date>
                <input type="hidden" id="dtFlusso_da_hidden" value="" />
            </s:div>

            <s:div name="dtconferma" cssclass="divDataA">
                <s:date label="Data Conferma:" cssclasslabel="labelsmall" cssclass="dateman"
                        prefix="dtFlusso_a" yearbegin="${ddlDateAnnoDa}"
                        yearend="${ddlDateAnnoA}" locale="IT-it"
                        descriptivemonth="false" separator="/"
                        calendar="${dtFlusso_a}">
                </s:date>
                <input type="hidden" id="dtFlusso_a_hidden" value="" />
            </s:div>
        </s:div>
    </s:div>
    <br/>
    <s:div name="divRicercaBottoni" cssclass="divRicBottoni">
        <s:button id="tx_button_aggiungi" onclick="" text="Aggiungi" type="submit" cssclass="btnStyle" />
        <s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
        <s:button id="tx_button_indietro" validate="false" onclick="" text="Indietro" type="submit" cssclass="btnStyle" />
    </s:div>

    <s:div name="div_messaggi" cssclass="div_align_center">
        <c:if test="${!empty tx_message}">
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


</s:form>



