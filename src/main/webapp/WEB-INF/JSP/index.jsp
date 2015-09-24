<%@page contentType='text/html' pageEncoding='UTF-8' session='false'%>
<%@taglib uri='http://vdab.be/tags' prefix='v'%>
<%@taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@taglib uri='http://java.sun.com/jsp/jstl/fmt' prefix='fmt'%>

<!doctype html>
<html lang="nl">

<head>
<v:head title="Wereldwijnen"></v:head>
</head>
<body>
	<v:menu />
	
	<!-- Soorten -->
	<c:if test="${not empty param.landId}">
		<div class="soorten">
			<h2>Soorten uit ${gekozenLand.naam}</h2>
			<ol>
				<c:forEach var="soort" items="${gekozenLand.soorten}">
					<c:url var="urlMetIdLandEnSoort" value=''>
						<c:param name="landId" value="${gekozenLand.id}"/>
						<c:param name="soortId" value="${soort.id}"/>
					</c:url>
					<li>
						<a href="${urlMetIdLandEnSoort}">${soort.naam}</a>
					</li>
					
				</c:forEach>
			</ol>

		</div>
	</c:if>
	
	<!-- Wijnen -->
	<c:if test="${not empty param.soortId}">
		<div class="wijnen">
			<h2>Wijnen uit ${gekozenSoort.naam}</h2>
			<ol>
				<c:forEach var="wijn" items="${gekozenSoort.wijnen}">
				<c:url var="urlToevoegen" value='toevoegen.htm'>
						<c:param name="wijnId" value="${wijn.id}"/>
				</c:url>
				<li>
					<a href="${urlToevoegen}">
						${wijn.jaar}
						<c:forEach begin='1' end='${wijn.beoordeling}'>&#9733;</c:forEach>
					</a>
				</li>
				</c:forEach>	
			</ol>	
		</div>
	</c:if>

</body>
</html>