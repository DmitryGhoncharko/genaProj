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
<style>
    .flex {
        display: flex;
        flex-direction: column;
        height: 100vh;
    }

    body {
        margin: 0;
        padding: 0
    }
    .h100 {

        flex-grow: 3
    }


</style>
<head>
    <title>Title</title>
</head>
<body>
<div class="container-fluid flex">
    <div class="row">
        <div class="col-md-12">
            <jsp:include page="header.jsp"></jsp:include>
        </div>
    </div>

    <div class="row h100">
        <div class="col-md-12">
            <c:forEach var="drug" items="${requestScope.drugs}">
                <c:choose>
                    <c:when test="${not empty sessionScope.user && sessionScope.user.role eq RolesHolder.PHARMACY}">
                        <div class="row">
                            <div class="col-md-6">
                                <form class="needs-validation" style="margin-left: 30%; margin-top: 5%">
                                    <input hidden="" name="drugId" value="${drug.id}">
                                    <div class="card" style="width: 18rem;">
                                        <div class="card-body">
                                            <input class="needs-validation" name="updateDrugName" type="text" value="${drug.name}" minlength="1" maxlength="45" placeholder="input new drug name" required>
                                            <input class="needs-validation" name="updateDrugProducer" type="text" value="${drug.producer.name}" minlength="1" maxlength="45" placeholder="input new drug producer name" required>
                                        </div>
                                        <ul class="list-group list-group-flush">
                                            <li class="list-group-item">
                                                <input class="needs-validation" name="updateDrugCount" type="number" value="${drug.count}" placeholder="write new drug count" min="0" minlength="1" maxlength="11" required>
                                            </li>
                                            <li class="list-group-item">
                                                <select name="updateDrugNeedRecipe" class="needs-validation" required>
                                                    <option selected value="${drug.needRecipe}"><button class="button-prmary">current need recipe ${drug.needRecipe}</button></option>
                                                    <option value="true">
                                                        <button class="btn btn-primary">
                                                            true
                                                        </button>
                                                    </option>
                                                    <option value="false">
                                                        <button class="btn btn-primary">
                                                            false
                                                        </button>
                                                    </option>
                                                </select>
                                            </li>
                                            <li class="list-group-item">
                                                <input class="needs-validation" value="${drug.price}"  type="text" name="updateDrugPrice"
                                                       placeholder="write new drug price" required>
                                            </li>
                                            <input hidden id="${drug.id}d" value="${drug.description}" name="updateDrugDescription" type="text">
                                        </ul>
                                        <div class="card-body">
                                            <button class="btn btn-primary" type="submit" formaction="/controller?command=updateDrug" formmethod="post">Update</button>
                                            <button class="btn btn-primary" type="submit" formaction="/controller?command=deleteDrug" formmethod="post">Delete</button>
                                            <c:if test="${not empty requestScope.errorDelete && not empty requestScope.drugId && requestScope.drugId eq drug.id}">
                                                <div class="alert alert-danger" role="alert">
                                                        ${requestScope.errorDelete}
                                                </div>
                                            </c:if>
                                        </div>
                                    </div>

                            </form>
                            </div>
                            <div class="col-md-6">
                                <p style="margin-left: 30%; margin-right: 30%; margin-bottom: 5%;margin-top: 5%">Description</p>
                                <input  class="needs-validation" value="${drug.description}"  id="${drug.id}" onkeyup="copyValueTo(this,'${drug.id}d')"  type="text" minlength="1"
                                        placeholder="write new drug description" style="margin-left: 15%;margin-right: 15%;margin-bottom: 5%;margin-top: 5%" required>
                            </div>
                            <script>
                                function copyValueTo(fromElem, toElemId) {
                                    var elem = document.getElementById(toElemId);
                                    elem.value = fromElem.value;
                                }
                            </script>
                        </div>
                    </c:when>
                    <c:otherwise>
                <div class="row">
                    <div class="col-md-6">
                        <form class="needs-validation" style="margin-left: 30%; margin-top: 5%">
                            <input hidden="" name="drugId" value="${drug.id}">
                            <input hidden="" name="drugNeedRecipe" value="${drug.needRecipe}">
                            <input hidden="" name="drugName"  value="${drug.name}">
                            <input hidden="" name="drugCount" value="${drug.count}">
                            <input hidden="" name="drugPrice" value="${drug.price}">
                            <input hidden="" name="drugProducerName" value="${drug.producer.name}">
                                <div class="card" style="width: 18rem;">
                                    <div class="card-body">
                                        <h5 class="card-title">${drug.name}</h5>
                                        <p class="card-text">${drug.producer.name}</p>
                                    </div>
                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item">${drug.count}</li>
                                        <li class="list-group-item">${drug.needRecipe}</li>
                                        <li class="list-group-item">${drug.price}</li>
                                        <c:if test="${not empty sessionScope.user && sessionScope.user.role eq RolesHolder.CLIENT}">
                                            <li class="list-group-item">
                                                <input class="needs-validation" type="number" name="countUserBuyDrugs" placeholder="count drugs"
                                                       min="1"
                                                       max="${drug.count}" required> Count Drugs</input>
                                                <div class="invalid-feedback">
                                                    minimal login length = 6
                                                </div>
                                            </li>
                                        </c:if>
                                    </ul>
                                    <div class="card-body">
                                        <c:if test="${not empty sessionScope.user && sessionScope.user.role eq RolesHolder.CLIENT}">
                                            <button class="btn btn-primary" type="submit" formaction="/controller?command=addToBucket" formmethod="post">Add to order</button>
                                        </c:if>
                                        <c:if test="${not empty requestScope.errorDelete && not empty requestScope.drugId && requestScope.drugId eq drug.id}">
                                            <div class="alert alert-danger" role="alert">
                                                    ${requestScope.errorDelete}
                                            </div>
                                        </c:if>
                                    </div>
                                </div>


                        </form>
                    </div>
                    <div class="col-md-6">
                        <p style="margin-left: 30%; margin-right: 30%; margin-bottom: 5%;margin-top: 5%">Description</p>
                        <a style="margin-left: 15%;margin-right: 15%;margin-bottom: 5%;margin-top: 5%">${drug.description}</a>
                    </div>

                </div>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <jsp:include page="footer.jsp"></jsp:include>
        </div>
    </div>
</div>
</body>
<script>
    (function() {
        'use strict';
        window.addEventListener('load', function() {
            var forms = document.getElementsByClassName('needs-validation');
            var validation = Array.prototype.filter.call(forms, function(form) {
                form.addEventListener('submit', function(event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        }, false);
    })();
</script>
</html>