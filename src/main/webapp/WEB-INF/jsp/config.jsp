<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"> </script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/chosen.jquery.js" type="text/javascript"></script>
<link rel="stylesheet" href="/CMSContent/css/chosen.css">
<c:set var="n"><portlet:namespace/></c:set>
<portlet:actionURL var="getPages" name="getPages"></portlet:actionURL>

<style type="text/css">
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

div.col_content{
	width: 100%;
}

</style>


<div class="usdChannel" id="content-wrapper">
	<div id="content">
		<c:set var="mode" value="normal"/>
		
		<div id="left_col">
			<h2>Portlet view type:</h2>
			<select id="${mode}_view" name="view_type" class="form-control" OnChange="viewChange('${mode}')">
				<c:forEach var="view" items="${availableViews}">
					<c:choose>
						<c:when test="${view.view == currentView.view}">
							<option value="${view.view}" selected="selected">${view.name}</option>
						</c:when>
						<c:otherwise>
							<option value="${view.view}">${view.name}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
			<button class="btn btn-default" onclick="update_view('${mode}');return false"/>Update layout</button>
			<div class="col_content">
				<c:forEach var="view" items="${availableViews}">
					<!-- ${view} ${view.view} ${currentView} ${currentView.view} -->
					<c:choose>
						<c:when test="${view.view == currentView.view}">
							<div id="${mode}_${view.view}" class="${mode}_desc show">
								<h3>View Description:</h3>
								<p>${view.description}</p>
							</div>
						</c:when>
						<c:otherwise>
							<div id="${mode}_${view.view}" class="${mode}_desc hide">
								<h3>View Description:</h3>
								<p>${view.description}</p>
							</div>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
			<div>
				<table class="table table-striped">
					<thead>
						<tr>
							<th>Property</th>
							<th>Value</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="property" items="${currentView.properties}">
						<tr>
							<td>${property}</td>
						</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
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
						<select id="${mode}_source" class="form-control" OnChange="OnChange(this.form.source_selector_${counter},'${mode}_doc_select');">
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
					<table class="table table-striped">
						<thead>
							<tr>
								<th>Position</th>
								<th>Title</th>
								<th>Id</th>
								<th></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="doc" items="${activeDocumentsNormal}">
								<tr>
									<td><button>Up</button><button>Down</button></td>
									<td>${doc.title}</td>
									<td>${doc.id}</td>
									<td><button>Remove</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
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
	var e = document.getElementById(val+"_doc_select");
	var selected = e.options[e.selectedIndex].value;
	var source_e =document.getElementById(val+"_source");
	var source_selected = source_e.options[source_e.selectedIndex].value;
	$.ajax({dataType:"json",
		url:"${updateDocument}",
		data:{"document":selected,
			"source":source_selected,
			"index":e.length,
			"mode":val}});
}

function update_view(mode)
{
	alert("Updating view!");
	var e = document.getElementById(mode+"_view_type");
	var selected = e.options[e.selectedIndex].value;
	$.ajax({dataType:"json",
		url:"${updateView}",
		data:{"view_type":selected,
			"mode":mode}});
}

function viewChange(mode)
{
	var all = document.getElementsByClassName(mode + "_desc");
	var e = document.getElementById(mode+"_view");
	var selected = e.options[e.selectedIndex].value;
	for(i = 0; i < all.length; i ++)
	{
		var obj = all[i];
		var classes = obj.className.split(" ");
		comp = mode + "_" + selected;
		if(obj.id == comp)
		{
			obj.className = mode + "_desc show";
		}
		else
		{
			obj.className = mode + "_desc hide";
		}
	}
	
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
function OnChange(sources,doc_selector)
{
	var myindex = sources.selectedIndex;
	var SelValue = sources.options[myindex].value;

	pages.options.length=0;
	pages.options[0] = new Option("Loading...","");
	alert("on change!");
//	$.ajax({dataType:"json",
//		url:"/CMSContent/v1/api/getPagesWithIndex.json",
//		data:{"source":SelValue,"index":doc_selector},
//		success:populate_pages});
}
</SCRIPT>
