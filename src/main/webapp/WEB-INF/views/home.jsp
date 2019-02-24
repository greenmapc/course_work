<%--
  Created by IntelliJ IDEA.
  User: matveymaletskov
  Date: 2019-02-21
  Time: 10:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
<h1>Hello, simple web-app</h1>

<p>The time on the server is ${serverTime}.</p>

<form action="testUser" method="post">
    <input type="text" name="firstName" value=""/>
    <br>
    <input type="text" name="lastName" value=""/>
    <br>
    <input type="submit" value="Login"/>
</form>
</body>
</html>