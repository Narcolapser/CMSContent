<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--Single page view.-->
<div class="usdChannel">
	<c:forEach var="doc" items="${content}">
	<div>
		${doc.render()}
	</div>
	</c:forEach>
</div>
