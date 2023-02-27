<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s"%>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m"%>
<s:div name="divMessage" cssclass="ad_display_block">
	<c:if test="${!empty tx_message}">
		<hr />
		<s:label name="tx_message" text="${tx_message}" />
		<hr />
	</c:if>
	<c:if test="${!empty tx_error_message}">
		<hr />
		<s:label name="tx_error_message" text="${tx_error_message}" cssclass="ad_color_red" />
		<hr />
	</c:if>
</s:div>
