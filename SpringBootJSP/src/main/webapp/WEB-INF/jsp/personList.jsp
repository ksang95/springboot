<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/main.css" />
</head>
<body>
<h1>Person List</h1>
<br><br>
<div>
<table border="1">
<tr>
<th>First Name</th>
<th>Last Name</th>
</tr>
<c:forEach items="${persons}" var="person">
<tr>
<td>${person.firstName}</td>
<td>${person.lastName}</td>
</tr>
</c:forEach>
</table>
</div>
</body>
</html>