<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="bollettino" encodeAttributes="true" />


<c:if test="${bollettini != null}">
	<s:datagrid cachedrowset="bollettini" border="0" rowperpage="${applicationScope.rowsPerPage}"
		action="bollettino.do?action=search&vista=bollettino"
		usexml="true" viewstate="true">

		<s:dgcolumn index="1" label="Tipo Bollettino" />
		<s:dgcolumn index="2" label="Descrizione Tipo Bollettino" />
		<s:dgcolumn label="Tipo Compilazione" >
			<s:if left="{3}" control="eq" right="M">
				<s:then>Manuale</s:then>
			</s:if>
			<s:if left="{3}" control="eq" right="A">
				<s:then>Automatica</s:then>
			</s:if>
			<s:if left="{3}" control="neq" right="M" operator="and" 
				secondleft="{3}" secondcontrol="neq" secondright="A">
				<s:then>{3}</s:then>
			</s:if>
		</s:dgcolumn>
		<s:dgcolumn label="Azioni">
			<s:hyperlink cssclass="hlStyle"
				href="bollettino.do?action=edit&bollettino_bollettinoType={1}"
				imagesrc="../applications/templates/configurazione/img/edit.png"
				alt="Modifica" text="" />

			<s:hyperlink cssclass="hlStyle"
				href="bollettino.do?action=richiestacanc&richiestacanc=y&bollettinoType={1}"
				imagesrc="../applications/templates/configurazione/img/cancel.png"
				alt="Cancella" text="" />


		</s:dgcolumn>
	</s:datagrid>
</c:if>

