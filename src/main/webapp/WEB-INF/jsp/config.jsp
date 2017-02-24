<%@ include file="/WEB-INF/jsp/include.jsp" %>
<div class=\"usdChannel\">
	<portlet:actionURL name="updateDisplay" var="updateDisplay">
		<portlet:param name="action" value="updateDisplay"/>
	</portlet:actionURL>

	<h2>Portlet display type:</h2></br>
	<form id="disp_type_form" action="${updateDisplay}">
		<select id="disp_type" name="disp_type">
			<c:forEach var="disp" items="${displayTypes}">
				<c:choose>
					<c:when test="${disp == displayType}">
						<option value="${disp}" selected="selected">${disp}<option>
					</c:when>
					<c:otherwise>
						<option value="${disp}">${disp}<option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<input type="submit" name="action" class="btn btn-default" value="updateDisplay"/>
	</form>

	<h2>Pages</h2>
	<c:set var="counter" value="${0}"/>
	<c:forEach var="pageUri" items="${pageUris}">
		<div class="pageUri" style="display:block">
			<h3>Page: ${pageUri}</h3>
			<c:set var="counter" value="${counter + 1}"/>

			<portlet:actionURL name="updatePage" var="updatePage"/>
			<form id="page_uri_form_${counter}" action="${updatePage}">
				<select id="${counter}" name="channel">
					<c:forEach var="page" items="${availablePages}">
						<c:choose>
							<c:when test="${page.path == pageUri}">
								<option value="${page.path}" selected="selected">Title: ${page.title}, Full Path: ${page.path}</option>
							</c:when>
							<c:otherwise>
								<option value="${page.path}">Title: ${page.title}, Full Path: ${page.path}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
				</br>
				<input type="hidden" name="index" value="${counter}"/>
				<input type="submit" name="action" value="update" class="btn btn-default"/>
				<portlet:actionURL name="removePage" var="removePage">
					<portlet:param name="action" value="remove"/>
					<portlet:param name="index" value="${counter}"/>
				</portlet:actionURL>
				<a type="button" href="${removePage}" class="btn btn-default">Remove page</a>
			</form>
		</div>
	</c:forEach>

	<portlet:actionURL name="addPage" var="addPage">
		<portlet:param name="action" value="add"/>
	</portlet:actionURL>
	<a type="button" href="${addPage}" class="btn btn-default">Add page</a>

	</br></br>
	<portlet:renderURL var="formDoneAction" portletMode="VIEW" windowState="NORMAL"/>
	<a type="button" href="${formDoneAction}" class="btn btn-default">Done</a>
</div>
