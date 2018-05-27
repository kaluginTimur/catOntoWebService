<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Породы кошек</title>
    </head>
    <body>
        <c:out value="${breedList.key.entrySet()}" />
        <c:forEach var="breed" items="${breedList}" >
            <p><a href="breed/${breed.key}"><c:out value="${breed.value}" /></a></p>
        </c:forEach>
    </body>
</html>
