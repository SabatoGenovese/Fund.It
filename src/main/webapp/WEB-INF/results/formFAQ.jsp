<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <%@include file="../components/head.jsp" %>
        <title>Fund.it</title>
        <link rel="stylesheet" type="text/css" href=${pageContext.request.contextPath}/css/faq.css>
    </head>
    <body>
        <form method="post">
            <input type="text" name="domanda" maxlength="200" placeholder="Domanda" value="${requestScope.faq.domanda}">
            <input type="text" name="risposta" maxlength="200" placeholder="Risposta" value="${requestScope.faq.risposta}">
            <input type="hidden" name="idFaq" value="${requestScope.faq.idFaq}">
            <input type="submit"
            <c:choose>
                <c:when test="${requestScope.faq != null}">
                   formaction="${pageContext.request.contextPath}/faq/modificaFAQ" value="Salva modifiche">
                </c:when>
                <c:otherwise>
                    formaction="${pageContext.request.contextPath}/faq/inserisciFAQ" value="Inserisci FAQ">
                </c:otherwise>
            </c:choose>
        </form>
    </body>
</html>
