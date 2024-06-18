<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="inviauff" encodeAttributes="true" />

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

<s:form name="inviauffForm" action="inviauff.do" method="post" hasbtn1="false" hasbtn2="false" hasbtn3="false">


    <s:div name="divElement2" cssclass="divRicMetadatiCenter">
        <s:div name="divElement18a" cssclass="labelData">
            <s:label name="label_data"
                     cssclass="seda-ui-label label85 bold textright"
                     text="Data Richiesta" />
        </s:div>

        <s:div name="divElement18b" cssclass="floatleft">
            <s:div name="divDataDa" cssclass="divDataDa">
                <s:date label="Data:" prefix="tx_data_da"
                        yearbegin="${ddlDateAnnoDa}" yearend="${ddlDateAnnoA}"
                        locale="IT-it" descriptivemonth="false" separator="/"
                        calendar="${tx_data_da}" cssclasslabel="labelsmall"
                        cssclass="dateman">
                </s:date>
                <input type="hidden" id="tx_data_da_hidden" value="" />
            </s:div>
        </s:div>
    </s:div>


    <s:div name="divElement1" cssclass="divRicMetadatiLeft">
        <s:dropdownlist name="flagstato"
                        disable="false"
                        label="Stato Prenotazione:"
                        cssclasslabel="label85 bold textright"
                        cssclass="tbddl"
                        cachedrowset="" usexml="true"
                        validator="ignore" showrequired="true"
                        valueselected="${flagSubentro}">
            <s:ddloption text="Tutti" value="" />
            <s:ddloption  value="T" text="Terminata"/>
            <s:ddloption  value="D" text="Da Elaborare"/>
        </s:dropdownlist>
    </s:div>
    <br/>
    <s:div name="divRicercaBottoni" cssclass="divRicBottoni">
        <s:button id="tx_button_cerca" onclick="" text="Cerca" type="submit" cssclass="btnStyle" />
        <s:button id="tx_button_reset" validate="false" onclick="" text="Reset" type="submit" cssclass="btnStyle" />
        <s:button id="tx_button_nuovo" validate="false"  onclick="" text="Nuovo" type="submit" cssclass="btnStyle" />
    </s:div>

    <c:if test="${!empty listaInputUfficio}">
        <fmt:setLocale value="it_IT" />
        <s:div name="divRicercaFillName" cssclass="divRicercaFill bold">
            Elenco Input Bach Invia Ufficio
        </s:div>
        <s:datagrid viewstate="" cachedrowset="listaInputUfficio" action="" border="1" usexml="true">
            <s:dgcolumn index="1" label="Tipo richiesta"></s:dgcolumn>
            <s:dgcolumn index="2" label="Stato" css="text_align_left"></s:dgcolumn>
            <s:dgcolumn index="3" label="Operatore"></s:dgcolumn>
            <s:dgcolumn index="4" label="Data conferma" format="dd/MM/yyyy"></s:dgcolumn>
            <s:dgcolumn index="5" label="Data scadenza" css="text_align_left"></s:dgcolumn>
            <s:dgcolumn index="6" label="Data Richiesta" css="text_align_left"></s:dgcolumn>
        </s:datagrid>
    </c:if>


</s:form>


