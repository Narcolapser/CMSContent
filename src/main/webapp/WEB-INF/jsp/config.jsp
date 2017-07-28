<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"> </script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/chosen.jquery.js" type="text/javascript"></script>
<link rel="stylesheet" href="/CMSContent/css/chosen.css">
<c:set var="n"><portlet:namespace/></c:set>
<portlet:actionURL var="getPages" name="getPages"></portlet:actionURL>

<style type="text/css">
/*#wrap {
   width:600px;
   margin:0 auto;
}
#left_col {
   float:left;
   width:50%;
}
#right_col {
   float:right;
   width:50%;
}*/

#content-wrapper{
  display:table;
}

#content{
  display:table-row;
}

#content>div{
  display:table-cell
}

</style>


<div class=\"usdChannel\" id="content-wrapper">
	<div id="content">
	<portlet:actionURL name="updateDisplay" var="updateDisplay">
		<portlet:param name="action" value="updateDisplay"/>
	</portlet:actionURL>

	<div id="left_col">
	<h2>Portlet display type:</h2>
	<!--</br>-->
	<form id="disp_type_form" action="${updateDisplay}">
		<select id="disp_type" name="disp_type" class="form-control">
			<c:forEach var="disp" items="${displayTypes}">
				<c:choose>
					<c:when test="${disp == displayType}">
						<option value="${disp}" selected="selected">${disp}</option>
					</c:when>
					<c:otherwise>
						<option value="${disp}">${disp}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<input type="submit" name="action" class="btn btn-default" value="updateDisplay"/>
	</form>
	</div>

	<div id="right_col">
	<h2>Pages</h2>

	</div>
	</div>


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
