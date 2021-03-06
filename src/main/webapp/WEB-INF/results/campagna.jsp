<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../components/head.jsp" %>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/campagna.css">
</head>
<body onload="percentage(${campagna.sommaTarget}, ${campagna.sommaRaccolta})">

<div>

    <!--Navbar-->
    <%@include file="../components/navbar.jsp" %>
    <div class="text-center mt-2">
        <%@include file="../components/toasts.jsp" %>
    </div>


    <!--Report-->
    <c:if test="${sessionScope.titoloReport.length() > 0}">
    <input id="message" type="hidden"
           value="${sessionScope.tipoReport}+${sessionScope.titoloReport}+${sessionScope.bodyReport}">
    <script>

        Toasty();
    </script>
        ${sessionScope.tipoReport = null}
        ${sessionScope.titoloReport = null}
        ${sessionScope.bodyReport = null}
    </c:if>

    <div>
        <!--Titolo-->
        <div class="container my-5">
            <h1 class="text-black text-center"> ${campagna.titolo}</h1>
        </div>
    </div>

    <!--Definizione colonne-->
    <div class="container">
        <div class="row">
            <div class="col-7">
                <!--Carosello-->
                <div id="carouselExampleControls" class="carousel slide" data-bs-ride="carousel">
                    <div class="carousel-inner">

                        <div class="carousel-item active">
                            <img src="${pageContext.request.contextPath}/file/${campagna.immagini.get(0).path}"
                                 class="d-block w-100" alt="...">
                        </div>

                        <c:forEach items="${requestScope.campagna.immagini}" var="immagine" begin="1">
                            <div class="carousel-item">
                                <img src="${pageContext.request.contextPath}/file/${immagine.path}"
                                     class="d-block w-100" alt="...">
                            </div>
                        </c:forEach>

                    </div>
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls"
                            data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls"
                            data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </div>
                <!--Fine Carosello-->

                <!--Descrizione-->
                <div class="container div-campagna-info2">
                    <h4>Descrizione</h4>
                    <hr class="solid text-black">
                    <p>
                        ${campagna.descrizione}
                    </p>

                </div>


                <!--Organizzatore-->
                <div class="container my-4 text-center">
                    <h3>Organizzatore</h3>
                    <hr class="solid text-black">
                    <a class="text-black" style="text-decoration: none; font-size: 23px">
                        <i class="fas fa-user-alt" style="font-size: 23px; color: #00AB98"></i>
                        ${campagna.utente.nome} ${campagna.utente.cognome}</a><br>
                    <a class="text-black" style="text-decoration: none; font-size: 19px"> Organizzatore</a><br>
                    <a class="text-black" style="text-decoration: none; font-size: 19px"> ${campagna.utente.citta},
                        Italia</a>
                </div>

                <!--Commenti-->
                <div class="container my-4">
                    <h3>Commenti</h3>

                    <c:forEach items="${requestScope.campagna.donazioni}" var="don">
                        <c:if test="${don.commento != null}">
                            <div class="container commento">
                                <hr class="solid text-black">
                               <c:choose>
                                   <c:when test = "${don.anonimo}">
                                       <h4>Anonimo ha donato
                                               <fmt:formatNumber type="number" maxFractionDigits="2"
                                                                 value="${don.sommaDonata}"/>&euro;</h4>
                                   </c:when>

                                   <c:otherwise>
                                       <h4>${don.utente.nome} ${don.utente.cognome} ha donato
                                               <fmt:formatNumber type="number" maxFractionDigits="2"
                                                                 value="${don.sommaDonata}"/>&euro;</h4>
                                   </c:otherwise>
                               </c:choose>

                                <h5>"${don.commento}"</h5>
                                <hr class="solid text-black">
                            </div>
                        </c:if>
                    </c:forEach>

                    <!--Segnalazione-->
                    <div class="container" style="margin-top: 120px">
                        <c:choose>
                            <c:when test="${sessionScope.utente != null}">
                                <a data-bs-toggle="modal" data-bs-target="#modalSegnalazioni"
                                   style="color: black; font-size: 20px"><i
                                        class="fas fa-flag"></i> Segnala la raccolta fondi</a>
                            </c:when>
                            <c:otherwise>
                                <a data-bs-toggle="modal" onclick="alert('Effettua prima il login!')"
                                   style="color: black; font-size: 20px"><i
                                        class="fas fa-flag"></i> Segnala la
                                    raccolta
                                    fondi</a>
                            </c:otherwise>
                        </c:choose>
                        <hr class="solid text-black">
                    </div>

                </div>
            </div>


            <div class="col-4 div-campagna-info" style="top:15px">
                <a class="text-center goal"><fmt:formatNumber type="number" maxFractionDigits="2"
                                                              value="${campagna.sommaRaccolta}"/>&euro; raccolti su
                    <fmt:formatNumber type="number" maxFractionDigits="2" value="${campagna.sommaTarget}"/>&euro;</a>

                <div class="progress" style="border-color: black; border-style: solid; border-width: 1px">
                    <div id="progressbar" class="progress-bar" role="progressbar" style=" background-color: #00AB98"
                         aria-valuenow="67" aria-valuemin="0" aria-valuemax="100"></div>
                </div>

                <div class="d-grid gap-2 my-3">
                    <input type="hidden" id="idCampagna" value="${campagna.idCampagna}">
                    <button id="condividiButton" type="button" class="btn btn-primary pulsante" data-bs-toggle="modal"
                            data-bs-target="#modalCondivisione"
                            style=" background-color: #00AB98; border-color: #00AB98">Condividi
                    </button>
                    <c:choose>
                        <c:when test="${sessionScope.utente != null}">
                            <a class="btn btn-primary pulsante" type="button"
                               href="http://localhost:8080/FundPay-1.0-SNAPSHOT/fundPay/paga?idCampagna=${campagna.idCampagna}&idCliente=${sessionScope.utente.idUtente}"
                               style=" background-color: #00AB98; border-color: #00AB98">Fai una Donazione
                            </a>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-primary pulsante" type="button"
                                    onclick="alert('Effettua prima il login!')"
                                    style=" background-color: #00AB98; border-color: #00AB98">Fai una Donazione
                            </button>
                        </c:otherwise>
                    </c:choose>
                </div>

                <a id="donation-count" style="font-size: 20px" )><i
                        class="fas fa-chart-line"></i> ${fn:length(campagna.donazioni)} persone hanno donato a questa
                    campagna</a><br>

                <div class="container my-4">
                    <hr class="solid text-black">
                </div>

                <!--Ultime donazioni-->
                <div class="container text-center" style="margin-top: 30px">

                    <c:choose>
                        <c:when test="${(campagna.donazioni.size()-3) >= 0}">
                            <c:forEach items="${campagna.donazioni}" begin="${campagna.donazioni.size()-3}" var="don">

                                <c:choose>
                                    <c:when test = "${don.anonimo}">
                                        <h6>Anonimo ha donato <fmt:formatNumber type="number" maxFractionDigits="2" value="${don.sommaDonata}"/>&euro; <span class="badge bg-white"
                                                                                                        style="color: #00AB98;">Nuovo</span>
                                        </h6>
                                    </c:when>

                                    <c:otherwise>
                                        <h6>${don.utente.nome} ha donato <fmt:formatNumber type="number" maxFractionDigits="2" value="${don.sommaDonata}"/>&euro; <span class="badge bg-white"
                                                                                                        style="color: #00AB98;">Nuovo</span>
                                        </h6>
                                    </c:otherwise>
                                </c:choose>

                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <c:forEach items="${campagna.donazioni}" var="don">
                                <c:choose>
                                    <c:when test = "${don.anonimo}">
                                        <h6>Anonimo ha donato <fmt:formatNumber type="number" maxFractionDigits="2" value="${don.sommaDonata}"/>&euro; <span class="badge bg-white"
                                                                                             style="color: #00AB98;">Nuovo</span>
                                        </h6>
                                    </c:when>

                                    <c:otherwise>
                                        <h6>${don.utente.nome} ha donato <fmt:formatNumber type="number" maxFractionDigits="2" value="${don.sommaDonata}"/>&euro; <span class="badge bg-white"
                                                                                                        style="color: #00AB98;">Nuovo</span>
                                        </h6>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>


                </div>


                <!--Visualizza donazioni-->
                <div class="d-grid gap-2 d-md-block text-center my-5">

                    <button type="button" class="btn btn-primary pulsante" data-bs-toggle="modal" data-bs-target="#exampleModal"
                            style=" background-color: #00AB98; border-color: #00AB98">Mostra tutto
                    </button>

                    <c:if test="${utente.idUtente == campagna.utente.idUtente}">
                        <button type="button" class="btn btn-primary pulsante" data-bs-toggle="modal"
                                data-bs-target="#modalChiusura"
                                style="background-color: crimson; border-color: crimson">Chiudi campagna
                        </button>

                        <button type="button" class="btn btn-primary pulsante mt-3" data-bs-toggle="modal"
                                data-bs-target="#modalCancellazione"
                                style="background-color: crimson; border-color: crimson">Cancella campagna
                        </button>

                        <div class="container">
                            <button class="btn btn-primary pulsante mt-4"
                                    onclick="location.href = '${pageContext.request.contextPath}/campagna/modificaCampagna?idCampagna=${requestScope.campagna.idCampagna}'"
                                    style="border-color: #00AB98; background-color: #00AB98">Modifica campagna
                            </button>
                        </div>
                    </c:if>


                </div>

            </div>

        </div>
    </div>

    <%@include file="../components/footer.jsp" %>

    <%@include file="../components/modals.jsp" %>


    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"
            crossorigin="anonymous"></script>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"
            integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13"
            crossorigin="anonymous"></script>

    <script src=${pageContext.request.contextPath}/js/campagna.js></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script defer>
        $("#condividiButton").click(
            function () {
                $.get(window.location.origin + window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)) + "/campagna/condividiCampagna?idCampagna=" + document.getElementById('idCampagna').value);
            }
        )
    </script>
</body>

</html>
