<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="<c:url value='/webjars/jquery/3.3.1-1/jquery.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-ui/1.12.1/jquery-ui.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/jquery-ui-themes/1.12.1/smoothness/jquery-ui.min.css'/>" />

<script src="<c:url value='/webjars/chosen/1.8.2/chosen.jquery.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/chosen/1.8.2/chosen.min.css'/>" />

<c:set var="n"><portlet:namespace/></c:set>
<c:set var="server">${hostname}.usd.edu</c:set>
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

.form-group{
	padding: 0px 0px 10px 0px;
}
.pos_col{
	min-width: 93px;
}
</style>


<div class="usdChannel" id="content-wrapper">
	<c:set var="mode" value="normal"/>
	<c:set var="modes" value = "${['normal','maximized']}"/>
	
	<ul class="nav nav-tabs">
		<c:forEach var="mode" items="${modes}">
			<c:set var="classes"></c:set>
			<c:if test="${mode eq 'normal'}">
				<c:set var="classes">class="active"</c:set>
			</c:if>
			<li ${classes}>
				<a href="#cms-${mode}" data-toggle="tab">${mode}</a>
			</li>
		</c:forEach>
	</ul>

	<div class="tab-content">
		<c:forEach var="mode" items="${modes}">
			<c:choose>
				<c:when test="${mode == maximized}"> 
					<c:set var="currentView" value="${maximized}"/>
				</c:when>
				<c:otherwise>
					<c:set var="currentView" value="${normal}"/>
				</c:otherwise>
			</c:choose>
			<c:set var="classes">class="tab-pane"</c:set>
			<c:if test="${mode eq 'normal'}">
				<c:set var="classes">class="tab-pane active"</c:set>
			</c:if>
			<div id="cms-${mode}" ${classes}>
				<div id="content">
					<div id="left_col">
						<h2>Portlet layout type:</h2>
						<div class="form-group">
							<select id="${mode}_view_type" name="view_type" class="form-control" OnChange="viewChange('${mode}')">
								<c:forEach var="view" items="${availableViews}">
									<c:choose>
										<c:when test="${view.view == activeViews[mode].view}">
											<option value="${view.view}" selected="selected">${view.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${view.view}">${view.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							<button class="btn btn-default" onclick="update_view('${mode}');return false"/>Update layout</button>
						</div>
						<div class="col_content">
							<c:forEach var="view" items="${availableViews}">
								<!-- View: ${view} v.v: ${view.view} current view:${activeViews[mode]} c.v:${activeViews[mode].view} -->
								<c:choose>
									<c:when test="${view.view == activeViews[mode].view}">
										<div id="${mode}_${view.view}" class="${mode}_desc show">
											<h3>Layout Description:</h3>
											<p>${view.description}</p>
										</div>
									</c:when>
									<c:otherwise>
										<div id="${mode}_${view.view}" class="${mode}_desc hide">
											<h3>Layout Description:</h3>
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
									<c:forEach var="property" items="${activeViews[mode].properties.keySet()}">
									<tr>
										<c:set var="prop">${mode}_${property.replace(" ","_")}</c:set>
										<td>${property}</td>
										<td><input id="${prop}" type="text" value="${activeViews[mode].properties.get(property)}"></input></td>
										<td><a class="btn btn-default" onclick="update_property('${prop}');return false">Update</a></td>
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
										<button onclick="new_document('${mode}_doc_select')" class="btn btn-info" title="Open new document on ${hostname}.usd.edu">
											<i class="fa fa-plus-square-o"></i> New Doc</button>
										<button onclick="new_form('${mode}')" class="btn btn-info" title="Open new form on ${hostname}.usd.edu">
											<i class="fa fa-code-fork"></i> New Form</button>
										<button onclick="edit_document('${mode}')" class="btn btn-warning" title="Open editor for selected doc on ${hostname}.usd.edu">
											<i class="fa fa-edit"></i> Edit</button>
										<button onclick="add_document('${mode}')" class="btn btn-primary">
											<i class="fa fa-link"></i> Add to Layout</button>
									</div>
									<label for="${mode}_source">Document Source</label>
									<select id="${mode}_source" class="form-control" OnChange="OnChange('${mode}_source','${mode}_doc_select');">
										<c:forEach var="source" items="${sources}">
											<option value="${source.getDaoName()}">${source.getDaoName()}</option>
										</c:forEach>
									</select>
								</div>
								<div id="${mode}_doc_selector">
									<select id="${mode}_doc_select" class="chosen-select">
										<c:forEach var="doc" items="${Internal}">
											<c:set var="title">Doc Title: </c:set>
											<c:if test="${fn:contains(doc.id,'form:')}">
												<c:set var="title">Form Title: </c:set>
											</c:if>
											<option value="${doc.id}" data-title="${doc.title}" data-type="${doc.docType}">${title} ${doc.title}, Id: ${doc.id}</option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div>
								<table class="table table-striped" id="${mode}_docs_table">
									<thead>
										<tr>
											<th class="pos_col" >Position</th>
											<th>Title</th>
											<th>Id</th>
											<th>Security Groups</th>
											<th></th>
										</tr>
									</thead>
									<tbody>
										<c:set var="index" value="1"/>
										<c:forEach var="doc" items="${activeDocs[mode]}">
											<tr class="${mode} ${index}">
												<td class="pos_col" >
													<div class="btn-group" role="group">
														<button onclick="up_document('${mode}',this);return false" class="btn btn-default">
															<i class="fa fa-arrow-up"></i>
														</button>
														<button onclick="down_document('${mode}',this);return false" class="btn btn-default">
															<i class="fa fa-arrow-down"></i>
														</button>
													</div>
												</td>
												<td>${doc.docTitle}</td>
												<td>${doc.docId}</td>
												<td>
													<select id="security_select_${mode}_${doc.docId}" class="chosen-select-multi" multiple="" data-placeholder="Everyone" OnChange="sec_change('security_select_${mode}_${doc.docId}','${mode}','${doc.docId}');">
														<c:forEach var="role" items="${securityRoles}">
															<c:set var="contains" value="false"/>
															<c:forEach var="irole" items="${doc.securityGroups}">
																<c:if test="${irole eq role}">
																	<c:set var="contains" value="true"/>
																</c:if>
															</c:forEach>
															<c:choose>
																<c:when test="${contains}">
																	<option value="${role}" data-title="${role}" selected="selected">${role}</option>
																</c:when>
																<c:otherwise>
																	<option value="${role}" data-title="${role}">${role}</option>
																</c:otherwise>
															</c:choose>
														</c:forEach>
													</select>
												</td>
												<td><button class="btn btn-danger" onclick="remove_document('${mode}',this);return false;">Remove</button></td>
											</tr>
											<c:set var="index" value="${index + 1}"/>
										</c:forEach>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:forEach>
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
<portlet:actionURL var="updateProperty">
	<portlet:param name="action" value="updateProperty" />
</portlet:actionURL>
<portlet:actionURL var="updateSecurity">
	<portlet:param name="action" value="updateSecurity" />
</portlet:actionURL>
<portlet:actionURL var="reorderDocs">
	<portlet:param name="action" value="reorder" />
</portlet:actionURL>
<SCRIPT LANGUAGE="javascript">

$(".chosen-select").chosen({width: "100%"});
$(".chosen-select-multi").chosen({width: "100%",no_results_text:"Security role not found."});

function sec_change(val,mode,doc)
{
	var e = document.getElementById(val);
	var values = getSelectValues(e);
	$.ajax({dataType:"json",
		url:"${updateSecurity}",
		data:{"document":doc,
			"groups":JSON.stringify(values),
			"mode":mode}});
}

function getSelectValues(select) {
	var result = [];
	var options = select && select.options;
	var opt;

	for (var i=0, i=options.length; i<i; i++) {
		opt = options[i];

		if (opt.selected) {
			result.push(opt.value || opt.text);
		}
	}
	return result;
}

function edit_document(val)
{
	var e = document.getElementById(val+"_doc_select");
	var selected = e.options[e.selectedIndex].value;
	var source_selector = document.getElementById(val+"_source");
	var selected_type = e.options[e.selectedIndex].getAttribute("data-type");
	var source = source_selector.options[source_selector.selectedIndex].value;
	console.log("Selected type: " + selected_type);
	if (selected_type.includes("form"))
		window.location.href = "https://${server}/uPortal/p/CMSForm.ctf2/max/render.uP?doc="+selected;
	else
		window.location.href = "https://${server}/uPortal/p/cmseditor.ctf4/max/render.uP?doc="+selected+"&source="+source;
}

function new_document(val)
{
	window.location.href = "https://${server}/uPortal/p/cmseditor.ctf2/max/render.uP";
}

function new_form(val)
{
	window.location.href = "https://${server}/uPortal/p/CMSForm.ctf2/max/render.uP";
}

function add_document(val)
{
	var e = document.getElementById(val+"_doc_select");
	var selected = e.options[e.selectedIndex].value;
	var selected_title = e.options[e.selectedIndex].getAttribute("data-title");
	var source_e = document.getElementById(val+"_source");
	var source_selected = source_e.options[source_e.selectedIndex].value;
	$.ajax({dataType:"json",
		url:"${updateDocument}",
		data:{"document":selected,
			"source":source_selected,
			"index":e.length,
			"mode":val}});
	
	var table = document.getElementById(val+"_docs_table");
	table = table.children[1];
	var last_row = table.children[table.children.length - 1];
	var row = new_doc_row(selected_title,selected,val);
	if (last_row != null)
		table.insertBefore(row,last_row.nextSibling);
	else
		table.appendChild(row);
}
function new_doc_row(title,id,mode)
{
	var new_row = document.createElement("TR");
	var content =	'<td><div class="btn-group" role="group"><button onclick="up_document(\''+mode+'\',this);return false" class="btn btn-default">';
	content += '<i class="fa fa-arrow-up"></i></button><button onclick="down_document(\''+mode+'\',this);return false" class="btn btn-default">';
	content += '<i class="fa fa-arrow-down"></i></button></div></td><td>'+title+'</td><td>'+id+'</td>';
	content += '<td><button class="btn btn-danger" onclick="remove_document(\''+mode+'\',this);return false;">Remove</button></td>';
	new_row.innerHTML = content
	return new_row;
}

function remove_document(mode,button)
{
	var table = document.getElementById(mode+"_docs_table")
	var val = button.parentNode.parentNode.rowIndex;
	$.ajax({dataType:"json",
		url:"${updateDocument}",
		data:{"index":val,
			"mode":mode}});
	table.deleteRow(val);
	
}

function up_document(mode,button)
{
	var row = button.parentNode.parentNode.parentNode;
	if (row.rowIndex == 0)
		return false;
	var index = row.rowIndex;
	var table = row.parentNode;
	var rows = table.getElementsByTagName('tr');
	var prev = rows[row.rowIndex - 2];
	table.insertBefore(row,prev);
	$.ajax({dataType:"json",
		url:"${reorderDocs}",
		data:{"index":index,
			"direction":"up",
			"mode":mode}});
}

function down_document(mode,button)
{
	var row = button.parentNode.parentNode.parentNode;
	var index = row.rowIndex;
	var table = row.parentNode;
	var rows = table.getElementsByTagName('tr');
	var next = rows[row.rowIndex + 1];

	table.removeChild(row);
	if (next != null)
		table.insertBefore(row,next);
	else
		table.appendChild(row);

	$.ajax({dataType:"json",
		url:"${reorderDocs}",
		data:{"index":index,
			"direction":"down",
			"mode":mode}});
}

function update_view(mode)
{
	var e = document.getElementById(mode+"_view_type");
	var selected = e.options[e.selectedIndex].value;
	$.ajax({dataType:"json",
		url:"${updateView}",
		data:{"view_type":selected,
			"mode":mode}});
}

function update_property(prop)
{
	var e = document.getElementById(prop);
	var value = e.value;
	$.ajax({dataType:"json",
		url:"${updateProperty}",
		data:{"property":prop,
			"value":value}});
}

function viewChange(mode)
{
	var all = document.getElementsByClassName(mode + "_desc");
	var e = document.getElementById(mode+"_view_type");
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

function OnChange(sources_id,doc_selector_id)
{
	var sources = document.getElementById(sources_id);
	var pages = document.getElementById(doc_selector_id);
	var myindex = sources.selectedIndex;
	var SelValue = sources.options[myindex].value;
	pages.options.length=0;
	pages.options[0] = new Option("Loading...","");
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getPagesWithIndex.json",
		data:{"source":SelValue,"index":doc_selector_id},
		success:populate_pages});
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
		pages.options[i].setAttribute("data-type",data["pages"][i]["docType"]);
	}
	$("#"+CID).trigger("chosen:updated");
}
$(document).ready(function(){
	OnChange("normal_source","normal_doc_select");
	OnChange("maximized_source","maximized_doc_select");
});
</SCRIPT>
