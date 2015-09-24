<%@ tag language="java" pageEncoding="UTF-8"%>
<%@taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core'%>

<nav>
	<h1>Wereldwijnen</h1>

	<ol class="menu gecentreerd">
		<c:forEach var="land" items="${landen}">
			<c:url var="urlMetId" value="/index.htm">
				<c:param name="landId" value="${land.id}" />
			</c:url>
			<li><a href="${urlMetId}"> <img
					src='<c:url value="/images/${land.id}.png"/>' alt="${land.naam}"
					title="${land.naam}">
			</a></li>
		</c:forEach>
	</ol>

	<c:if test="${mandjeAanwezig}">
		<a class="menu gecentreerd" href='<c:url value='/mandje.htm'/>'>
			<img src='<c:url value='/images/mandje.png'/>'  title="naar mandje" alt="naar mandje">
		</a>
	</c:if>

</nav>