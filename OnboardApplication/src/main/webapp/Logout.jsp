<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Logout</title>
</head>
<body>
    <h1>Logout</h1>
    <form action="${pageContext.request.contextPath}/logout" method="post">
        <input type="submit" value="Logout">
    </form>
</body>
</html>
