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
            <c:if test="${requestScope.drugUserOrders.size() == 0}">
                <h5 style="margin-left:43%; margin-top:10%">Your order is empty</h5>
                <img style="margin-left: 30%" src="${pageContext.request.contextPath}/static/orderEmpty.png">
            </c:if>
                <div class="row">
            <div class="col-md-6">
                    <form class="needs-validation" style="margin-left: 30%; margin-top: 5%">
                        <c:forEach var="drugUserOrder" items="${requestScope.drugUserOrders}">
                        <input hidden="" name="orderId" value="${drugUserOrder.id}">
                            <input hidden="" name="drugUserOrderId" value="${drugUserOrder.userOrder.id}">
                        <input hidden="" name="drugId" value="${drugUserOrder.product.id}">
                        <input hidden="" name="drugName" type="text" value="${drugUserOrder.product.name}">
                        <input hidden="" name="orderCount" value="${drugUserOrder.drugCount}">
                        <input hidden="" name="orderFinalPrice" value="${drugUserOrder.finalPrice}">
                        <div class="card" style="width: 18rem;">
                            <div class="card-body">
                                <h5 class="card-title">${drugUserOrder.product.name}</h5>
                                <p class="card-text">${drugUserOrder.product.producer.name}</p>
                            </div>
                            <ul class="list-group list-group-flush">
                                <h6>Колличество: </h6>
                                <br>
                                <li class="list-group-item">${drugUserOrder.drugCount}</li>
                                <h6>Итоговая цена: </h6>
                                <br>
                                <li class="list-group-item">${drugUserOrder.finalPrice}</li>

                                    </c:forEach>
                                <li class="list-group-item">${requestScope.orderFinalPrice}</li>
                                <select class="custom-select needs-validation" name="cardId" required>
                                    <c:forEach var="bankCard" items="${requestScope.bankCards}">
                                        <option value="${bankCard.id}">
                                            <button class="btn btn-primary">
                                                card id ${bankCard.id}
                                                <br/>
                                                balance ${bankCard.balance}
                                            </button>
                                        </option>
                                    </c:forEach>
                                </select>
                                </li>
                            </ul>
                            <div class="card-body">
                                <button class="btn btn-primary" type="submit" formaction="/controller?command=payOrder" formmethod="post">Оплатить</button>
                                <button class="btn btn-primary" formaction="/controller?command=deleteOrder" formmethod="post"> Удалить из корзины</button>
                                <c:if test="${not empty requestScope.errorDelete && not empty requestScope.drugId && requestScope.drugId eq product.id}">
                                    <div class="alert alert-danger" role="alert">
                                            ${requestScope.errorDelete}
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </form>
            </div>
                </div>
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

