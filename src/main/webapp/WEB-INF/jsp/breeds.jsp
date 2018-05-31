<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="jstl/custom_tag_library.tld" prefix="custom" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta http-equiv="x-ua-compatible" content="ie=edge">
        <title>Породы</title>
        <!-- Font Awesome -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <!-- Bootstrap core CSS -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
        <!-- Material Design Bootstrap -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.5.0/css/mdb.min.css" rel="stylesheet">
        <link href="resources/css/style-custom.css" rel="stylesheet">
    </head>
    <body>
        <nav class="navbar navbar-expand-lg navbar-dark blue fixed-top ">
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
            <div class="col-md-12 animated slideInDown">
                <c:forEach var="breedName" items="${breedList}" >
                    <h3><c:out value="${custom:getEntityNameRu(breedName)}" /></h3>
                    <p>${custom:getEntityCommentRu(breedName)}</p>
                    <a href="breed/${breedName}">
                        <button type="button" class="btn btn-outline-info btn-sm float-right">
                            О породе
                        </button>
                    </a>
                        <br><br><hr class="hr-style-custom">
                </c:forEach>
            </div>
        </div>
            
            
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
