<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jwds" uri="by.ghoncharko.webproject" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.ghoncharko.webproject.entity.Role"%>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js" integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
<header>
<div class="container-fluide">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="#">
            <img src="<c:url value="${pageContext.request.contextPath}/static/icon.png"/>" width="70" height="65"
                 class="d-inline-block align-top" alt="">
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a class="nav-link" href="/controller?command=/">Home <span class="sr-only">(current)</span></a>
                </li>

                <li class="nav-item">
                    <a class="nav-link" href="/controller?command=preparates&page=1">Preparates</a>
                </li>
                <c:if test="${not empty sessionScope.user && sessionScope.user.role eq Role.CLIENT}">
                    <li class="nav-item">
                        <a class="nav-link" href="/controller?command=recipes">Recipes</a>
                    </li>
                </c:if>
                <c:if test="${not empty sessionScope.user && sessionScope.user.role eq Role.CLIENT}">
                    <li class="nav-item">
                        <a class="nav-link" href="/controller?command=order">Order</a>
                    </li>
                </c:if>
                <c:if test="${not empty sessionScope.user && sessionScope.user.role eq Role.CLIENT}">
                    <li class="nav-item">
                        <a class="nav-link" href="/controller?command=card">Show my bank cards</a>
                    </li>
                </c:if>
                <c:if test="${not empty sessionScope.user && sessionScope.user.role eq Role.PHARMACY}">
                    <li class="nav-item">
                        <a class="nav-link" href="/controller?command=createDrug">Create new drug</a>
                    </li>
                </c:if>
                <c:if test="${not empty sessionScope.user && sessionScope.user.role eq Role.DOCTOR}">
                    <li class="nav-item">
                        <a class="nav-link" href="/controller?command=recipeRequest">Show recipe requests</a>
                    </li>
                </c:if>
                <c:if test="${not empty sessionScope.user && sessionScope.user.role eq Role.DOCTOR}">
                    <li class="nav-item">
                        <a class="nav-link" href="/controller?command=usersAsClients">Create recipe for clent</a>
                    </li>
                </c:if>
                <c:if test="${not empty sessionScope.user}">
                <li class="nav-item">
                    <a class="nav-link" style="color: dodgerblue">
                    <jwds:welcomeUser text="Привет"/>
                    </a>
                </li
                </c:if>
            </ul>
        </div>

        <a class="btn btn-primary" href="/controller?command=changeLanguage&lang=en_US&crntURI=${pageContext.request.requestURI}" type="radio" aria-autocomplete="off" aria-checked >en</a>


        <a class="btn btn-primary" href="/controller?command=changeLanguage&lang=ru_RU&crntURI=${pageContext.request.requestURI}" type="radio" aria-autocomplete="off" aria-checked style="margin-right: 10px">ru</a>

        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a class="btn btn-primary" href="/controller?command=logout" role="button">Logout</a>
            </c:when>
            <c:otherwise>
                <a class="btn btn-primary" href="/controller?command=login" role="button">Login</a>
            </c:otherwise>
        </c:choose>
        <a class="nav-link" href="/controller?command=registration">Registration</a>
    </nav>
</div>
</header>
