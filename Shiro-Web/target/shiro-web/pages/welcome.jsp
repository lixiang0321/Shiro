<%@ page import="java.util.Enumeration" %>
<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>
<head>
    <title>Shiro认证授权管理框架</title>
    <jsp:include page="/pages/plugins/include_basepath.jsp"/>
</head>
<body>
<%
    Enumeration<String> names = session.getAttributeNames();
    while (names.hasMoreElements()) {
        String name = names.nextElement() ;
        out.println("<h2>" + name + " = " + session.getAttribute(name) + "</h2>");
    }
%>
<shiro:authenticated>
    <h3>你已经登录过了</h3>
</shiro:authenticated>
<shiro:notAuthenticated>
    <h3>你没登录</h3>
</shiro:notAuthenticated>
<h1>用户登录成功，欢迎“<shiro:principal/>”光临，系统<a href="logout.servlet">注销</a>！</h1>
<shiro:hasRole name="member">
    <h3>当前的用户拥有member角色</h3>
</shiro:hasRole>
<shiro:hasPermission name="member:add">
    <h3>当前用户拥有member:add权限</h3>
</shiro:hasPermission>
</body>
</html>
