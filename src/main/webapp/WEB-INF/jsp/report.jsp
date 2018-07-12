<%-- Author: Toben Archer | Version $Id$ --%>
<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<p>Report jsp!</p>
<table class="table">
	<tr>
		<c:forEach var="field" items="${fields}">
			<th>${field}</th>
		</c:forEach>
	</tr>
	<c:forEach var="response" items="${responses}">
		<tr>
			<c:forEach var="field" items="${fields}">
				<td>${response.getAnswer(field)}</td>
			</c:forEach>
		</tr>
	</c:forEach>
</table
