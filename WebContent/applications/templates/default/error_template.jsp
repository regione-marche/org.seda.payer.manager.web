<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/tld/maftags.tld" prefix="m" %>
<%@ taglib uri="/WEB-INF/tld/Seda.tld" prefix="s" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="../applications/templates/shared/css/shared.css">
<link rel="stylesheet" type="text/css" href="../applications/templates/shared/css/seda.css">
<link rel="stylesheet" type="text/css" href="../applications/templates/shared/css/style.css">

<%--Gestione ridimensionamento testo (A+, A-) --%>
<link id="fontsize_id" href="../applications/templates/shared/css/fontNormalSize.css" rel="stylesheet" type="text/css">
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/fontSizeFunctions.js" type="text/javascript" ></script>

<title><template:include parameter="pagetitle"/></title>
<link rel="icon"  href="../applications/templates/shared/${sessionScope.template}/img/favicon.ico"  type="image/x-icon"  />
</head>
<body>
	<div id="container" class="containerGreen">
		<div id="divHeader"><template:include parameter="header"/> </div>
		<div id="divContent"><template:include parameter="content"/> </div>
		<template:include parameter="footer"/>
	</div>	
</body>
</html>