<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script type="text/javascript">
function callURL(val)
{
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.onreadystatechange = function()
	{
		if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
			callback(xmlHttp.responseText);
	}
	xmlHttp.open("GET",val, true); // true for asynchronous 
	xmlHttp.send(null);
}
function addPage(path)
{
	var val = "https://localhost.usd.edu" + path;
	callURL(val)

}
function updatePage(path,index)
{
	var val = "https://localhost.usd.edu" + path;
	var params = "&index=" + index;
	var x = document.getElementsByName("pageUris");
	var i;

	for(i = 0; i < x.length ; i++)
	{
		if (x[i].id == index)
		{
			params += "&channel="+x[i].value;
		}
	}
	callURL(val+params);
}
function removePage(path,index)
{
	var val = "https://localhost.usd.edu" + path;
	var params = "&index=" + index;
	callURL(val+params);
}
function updateDisplay(path)
{
	var val = "https://localhost.usd.edu" + path;
	var params = "&disp_type=" + document.getElementById("disp_type").value;
	callURL(val+params);
}
</script>
<div class=\"usdChannel\">
	<portlet:actionURL name="addPage" var="addPage">
		<portlet:param name="action" value="add"/>
	</portlet:actionURL>

	<portlet:actionURL name="updatePage" var="updatePage">
		<portlet:param name="action" value="update"/>
	</portlet:actionURL>

	<portlet:actionURL name="removePage" var="removePage">
		<portlet:param name="action" value="remove"/>
	</portlet:actionURL>

	<portlet:actionURL name="updateDisplay" var="updateDisplay">
		<portlet:param name="action" value="updateDisplay"/>
	</portlet:actionURL>

	<select id="disp_type">
		<option value="single">Single Page<option>
		<option value="collapsing">Collapsing<option>
		<option value="tabbed">Tabbed<option>
	</select>
	<br/>
	<button type="button" onclick="updateDisplay('${updateDisplay}');" class="portlet-form-button">Update Display</button>

	<c:set var="counter" value="${0}"/>
	<c:forEach var="pageUri" items="${pageUris}">
		<div class="pageUri" style="display:block">
			<h3>Page: ${pageUri}</h3>
			<c:set var="counter" value="${counter + 1}"/>
			<select id="${counter}" name="pageUris">
				<c:forEach var="page" items="${availablePages}">
					<option value="${page.path}">Title: ${page.title}, Full Path: ${page.path}</option>
				</c:forEach>
			</select>
			</br>
			<button type="button" onclick="updatePage('${updatePage}','${counter}');" class="portlet-form-button">Update page</button>
			<button type="button" onclick="removePage('${removePage}','${counter}');" class="portlet-form-button">Remove page</button>
		</div>
	</c:forEach>

	<button type="button" onclick="addPage('${addPage}');" class="portlet-form-button">Add page</button>
	<portlet:renderURL var="formDoneAction" portletMode="VIEW" windowState="NORMAL"/>
	<button type="button" onclick="window.location='${formDoneAction}'" class="portlet-form-button">Done</button>

</div>
