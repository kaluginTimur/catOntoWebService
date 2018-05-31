<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="jstl/custom_tag_library.tld" prefix="custom" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>CatOnto</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <!-- Bootstrap core CSS -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
        <!-- Material Design Bootstrap -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.5.0/css/mdb.min.css" rel="stylesheet">
        <link href="resources/css/style-custom.css" rel="stylesheet">
    </head>
    
    <body>
        <nav class="navbar navbar-expand-sm navbar-dark blue fixed-top ">
            <div class="container-fluid">
                <img src="resources/img/catonto/cat-icon-header.png">
                <a class="navbar-brand" href="#">CatOnto Web Service</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" aria-expanded="false">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item active">
                        <a class="nav-link" href="${pageContext.request.contextPath}">На главную
                            <span class="sr-only">(current)</span>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="breeds">Породы</a>
                    </li>
                </ul>
                <form class="form-inline">
                    <div class="md-form mt-0">
                        <input class="form-control mr-sm-2" type="text" placeholder="Поиск..." aria-label="Search">
                    </div>
                </form>
            </div>
        </nav>
                            
        <div class="container">
            <form method="post" action="findBreed">
                <div class="form-row">
                    <h5 class="form-group col-md-6 animated slideInDown mb-3">Выберите желаемые физические характеристики</h5>
                    <h5 class="form-group col-md-6 animated slideInDown mb-3">Выберите желаемые черты личности</h5>
                </div>
                <div class="form-row">
                    <div class="form-group col-md-6 animated slideInLeft mb-3 pre-scrollable" style="max-height: 450px"> 
                        <c:forEach var="bodyProp" items="${breedBodyProperties}">
                            <p>
                                <c:out value="${custom:getEntityNameRu(bodyProp.key)}" />:
                                <c:if test="${empty bodyProp.value}">
                                    <input class="form-control" name="${bodyProp.key}" type="number" placeholder="Введите значение" />
                                </c:if>
                                <c:if test="${!empty bodyProp.value}">  
                                    <select class="form-control" name="${bodyProp.key}" <c:if test="${bodyProp.key == 'hasBody'}">multiple size="3"</c:if>>
                                        <c:if test="${bodyProp.key != 'hasBody'}"><option value="">Выберите значение</option></c:if>
                                        <c:forEach var="propValue" items="${bodyProp.value}">
                                            <option value="${propValue}"><c:out value="${custom:getEntityNameRu(propValue)}" /></option>
                                        </c:forEach>
                                    </select>
                                </c:if>
                            </p>
                        </c:forEach>
                    </div>
                    <div class="form-group col-md-6 animated slideInRight mb-3 pre-scrollable" style="max-height: 450px">
                        
                        <c:forEach var="personProp" items="${breedPersonProperties}">
                            <p>
                                <c:out value="${custom:getEntityNameRu(personProp.key)}" />:   
                                <select class="form-control" name="${personProp.key}" <c:if test="${fn:contains(personProp.key, 'TemperTrait')}">multiple size="3"</c:if>>
                                    <c:if test="${!fn:contains(personProp.key, 'TemperTrait')}"><option value="">Выберите значение</option></c:if>
                                    <c:forEach var="propValue" items="${personProp.value}">
                                        <option value="${propValue}"><c:out value="${custom:getEntityNameRu(propValue)}" /></option>
                                    </c:forEach>
                                </select>
                            </p>
                        </c:forEach>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12 animated flipInY mb-3">
                        <div class="text-center">
                            <input class="btn btn-outline-blue mb-2" type="submit" value="Найти подходящую породу" />
                        </div>
                    </div>
                </div>
            </form>
        </div>
        
        <c:if test="${breedsFinded != null}"> 
            <div id="searchResultModal" class="modal fade in" role="dialog">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Результаты поиска</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <c:if test="${empty breedsFinded}">
                                <div class="list-group">
                                    <div class="d-flex w-100 justify-content-between">
                                        <p>К сожалению, ни одной подходящей породы не найдено :(</p>
                                        <img src="resources/img/catonto/cat-icon-search.png">
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${!empty breedsFinded}">
                                <div class="list-group">
                                    <c:forEach var="breedsMap" items="${breedsFinded}">
                                        <a href="breed/${breedsMap.key}" class="list-group-item list-group-item-action flex-column align-items-start">
                                            <div class="d-flex w-100 justify-content-between">
                                                <h5 class="mb-1"><c:out value="${custom:getEntityNameRu(breedsMap.key)}" /></h5>
                                            </div>
                                            <p class="mb-1">вам подходит по следующим параметрам:</p>
                                            <c:forEach var="breedsProperty" items="${breedsMap.value}">
                                                <small><mark>
                                                        <c:out value="${fn:toLowerCase(custom:getEntityNameRu(breedsProperty))}" />
                                                    </mark>;</small>
                                                </c:forEach>
                                        </a>
                                    </c:forEach>
                                </div>
                            </c:if>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                        </div>
                    </div>
                </div>
            </div>           
        </c:if>
                            
                            
        <footer class="page-footer font-small blue darken-3 mt-4">
            <div class="container">
                <div class="row">
                    <div class="col-md-12 py-5">
                        <div class="mb-5 flex-center">
                            <img src="resources/img/catonto/cat-icon-footer.png">
                        </div>
                    </div>
                </div>
            </div>
            <div class="footer-copyright text-center py-3">
                Icons made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from 
                <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a> 
                is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0" target="_blank">CC 3.0 BY</a>
                /
                <a href="https://mdbootstrap.com/bootstrap-tutorial/">Designed with help MDBootstrap.com</a>
            </div>
            <div class="footer-copyright text-center py-3">© 2018 Copyright / CatOnto</div>
        </footer>
                         
        <!-- JQuery -->
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <!-- Bootstrap tooltips -->
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.13.0/umd/popper.min.js"></script>
        <!-- Bootstrap core JavaScript -->
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0/js/bootstrap.min.js"></script>
        <!-- MDB core JavaScript -->
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.5.0/js/mdb.min.js"></script>
        <script type="text/javascript" src="resources/js/catontojs.js"></script>
    </body>
</html>
