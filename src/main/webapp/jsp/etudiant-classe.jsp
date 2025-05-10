<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="fr">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Classe : ${cours.nom} - Étudiant</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/etudiant-classe.css" />
</head>

<body>
<div class="container">
    <header>
        <h1>Classe : ${cours.nom}</h1>
        <a href="${pageContext.request.contextPath}/etudiant/accueil" class="back-btn">← Retour à l'accueil</a>
        <%-- <button class="users-btn">👥 Utilisateurs inscrits</button> --%>
    </header>

    <section class="homework-list">
        <h2>Mes Devoirs</h2>

        <c:if test="${not empty erreurMessage}">
            <p class="error-message">${erreurMessage}</p>
        </c:if>
        <c:if test="${not empty succesMessageSoumission}">
            <p class="success-message">${succesMessageSoumission}</p>
            <c:remove var="succesMessageSoumission" scope="session"/>
        </c:if>
         <c:if test="${not empty erreurMessageSoumission}">
            <p class="error-message">${erreurMessageSoumission}</p>
            <c:remove var="erreurMessageSoumission" scope="session"/>
        </c:if>

        <c:if test="${empty devoirs}">
            <p>Aucun devoir assigné pour ce cours pour le moment.</p>
        </c:if>

        <c:forEach items="${devoirs}" var="devoir">
            <div class="homework-card">
                <div>
                    <h3>${devoir.titre}</h3>
                    <p>Deadline : <fmt:formatDate value="${devoir.dateLimite}" pattern="dd/MM/yyyy" /></p>
                    <c:if test="${not empty devoir.fichierNom}">
                        <a href="${pageContext.request.contextPath}/preview?devoirId=${devoir.id}" target="_blank" class="file-link">📄 Consulter le document du devoir</a>
                    </c:if>
                </div>

                <c:set var="rendu" value="${rendusMap[devoir.id]}" />
                <c:set var="currentDate" value="<%= new java.util.Date() %>" />

                <c:choose>
                    <c:when test="${not empty rendu}">
                        <%-- Cas 2 : Déjà soumis --%>
                        <p class="status submitted">Déjà soumis</p>
                        <c:if test="${not empty rendu.fichierNom}"> <%-- Check fichierNom or fichierData --%>
                             <a href="${pageContext.request.contextPath}/download?renduId=${rendu.id}" target="_blank" class="file-link">📎 Voir mon rendu</a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/etudiant/devoir-details?renduId=${rendu.id}" class="view-btn">Voir détails et évaluation</a>
                    </c:when>
                    <c:when test="${devoir.dateLimite.before(currentDate)}">
                        <%-- Cas 3 : Deadline dépassée sans rendu --%>
                        <p class="status missed">Non rendu (deadline dépassée)</p>
                    </c:when>
                    <c:otherwise>
                        <%-- Cas 1 : Devoir en cours, pas encore soumis --%>
                        <form class="submit-form" action="${pageContext.request.contextPath}/etudiant/soumettre-devoir" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="devoirId" value="${devoir.id}" />
                            <input type="hidden" name="coursId" value="${cours.id}" /> <%-- For redirect back --%>
                            <input type="file" accept=".pdf,.doc,.docx,.txt,.zip,.rar" name="fichierRendu" required />
                            <button type="submit">Soumettre</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:forEach>
    </section>
</div>
</body>

</html>
