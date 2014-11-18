<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Title of the document</title>
<link href="<c:url value="/resources/styles/bootstrap.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/styles/style.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/styles/site.css" />"
	rel="stylesheet">
<meta name="viewport" content="width=device-width, initial-scale=1">

<script type="text/javascript">
	function hideErrorMessage() {
		document.getElementById("error").style.display = "none";
	}
	
	function hideLogoutMessage() {
		document.getElementById("logout").style.display = "none";
	}

	function startTimer() {
		var error = window.setTimeout("hideErrorMessage()", 5000);
		var logout = window.setTimeout("hideLogoutMessage()", 5000);
	}
</script>

</head>
<body onload="startTimer()">
	<header class="col-xs-12">
	<div id="header_up">
		<h1>PERFECT SCHEDULE</h1>
		<h4>IT'S ENOUGH TO MAKE YOU FEEL OF LIFE</h4>
		<button type="button" class="btn btn-danger">FIND OUT MORE</button>
	</div>
	<div id="header_down">
		<div class="col-xs-12">
			<h2>ABOUT NURSE SCHEDULING</h2>
			Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do
			eiusmod tempor incididunt ut labore et dolore magna aliqua.
		</div>
		<div class="col-xs-4">
			<h2>Perfect schedule</h2>
			Lorem ipsum dolor sit amet, consectetur adipisicing elit,
		</div>
		<div class="col-xs-4">
			<h2>Share with staff</h2>
			Lorem ipsum dolor sit amet, consectetur adipisicing elit,
		</div>
	</div>
	<div class="col-xs-4">
		<h2>Personalize</h2>
		Lorem ipsum dolor sit amet, consectetur adipisicing elit,
	</div>
	</div>
	</div>
	</header>
	<main class="col-xs-12">
	<div class="col-xs-4 col-xs-offset-2">
		<h3>
			<b>READY TO TEST</b>
		</h3>
		<h4>
			Request for beta account! <br> Leaving Your Email address and
			wait for credentials!<br>
		</h4>
		<div class="col-xs-8 col-xs-offset-2">
			<input type="email" class="form-control formField" id="emailTest"
				placeholder="sample@email.com">
			<button type="button" class="btn btn-danger btn-block formField">FIND
				OUT MORE</button>
		</div>
		<div class="checkbox">
			<label> <input type="checkbox"> <a href="#"
				title="ATerm">Accepte Terms</a>
			</label>
		</div>
	</div>
	<div class="col-xs-4">
		<h3>
			<b>SIGN IN</b>
		</h3>
		Sign in and test our schedule<br>
		<form method="post" action="j_spring_security_check">
			<input name="j_username" type="email" class="form-control formField"
				id="emailSignIn" placeholder="sample@email.com"> <input
				name="j_password" type="password" class="form-control formField"
				id="passSignIn" placeholder="Password"> <input
				class="btn btn-danger btn-block formField button" name="submit"
				type="submit" value="FIND OUT MORE" />

			<c:if test="${param.error}">
				<div id="error">Login or password is incorrect!</div>
			</c:if>
			
			<c:if test="${param.logout}">
				<div id="logout">You've been logged out successfully.</div>
			</c:if>

		</form>

	</div>
	</main>
	<footer class="col-xs-12">
	<h4>Please contact with: email@contact.com</h4>
	All Rights reserved <br>
	This is only school project </footer>
</body>
</html>