<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"> </script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<portlet:actionURL var="getPages" name="getPages"></portlet:actionURL>

<SCRIPT LANGUAGE="javascript">
<!--
function populate_pages(data, textStatus, jqXHR)
{
//	alert("Populating...");
//	alert(data.toSource());
	var CID = "channel_" + data["index"];
	alert(CID);
	var pages = document.getElementById(CID);
//	alert(pages);
	pages.options.length=0;
	for (i = 0; i < data["pages"].length; i++)
	{
		pages.options[i] = new Option(
			"Title: " + data["pages"][i]["title"] + 
			", Full Path: " + data["pages"][i]["path"],
			data["pages"][i]["path"]);
	}
}
function OnChange(sources,pages)
{
	var myindex = sources.selectedIndex;
	var SelValue = sources.options[myindex].value;
//	alert(SelValue);

	pages.options.length=0;
	pages.options[0] = new Option("Loading...","");
//	$.ajax({dataType:"json",
//			url:"/CMSContent/v1/api/getPagesWithIndex.json",
//			data:{"source":SelValue,"index":myindex},
//			success:populate_pages});
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getPagesWithIndex.json",
		data:{"source":SelValue},
		success:populate_pages});
}
//-->
</SCRIPT>
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
		<div class="pageUri.key" style="display:block" class="form-group">
<!--			<h3>Page: ${pageUri.key}</h3>-->
			<c:set var="counter" value="${counter + 1}"/>

			<portlet:actionURL name="updatePage" var="updatePage"/>
			<form id="page_uri_form_${counter}" action="${updatePage}">
				<label for="source_selector_${counter}">Content Source</label>
				<select id="source_selector_${counter}" name="source" class="form-control"
						OnChange='OnChange(this.form.source_selector_${counter},this.form.channel_${counter});'>
					<c:forEach var="source" items="${sources}">
						<c:choose>
							<c:when test="${source == pageUri.value}">
								<option value="${source}" selected="selected">${source}</option>
							</c:when>
							<c:otherwise>
								<option value="${source}">${source}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
				<label for="channel_${counter}">Section Content</label>
				<select id="channel_${counter}" name="channel" class="form-control">
					<c:forEach var="page" items="${availablePages}">
						<c:choose>
							<c:when test="${page.path == pageUri.key}">
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
