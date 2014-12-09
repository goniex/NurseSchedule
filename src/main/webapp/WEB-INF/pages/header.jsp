<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<sec:authentication var="principal" property="principal" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Panel</title>      
        <link href="<c:url value="/resources/lib/css/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/adminpanel.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/css/nursepanel.css" />" rel="stylesheet">   
    </head>
	<body>
        <header class="col-xs-12 panel">
            <nav class="col-xs-4 col-xs-offset-2">
                <p class="navbar-text navbar-left"><a href="#" class="navbar-link">HOME</a></p>
                <p class="navbar-text navbar-left"><a href="#" class="navbar-link">PANEL</a></p>
            </nav>
            <p class="navbar-text navbar-right"><a href="<c:url value="j_spring_security_logout" />" class="navbar-link">LOGOUT</a></p>
            <p class="navbar-text navbar-right">${principal.username}</p>
        </header>
     
     <div class="container">