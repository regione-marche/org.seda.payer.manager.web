<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib uri="/WEB-INF/tld/template.tld" prefix="template" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%-- <c:url value="/applications/templates/login/css/login.css" var="url"/> --%>

<link href="../applications/templates/login/css/login.css" rel="stylesheet" type="text/css">
<link href="../applications/templates/shared/css/shared.css" rel="stylesheet" type="text/css">

<%--Gestione ridimensionamento testo (A+, A-) --%>
<link id="fontsize_id" href="../applications/templates/shared/css/fontNormalSize.css" rel="stylesheet" type="text/css">
<script src="../applications/js/jquery-min.js" type="text/javascript"></script>
<script src="../applications/js/fontSizeFunctions.js" type="text/javascript" ></script>

<title><template:include parameter="pagetitle" /></title>
</head>
<body>

	<div id="divHeader"><template:include parameter="header"/></div>
	<div id="content"><template:include parameter="content"/></div>
	<template:include parameter="footer"/>		

</body>
</html>