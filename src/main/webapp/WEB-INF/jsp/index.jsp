<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form method="post" action="findBreed">
            Выберите желаемые физические характеристики:
            <c:forEach var="bodyProp" items="${breedBodyProperties}">
                <p>
                    <c:set var="propertyMap" value="${bodyProp.key.entrySet().iterator().next()}"/>
                    <c:out value="${propertyMap.value}" />: 
                    <c:if test="${empty bodyProp.value}">
                        <input name="${propertyMap.key}" type="number" placeholder="Введите значение" />
                    </c:if>
                    <c:if test="${!empty bodyProp.value}">    
                        <select name="${propertyMap.key}" <c:if test="${propertyMap.key == 'hasBody'}">multiple size="2"</c:if>>
                            <c:if test="${propertyMap.key != 'hasBody'}"><option value="">Выберите значение</option></c:if>
                            <c:forEach var="valueMap" items="${bodyProp.value}">
                                <option value="${valueMap.key}"><c:out value="${valueMap.value}" /></option>
                            </c:forEach>
                        </select>
                    </c:if>
                </p>
            </c:forEach>
            Выберите желаемые черты личности:
            <c:forEach var="personProp" items="${breedPersonProperties}">
                <p>
                    <c:set var="propertyMap" value="${personProp.key.entrySet().iterator().next()}"/>
                    <c:out value="${propertyMap.value}" />:    
                    <select name="${propertyMap.key}" <c:if test="${fn:contains(propertyMap.key, 'TemperTrait')}">multiple size="2"</c:if>>
                        <c:if test="${!fn:contains(propertyMap.key, 'TemperTrait')}"><option value="">Выберите значение</option></c:if>
                        <c:forEach var="valueMap" items="${personProp.value}">
                            <option value="${valueMap.key}"><c:out value="${valueMap.value}" /></option>
                        </c:forEach>
                    </select>
                </p>
            </c:forEach>
            <input type="submit" value="Найти подходящую породу" />
        </form>
        <c:if test="${breedsFinded != null}">
            <c:if test="${empty breedsFinded}"><p>К сожалению, ни одной подходящей породы не найдено :(</p></c:if>
            <c:if test="${!empty breedsFinded}">
                Результаты поиска:
                <c:forEach var="breedsMap" items="${breedsFinded}">
                    <c:set var="breedNameMap" value="${breedsMap.key.entrySet().iterator().next()}"/>
                    <p>
                        <a href="breed/${breedNameMap.key}"><c:out value="${breedNameMap.value}" /></a>
                        вам подходит по следующим параметрам:
                        <c:forEach var="breedsProperty" items="${breedsMap.value}">
                            <c:out value="${fn:toLowerCase(breedsProperty)}" />;
                        </c:forEach>
                    </p>
                    </c:forEach>
            </c:if>
        </c:if>
    </body>
</html>
