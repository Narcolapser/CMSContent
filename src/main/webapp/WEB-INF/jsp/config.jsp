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
  width: 100%;
}

#content{
  display:table-row;
}

#content>div{
  display:table-cell;
  width:50%;
  padding: 10px;
}

</style>


<div class="usdChannel" id="content-wrapper">
	<div id="content">
		<c:set var="mode" value="normal"/>
		
		<div id="left_col">
			<h2>Portlet view type:</h2>
			<select id="${mode}_disp_type" name="disp_type" class="form-control">
				<c:forEach var="disp" items="${availableViews}">
					<c:choose>
						<c:when test="${disp.view == currentView.view}">
							<option value="${disp.view}" selected="selected">${disp.name}</option>
						</c:when>
						<c:otherwise>
							<option value="${disp.view}">${disp.name}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
			<button class="btn btn-default" onclick="update_view('${mode}');return false"/>Update layout</button>
		</div>


		<div id="right_col">
			<h2>Documents</h2>
			<div name="${mode}_doc_pane">
				<div name="${mode}_controls" class="form-inline">
					<div class="form-group">
						<div class="btn-group" role="group">
							<button onclick="new_document('${mode}_doc_select')" class="btn btn-info">
								<i class="fa fa-clone"></i> New</button>
							<button onclick="add_document('${mode}')" class="btn btn-primary">
								<i class="fa fa-link"></i> Add</button>
							<button onclick="edit_document('${mode}_doc_select')" class="btn btn-warning">
								<i class="fa fa-gears"></i> Edit</button>
						</div>
						<label for="${mode}_source">Document Source</label>
						<select id="${mode}_source" class="form-control">
							<c:forEach var="source" items="${sources}">
								<option value="${source.getDaoName()}">${source.getDaoName()}</option>
							</c:forEach>
						</select>
					</div>
					<div id="${mode}_doc_selector">
						<select id="${mode}_doc_select">
							<c:forEach var="doc" items="${Internal}">
								<option value="${doc.id}">Title: ${doc.title}, Id: ${doc.id}</option>
							</c:forEach>
						</select>
					</div>
				</div>
				<div>
					<c:forEach var="doc" items="${activeDocumentsNormal}">
						<p>${doc}</p>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>


	</br></br>
	<portlet:renderURL var="formDoneAction" portletMode="VIEW" windowState="NORMAL"/>
	<a type="button" href="${formDoneAction}" class="btn btn-default">Done</a>
</div>

<portlet:actionURL var="updateDocument">
	<portlet:param name="action" value="updateDocument"/>
</portlet:actionURL>
<portlet:actionURL var="updateView">
	<portlet:param name="action" value="updateView" />
</portlet:actionURL>
<SCRIPT LANGUAGE="javascript">
//$(".chosen-select").chosen();

function new_document(val)
{
	alert("New!" + val);
}

function edit_document(val)
{
	alert("Edit!" + val);
}

function add_document(val)
{
//	alert("Add!" + val);'${mode}_doc_select',
	var e = document.getElementById(val+"_doc_select");
	var selected = e.options[e.selectedIndex].value;
	var source_e =document.getElementById(val+"_source");
	var source_selected = source_e.options[source_e.selectedIndex].value;
	$.ajax({dataType:"json",
		url:"${updateDocument}",
		data:{"document":selected,
			"source":source_selected,
			"index":e.length,
			"mode":val},
		success:add_back});
}

function update_view(mode)
{
	alert("Updating view!");
//	var e = document.getElementById(mode+"_disp_type");
//	var selected = e.options[e.selectedIndex].value;
//	$.ajax({dataType:"json",
//		url:"${updateView}",
//		data:{"disp_type":selected,
//			"mode":mode}});
}

function add_back(data, textStatus, jqXHR)
{
	alert("BACK!");
	alert(data);
}

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
