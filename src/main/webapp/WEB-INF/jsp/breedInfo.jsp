<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${breedName}</title>
    </head>
    <body>
        <h1><c:out value="${breedName}" /></h1>
        Для этой породы характерны следующие особенности тела: 
        <c:forEach var="prop" items="${breedBodyProp}" >
            <p>
                <c:out value="${prop.key}: " /> 
                <c:forEach var="propValue" items="${prop.value}" varStatus="loop">
                    <c:out value="${fn:toLowerCase(propValue)}" />
                    <c:if test="${!loop.last}" >,  </c:if>
                    <c:if test="${loop.last}">.</c:if>
                </c:forEach>
            </p>
        </c:forEach>
        Для этой породы характерны следующие особенности личности: 
        <c:forEach var="prop" items="${breedPersonalityProp}" >
            <p>
                <c:out value="${prop.key}: " /> 
                <c:forEach var="propValue" items="${prop.value}" varStatus="loop">
                    <c:out value="${fn:toLowerCase(propValue)}" />
                    <c:if test="${!loop.last}" >,  </c:if>
                    <c:if test="${loop.last}">.</c:if>
                </c:forEach>
            </p>
        </c:forEach>
    </body>
</html>
