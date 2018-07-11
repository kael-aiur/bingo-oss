<%@ page import="net.bingosoft.oss.ssoclient.model.Authentication" %><%--
  Created by IntelliJ IDEA.
  User: KAEL
  Date: 2017/5/16
  Time: 20:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>用户信息</title>
</head>
<body>
    <%= ((Authentication)request.getSession().getAttribute("loginUser")).getUsername() %>
    <br/>
    <a href="<%=request.getContextPath() +"/index.jsp"%>">回到首页</a>
</body>
</html>
