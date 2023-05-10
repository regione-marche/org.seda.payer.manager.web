<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="../applications/templates/shared/css/seda.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/shared/css/shared.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/shared/css/style.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/ecmanager/css/ecmanager.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/wismanager/css/wismanager.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/adminusers/css/adminusers.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/adminusers/css/jquery.autocomplete.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/shared/css/menu.css" rel="stylesheet" type="text/css">
<link type="text/css" rel="stylesheet"
	href="../applications/templates/shared/css/ui-lightness/jquery-ui-custom.css">
<link type="text/css" rel="stylesheet"
	href="../applications/templates/monitoraggio/css/popup.css">
<link type="text/css" rel="stylesheet"
	href="../applications/templates/riversamento/css/popup.css">

<link href="../applications/templates/entrate/css/entrate.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/monitoraggiocruss/css/monitoraggiocruss.css" rel="stylesheet" type="text/css">

<%--<link id="printdetailtransazione_css" type="text/css" rel="stylesheet" media="print" href="none.css"> --%>




<%--Gestione ridimensionamento testo (A+, A-) --%>
<link id="fontsize_id" href="../applications/templates/shared/css/fontNormalSize.css" rel="stylesheet" type="text/css">
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/fontSizeFunctions.js" type="text/javascript" ></script>
<script src="../applications/js/generics.js" type="text/javascript" ></script>

<script type="text/javascript">
	$(document).ready( function() {
		//rende visibili i controlli del datagrid solo se il javascript è abilitato
		setDataGridControlsVisible();
	});

	function setDataGridControlsVisible() {
		//label 'righe per pagina'
		var lblddlrow = $("label#sedauilabeldgrow");
		if (lblddlrow != null)
			lblddlrow.attr("class", "seda-ui-labeldgrow-visible");
		
		//dropdownlist 'righe per pagina'
		var ddlrow = $("select#sedauiddlrow");
		if (ddlrow != null)
			ddlrow.attr("class", "seda-ui-ddlrow-visible");
		
		//button 'righe per pagina'
		var btnddlrow = $("input#sedauidgbuttonrow");
		if (btnddlrow != null)
			btnddlrow.attr("class", "seda-ui-pagingbtn seda-ui-dgbuttonrow-visible");
		
		//label 'vai a pagina'
		var lblddlpage = $("label#sedauilabeldgpage");
		if (lblddlpage != null)
			lblddlpage.attr("class", "seda-ui-labeldgpage-visible");
			
		//dropdownlist 'vai a pagina'
		var ddlpage = $("select#sedauiddlpage");
		if (ddlpage != null)
			ddlpage.attr("class", "seda-ui-ddlpage-visible");
		
		//button 'vai a pagina'
		var btnddlpage = $("input#sedauidgbuttonpage");
		if (btnddlpage != null)
			btnddlpage.attr("class", "seda-ui-pagingbtn seda-ui-dgbuttonpage-visible");
	}
</script>


<c:if test="${!(sessionScope.googleAnalytics==null)}">
<script type="text/javascript">

  var _gaq = _gaq || [];
  ${sessionScope.googleAnalytics};
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
</c:if>


<title><template:include parameter="pagetitle"/></title>
<link rel="icon"  href="../applications/templates/shared/${sessionScope.template}/img/favicon.ico"  type="image/x-icon"  />
</head>
<body>
	<div id="container" class="containerGreen">
		<div id="divHeader"><template:include parameter="header"/> </div>
		<div id="divMenu"><template:include parameter="menu"/> </div>
		<div id="divMain"><template:include parameter="main"/> <template:include parameter="main2"/><template:include parameter="popup"/></div>
		<div id="divResults"><template:include parameter="results"/></div>
		<div id="divFooter"><template:include parameter="footer"/></div>
	</div>	
</body>
</html>