<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form method="post" action="create">
            <c:forEach var="breed" items="${breedList}" varStatus="loop">
                <a href="${breed.key}"><c:out value="${breed.value}" /></a>
            </c:forEach>
            <input type="submit" value="Сохранить" />
        </form>
    </body>
</html>
