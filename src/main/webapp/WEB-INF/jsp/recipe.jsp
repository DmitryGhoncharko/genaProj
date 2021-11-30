<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.ghoncharko.webproject.entity.RolesHolder" %>
<html>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
      integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"
        integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13"
        crossorigin="anonymous"></script>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
      integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
<head>
    <title>Title</title>
</head>
<body>
<header>
    <jsp:include page="header.jsp"></jsp:include>
</header>
<div class="container mt-4">
    <div class="row">
        <c:forEach var="recipe" items="${requestScope.recipes}">
            <form action="/controller?command=recipecmnd" method="post">
                <div class="col-auto mb-3"></div>
                <div class="card" style="width: 18rem;"></div>
                <div class="card-body "></div>

                <input hidden="" name="recipeId" type="text" value="${recipe.id}"> <h5 class="card-title">${recipe.id}</h5> </input>
                <input hidden="" name="userId" value="${recipe.user.id}">
                <input hidden="" name="drugId" value="${recipe.drug.id}">
                <input hidden="" name="drugName" value="${recipe.drug.name}"> <h6 class="card-subtitle mb-2 text-muted" >${recipe.drug.name}</h6> </input>
                <input hidden="" name="recipeDateStart" value="${recipe.dateStart}"> <h6 class="card-subtitle mb-2 text-muted" >${recipe.dateStart}</h6> </input>
                <input hidden="" name="recipeDateEnd" value="${recipe.dateEnd}"> <h6 class="card-subtitle mb-2 text-muted" >${recipe.dateEnd}</h6> </input>
                <c:choose>
                    <c:when test="${not empty sessionScope.user && sessionScope.user.role eq RolesHolder.CLIENT}">
                        <button  class="navbar-toggler">Продлить рецепт</button>
                        <c:if test="${not empty requestScope.error && not empty requestScope.drugId && requestScope.drugId eq drug.id}">
                            <div class="alert alert-danger" role="alert">
                                    ${requestScope.error}
                            </div>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <a class="btn btn-primary" role="button">Please login as Client</a>
                    </c:otherwise>
                </c:choose>
                </input>
            </form>
        </c:forEach>
    </div>
</div>
</body>
</html>

