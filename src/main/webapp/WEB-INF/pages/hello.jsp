<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
	<h1>${message}</h1>
	<a href="<c:url value="j_spring_security_logout" />" > Logout</a>
</body>
</html>