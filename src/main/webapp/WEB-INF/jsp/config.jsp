<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class=\"usdChannel\">

<!--	<p>${availablePages}</p>-->
<!--	<p>spelled out:</p>-->

<!--	<c:forEach var="page" items="${availablePages}">-->
<!--		<p>Title: ${page.title}</p>-->
<!--		<p>Path: ${page.path}</p>-->
<!--	</c:forEach>-->

<!--	<p>page uris:</p>-->
<!--	<p>${pageUris[0]}</p>-->

	<c:forEach var="pageUri" items="${pageUris}">
		<div class="pageUri" style="display:block">
			<h3>Page: ${pageUri}</h3>
		</div>
	</c:forEach>

	<portlet:actionURL var="addPage">
		<portlet:param name="action" value="add"/>
	</portlet:actionURL>

	<button type="button" onclick="${addPage}" class="portlet-form-button">Change it.</button>
	<portlet:renderURL var="formDoneAction" portletMode="VIEW" windowState="NORMAL"/>
	<button type="button" onclick="window.location='${formDoneAction}'" class="portlet-form-button">Done</button>

</div>
