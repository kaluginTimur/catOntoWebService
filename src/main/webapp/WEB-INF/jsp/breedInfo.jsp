<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ taglib uri="jstl/custom_tag_library.tld" prefix="custom" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>${custom:getEntityNameRu(breedName)}</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <!-- Bootstrap core CSS -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
        <!-- Material Design Bootstrap -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.5.0/css/mdb.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/resources/css/style-custom.css" rel="stylesheet">
        <link href="https://cdnjs.cloudflare.com/ajax/libs/baguettebox.js/1.10.0/baguetteBox.min.css" rel="stylesheet" />
        <link href="${pageContext.request.contextPath}/resources/css/compact-gallery.css" rel="stylesheet">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark blue fixed-top ">
            <div class="container-fluid">
                <img src="${pageContext.request.contextPath}/resources/img/catonto/cat-icon-header.png">
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
                        <a class="nav-link" href="${pageContext.request.contextPath}/breeds">Породы</a>
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
            <div class="row animated fadeIn">
                <div class="card">
                    <div class="card-header">
                        <c:out value="${custom:getEntityNameRu(breedName)}" />
                    </div>
                    <div class="card-body">
                        <blockquote class="blockquote mb-0">
                            <p><c:out value="${custom:getEntityCommentRu(breedName)}" /></p>
                            <c:if test="${breedOrigin != ''}">
                                <footer class="blockquote-footer">
                                    Происхождение - <cite title="Source Title">${custom:getEntityNameRu(breedOrigin)}</cite>
                                </footer>
                            </c:if>
                        </blockquote>
                    </div>
                </div>
            </div>
                     
            <div class="row-fluid animated fadeIn" style="padding-top:50px; animation-delay: 1s">
                <div class="card text-center">
                    <div class="card-header">
                        <ul class="nav nav-tabs card-header-tabs">
                            <li class="nav-item animated fadeIn">
                                <a class="nav-link active" data-toggle="tab" href="#body">Характерные физические характеристики</a>
                            </li>
                            <li class="nav-item animated fadeIn">
                                <a class="nav-link" data-toggle="tab" href="#personality">Характерные личностные характеристики</a>
                            </li>
                        </ul>
                    </div>
                    <div class="tab-content clearfix">
                        <div class="tab-pane animated fadeIn active" id="body">
                            <div class="card-body">
                                <c:forEach var="prop" items="${breedBodyProp}" >
                                    <div class="row">
                                        <div class="col-sm">
                                            <ul class="list-group list-group-flush">
                                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                                    <c:out value="${custom:getEntityNameRu(prop.key)}: " />
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="col-sm">
                                            <ul class="list-group">
                                                <li class="list-group-item d-flex align-items-center">
                                                    <c:forEach var="propValue" items="${prop.value}" varStatus="loop">
                                                        <h5>
                                                            <span class="badge badge-primary">
                                                                <c:out value="${fn:toLowerCase(custom:getEntityNameRu(propValue))}" />
                                                            </span>&nbsp;
                                                        </h5>    
                                                    </c:forEach>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="tab-pane animated fadeIn" id="personality">
                            <div class="card-body">
                                <c:forEach var="prop" items="${breedPersonalityProp}" >
                                    <div class="row">
                                        <div class="col-sm">
                                            <ul class="list-group list-group-flush">
                                                <li class="list-group-item d-flex justify-content-between align-items-center">
                                                    <c:out value="${custom:getEntityNameRu(prop.key)}: " /> 
                                                </li>
                                            </ul>
                                        </div>
                                        <div class="col-sm">
                                            <ul class="list-group">
                                                <li class="list-group-item d-flex align-items-center">
                                                    <c:forEach var="propValue" items="${prop.value}" varStatus="loop">
                                                        <h5>
                                                            <span class="badge badge-primary">
                                                                <c:out value="${fn:toLowerCase(custom:getEntityNameRu(propValue))}" />
                                                            </span>&nbsp;
                                                        </h5>   
                                                    </c:forEach>
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <c:if test="${!empty breedImages}">
            <section class="gallery-block compact-gallery animated fadeInUp">
                <div class="container">
                    <div class="heading">
                        <h2>Галерея</h2>
                    </div>
                    <div class="row no-gutters">
                        <c:forEach var="path" items="${breedImages}" >
                            <div class="col-md-6 col-lg-4 item zoom-on-hover">
                                <a class="lightbox" href="${pageContext.request.contextPath}/resources/img/catonto/cats/${breedName}/${path}">
                                    <img class="img-fluid image" src="${pageContext.request.contextPath}/resources/img/catonto/cats/${breedName}/${path}">
                                </a>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </section> 
        </c:if> 
            
        <footer class="page-footer font-small blue darken-3 mt-4">
            <div class="container">
                <div class="row">
                    <div class="col-md-12 py-5">
                        <div class="mb-5 flex-center">
                            <img src="${pageContext.request.contextPath}/resources/img/catonto/cat-icon-footer.png">
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
        <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/catontojs.js"></script>  
        <script src="https://cdnjs.cloudflare.com/ajax/libs/baguettebox.js/1.10.0/baguetteBox.min.js"></script>
        <script>
            baguetteBox.run('.compact-gallery', { animation: 'slideIn'});
        </script>
    </body>
</html>
