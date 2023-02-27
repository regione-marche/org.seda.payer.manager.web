<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<m:view_state id="seusers" encodeAttributes="true" />
<br/>
	<s:div name="divCentered" cssclass="div_align_center divSelezione">
		<s:form name="form_indietro"
			action="ritorna.do?vista=seusers_search" method="post"
			hasbtn1="false" hasbtn2="false" hasbtn3="false">
			<s:button id="button_indietro" onclick="" text="Indietro" cssclass="btnStyle"
				type="submit" />
		</s:form>
	</s:div>
		
