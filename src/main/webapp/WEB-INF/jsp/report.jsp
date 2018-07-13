<%-- Author: Toben Archer | Version $Id$ --%>
<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<table class="table table-striped">
	<tr>
		<c:forEach var="field" items="${fields}">
			<c:if test="${field.length() > 20}">
				<c:set var="field">${field.substring(0,20)}...</c:set>
			</c:if>
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
