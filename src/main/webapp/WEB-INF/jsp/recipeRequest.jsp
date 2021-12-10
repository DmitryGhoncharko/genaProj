<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
        <c:forEach var="recipeRequest" items="${requestScope.recipeRequests}">
            <form>
                <div class="col-auto mb-3"></div>
                <div class="card" style="width: 18rem;"></div>
                <div class="card-body "></div>
                <input hidden="" name="recipeRequestId" value="${recipeRequest.id}"><h5
                    class="card-title"></h5></input>

                <input hidden="" name="userId" value="${recipeRequest.user.id}">
                <input hidden="" name="drugId" value="${recipeRequest.drug.id}">
                <input hidden="" name="drugName" type="text" value="${recipeRequest.drug.name}"> <h5
                    class="card-title">${recipeRequest.drug.name}</h5> </input>
                <input hidden="" name="dateStart" value="${recipeRequest.dateStart}"> <h5
                    class="card-title">${recipeRequest.dateStart}</h5> </input>
                <input class="needs-validation" hidden="" name="dateEnd" value="${recipeRequest.dateEnd}"> <h6
                    class="card-subtitle mb-2 text-muted"
            >${recipeRequest.dateEnd}</h6> </input>
                <input name="updatedDateEnd" type="date" >
                <button class="navbar-toggler" formaction="/controller?command=acceptRecipeRequest" formmethod="post" type="submit">Продлить рецепт</button>
                <button class="navbar-toggler" formaction="/controller?command=declineRecipeRequest" formmethod="post">
                    Не продлевать рецепт
                </button>
            </form>
        </c:forEach>
    </div>
</div>
</body>
</html>
