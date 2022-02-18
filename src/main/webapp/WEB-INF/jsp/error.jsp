<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="${cookie.lang.value}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>
<fmt:message bundle="${loc}" key="label.error.title" var="title"/>
<fmt:message bundle="${loc}" key="label.error.spanMessage" var="spanMessage"/>
<fmt:message bundle="${loc}" key="label.error.midMessage" var="midMessage"/>
<fmt:message bundle="${loc}" key="label.error.backHome" var="backHome"/>
<head>
    <title>${title}</title>
</head>
<body>
<link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>


<div class="page-wrap d-flex flex-row align-items-center">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-12 text-center">
                <span class="display-1 d-block">${spanMessage}</span>
                <div class="mb-4 lead">${midMessage}</div>
                <a href="/controller?command=main_page" class="btn btn-link">${backHome}</a>
            </div>
        </div>
    </div>
</div>

</body>