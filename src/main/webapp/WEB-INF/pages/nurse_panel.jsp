<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Panel</title>
        <link href="<c:url value="/resources/styles/bootstrap.min.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/styles/style.css" />" rel="stylesheet">
        <link href="<c:url value="/resources/styles/nursepanel.css" />" rel="stylesheet">    
    </head>
    <body>
        <header class="col-xs-12">
            <nav class="col-xs-4 col-xs-offset-2">
                <p class="navbar-text navbar-left"><a href="#" class="navbar-link">HOME</a></p>
                <p class="navbar-text navbar-left"><a href="#" class="navbar-link">PANEL</a></p>
            </nav>
            <p class="navbar-text navbar-right"><a href="<c:url value="j_spring_security_logout" />" class="navbar-link">LOGOUT</a></p>
        </header>
        <main class="col-xs-12">
        <div class="col-xs-4 col-xs-offset-2 main_content">
            <a href="#" title=""><img src="http://swiatlosekunda.blog.pl/files/2013/09/skywalker-star-wars.jpg" class="img-circle avatar"></a>
            <h2>Imie i nazwisko</h2>
            <h4>email@gmail.com</h4>
            4/4<br>
            czas pracy
        </div>
        <div class="col-xs-4 col-xs-offset-1 main_content">
            <button type="button" class="btn btn-danger btn-block" id="generatedSchedule" >
            <h4>SEE SCHEDULE</h4>
            <h6>01.01.2012-01.02.2012</h6></button>
            <textarea class="form-control" rows="6" placeholder="Sent special request about schedule for next time"></textarea>
            <button type="button" class="btn btn-danger btn-block send_button">SEND</button>
        </div>
        </main>
    </body>
</html>