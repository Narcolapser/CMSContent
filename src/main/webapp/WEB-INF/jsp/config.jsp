<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"> </script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/chosen.jquery.js" type="text/javascript"></script>
<link rel="stylesheet" href="/CMSContent/css/chosen.css">
<c:set var="n"><portlet:namespace/></c:set>
<portlet:actionURL var="getPages" name="getPages"></portlet:actionURL>

<div class=\"usdChannel\">
	<portlet:actionURL name="updateDisplay" var="updateDisplay">
		<portlet:param name="action" value="updateDisplay"/>
	</portlet:actionURL>

	<h2>Portlet display type:</h2>
	<!--</br>-->
	<form id="disp_type_form" action="${updateDisplay}">
		<select id="disp_type" name="disp_type" class="form-control">
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
		<div class="pageUri.Id" style="display:block" class="form-group">
			<c:set var="counter" value="${counter + 1}"/>
			<c:set var="selected" value="selected"/>

			<portlet:actionURL name="updatePage" var="updatePage"/>
			<form id="page_uri_form_${counter}" action="${updatePage}">
				<label for="source_selector_${counter}">Content Source</label>
				<select id="source_selector_${counter}" name="source" class="form-control"
						OnChange='OnChange(this.form.source_selector_${counter},this.form.channel_${counter});'>
					<c:forEach var="source" items="${sources}">
						<c:choose>
							<c:when test="${source == pageUri.source}">
								<option value="${source}" selected="selected">${source}</option>
								<c:set var="selected" value=""/>
							</c:when>
							<c:otherwise>
								<option value="${source}">${source}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${selected == 'selected'}">
						<option value="None" selected="${selected}">None</option>
					</c:if>
				</select>
				<label for="channel_${counter}">Section Content</label>

				<c:choose>
					<c:when test="${pageUri.source == 'CommonSpot'}">
						<c:set var="pageSource" value="${CommonSpot}"/>
					</c:when>
					<c:when test="${pageUri.source == 'Internal'}">
						<c:set var="pageSource" value="${Internal}"/>
					</c:when>
					<c:otherwise>
						<c:set var="pageSource" value="${availablePages}"/>
					</c:otherwise>
				</c:choose>

				<c:set var="selected" value="selected"/>
				<select id="channel_${counter}" name="channel" class="form-control chosen-select">
					<c:forEach var="page" items="${pageSource}">
						<c:choose>
							<c:when test="${page.id == pageUri.id}">
								<option value="${page.id}" selected="selected">Title: ${page.title}, Full Id: ${page.id}</option>
								<c:set var="selected" value=""/>
							</c:when>
							<c:otherwise>
								<option value="${page.id}">Title: ${page.title}, Full Id: ${page.id}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
					<c:if test="${selected == 'selected'}">
						<option value="None" selected="${selected}">None</option>
					</c:if>
				</select>
				</br>
				<input type="hidden" name="index" value="${counter}"/>
				<input type="submit" name="action" value="Update" class="btn btn-default"/>
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
<SCRIPT LANGUAGE="javascript">
//$(".chosen-select").chosen();

function populate_pages(data, textStatus, jqXHR)
{
	CID = data["index"]
	var pages = document.getElementById(CID);

	pages.options.length=0;
	for (i = 0; i < data["pages"].length; i++)
	{
		pages.options[i] = new Option(
			"Title: " + data["pages"][i]["title"] + 
			", Full Id: " + data["pages"][i]["id"],
			data["pages"][i]["id"]);
	}
}
function OnChange(sources,pages)
{
	var myindex = sources.selectedIndex;
	var SelValue = sources.options[myindex].value;

	pages.options.length=0;
	pages.options[0] = new Option("Loading...","");
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getPagesWithIndex.json",
		data:{"source":SelValue,"index":pages.id},
		success:populate_pages});
}
</SCRIPT>
