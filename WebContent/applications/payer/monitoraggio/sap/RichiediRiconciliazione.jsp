<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="richiediRiconciliazione" encodeAttributes="true" encodeParameters="true"/>

	<s:div name="divMessage" cssclass="seda-ui-divvalidator">
		<c:choose>
			<c:when test="${!empty message}">
				<s:label name="message" text="${message}" cssclass="seda-ui-spanvalidator" />
			</c:when>
			<c:otherwise>
				<s:label name="messageConferma" text="Sei sicuro di voler riconciliare la transazione con il flusso di quadratura fittizio?" cssclass="seda-ui-spanvalidator" />
			</c:otherwise>
		</c:choose>
	</s:div>

	<s:div name="divConfermaStorno" cssclass="divRicBottoni">
		<s:div name="divHlOuter" cssclass="divHlOuter">
			<s:hyperlink name="hlIndietro" text="Indietro" href="../monitoraggio/ritorna.do?vista=monitoraggiotransazioni" cssclass="hlStyleButtonLeft" />
			<c:if test="${tx_button_conferma_riconciliazione_visible}">
				<s:hyperlink name="hlConferma" text="Conferma" 
				href="../monitoraggio/assegnaTranAFittizio.do?tx_codice_transazione_hidden=${tx_codice_transazione_hidden}&vista=monitoraggiotransazioni&tx_button_conferma_riconciliazione=S" 
				cssclass="hlStyleButtonRight" />
			</c:if>
		</s:div>
	</s:div>