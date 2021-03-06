<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<sec:authentication var="principal" property="principal" />
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Panel</title>      
        <link href="<c:url value="/resources/lib/css/bootstrap.min.css" />" rel="stylesheet"/>
        <link href="<c:url value="/resources/css/style.css" />" rel="stylesheet"/>
        <link href="<c:url value="/resources/css/adminpanel.css" />" rel="stylesheet"/>
        <link href="<c:url value="/resources/lib/css/angular-bootstrap-calendar.min.css"/>" rel="stylesheet"/>

        <script src="<c:url value="/resources/lib/js/angular.min.js"/>"></script>
        <script src="<c:url value="/resources/lib/js/angular-resource.min.js"/>"></script>
        <script src="<c:url value="/resources/lib/js/ui-bootstrap-tpls-0.12.0.min.js"/>"></script>
        <script src="<c:url value="/resources/lib/js/moment-with-locales.min.js"/>"></script>
        <script src="<c:url value="/resources/lib/js/angular-bootstrap-calendar-tpls.min.js"/>"></script>

        <script src="<c:url value="/resources/lib/ng-app/app.js"/>"></script>
        <script src="<c:url value="/resources/lib/ng-app/controllers/adminPanelCtrl.js"/>"></script>
        <script src="<c:url value="/resources/lib/ng-app/controllers/nursePanelCtrl.js"/>"></script>
        <script src="<c:url value="/resources/lib/ng-app/services/nurseService.js"/>"></script>
        <script src="<c:url value="/resources/lib/ng-app/services/scheduleService.js"/>"></script>
        <script src="<c:url value="/resources/lib/ng-app/directives/nsBlock.js"/>"></script>

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
     
     <div class="container" ng-app="nurseApp">
         <ns-block ng-show="isPending"></ns-block>
