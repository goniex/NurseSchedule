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
        <link href="<c:url value="/resources/styles/adminpanel.css" />" rel="stylesheet">
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
        <div class="col-xs-12">
            <div class="col-xs-4 col-xs-offset-2">
                <div class="col-xs-5">
                    <a href="#" title=""><img src="http://swiatlosekunda.blog.pl/files/2013/09/skywalker-star-wars.jpg" class="img-circle avatar"></a>
                </div>
                <div class="col-xs-5 col-xs-offset-2">
                    <h2>Admin</h2>
                    <h4>nazwa@gmail.com</h4>
                </div>
            </div>
            <div class="col-xs-3 col-xs-offset-1">
                <button type="button" class="btn btn-danger btn-block">GENERATE NEW</button>
                <button type="button" class="btn btn-danger btn-block">
                <h5>SEE SCHEDULE</h5>
                <h6>01.01.2012-01.02.2012</h6></button>
            </div>
        </div>
        <div class="col-xs-10 col-xs-offset-1">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <td>NAME/EMAIL</td>
                        <td>ETAT</td>
                        <td>SCHEDULE</td>
                        <td>DELETE</td>
                        <td>EDIT</td>
                    </tr>
                    <tr>
                        <td><h4>Imie Nazwisko</h4><h6>adres@gmail.com</h6></td>
                        <td>4/4</td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></a></td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a></td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></td>
                    </tr>
                    <tr>
                        <td><h4>Imie Nazwisko</h4><h6>adres@gmail.com</h6></td>
                        <td>4/4</td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></a></td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a></td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></td>
                    </tr>
                    <tr>
                        <td><h4>Imie Nazwisko</h4><h6>adres@gmail.com</h6></td>
                        <td>4/4</td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></a></td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a></td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></td>
                    </tr>
                    <tr>
                        <td><h4>Imie Nazwisko</h4><h6>adres@gmail.com</h6></td>
                        <td>4/4</td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-calendar" aria-hidden="true"></span></a></td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-trash" aria-hidden="true"></span></a></td>
                        <td><a href="#" title=""><span class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a></td>
                    </tr>
                </thead>
            </table>
        </div>
        <div class="col-xs-12">
            <div class="col-xs-3">
                <input type="text" class="form-control" placeholder="nurse name">
            </div>
            <div class="col-xs-3">
                <input type="text" class="form-control" placeholder="nurse lastname">
            </div>
            <div class="col-xs-2">
                <input type="email" class="form-control" placeholder="nazwa@gmail.com">
            </div>
            <div class="col-xs-2">
                <input type="number" class="form-control" placeholder="job time">
            </div>
            <div class="col-xs-2">
                <button type="button" class="btn btn-danger btn-block">GENERATE NEW</button>
            </div>
        </div>
        </main>
    </body>
</html>