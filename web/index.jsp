<%--
  Created by IntelliJ IDEA.
  User: zhj63
  Date: 2020/7/25
  Time: 23:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>正在跳转</title>
</head>
<body>
<%
    request.getRequestDispatcher("/index").forward(request,response);
%>
</body>
</html>
