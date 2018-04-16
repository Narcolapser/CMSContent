<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="<c:url value='/webjars/jquery/3.3.1-1/jquery.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-ui/1.12.1/jquery-ui.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-form/4.2.1/jquery.form.min.js'/>" type="text/javascript"></script>

<script src="<c:url value='/webjars/chosen/1.8.2/chosen.jquery.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/chosen/1.8.2/chosen.min.css'/>" />

<c:set var="n"><portlet:namespace/></c:set>
<c:set var="selected" value=""/>
<c:if test="${not empty parameters.get('doc')[0]}">
	<c:set var="selected" value="form:${parameters.get('doc')[0]}"/>
</c:if>

<table id="doc_table" class="table table-striped">
	<tbody>
		<tr>
			<td>Form:</td>
			<td>
				<select id="formSelector" class="chosen-select" OnChange='OnChange();' data-placeholder="Select document...">
					<c:forEach var="form" items="${Internal}">
						<c:if test="${form.docType eq 'form'}">
							<c:choose>
								<c:when test="${form.id == selected}">
									<option selected="selected" value="${form.id}">${form.title}</option>
								</c:when>
								<c:otherwise>
									<option value="${form.id}">${form.title}</option>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td>Form Name:</td>
			<td><input id="formName" type="text" class="form-control"></input></td>
			<td>Form Path:</td>
			<td><input id="formPath" type="text" class="form-control"></input></td>
		</tr>
		<tr>
			<td>Form Response Type:</td>
			<td>
				<select id="formResp" OnChange="responder_change()" class="form-control">
					<c:forEach var="res" items="${responders}">
						<option value="${res}">${res}</option>
					</c:forEach>
				</select>
			</td>
			<td id="formRespOptionLabel">
				Responder Option:
			</td>
			<td colspan="3">
				<input id="formRespOption" class="form-control"/>
			</td>
		</tr>
	</tbody>
</table>

<table id="control_table" class="table table-striped">
	<thead>
		<th>Position</th>
		<th>Label</th>
		<th>Type</th>
		<th>Options (if applicable, seperated by commas)</th>
		<td></td>
	</thead>
	<tbody id="control_body">
	</tbody>
</table>

<div class="btn-group" role="group">
	<button onclick="add_control();return false" class="btn btn-info" title="Add another control">
		<i class="fa fa-plus-square" colspan="4"></i>
	</button>
	<button onclick="update();return false" class="btn btn-success" title="save form">Save</button>
	<button onclick="delete_form();return false" class="btn btn-danger" title="delete form">Delete</button>
	<a id="return_btn" href="https://dev-uportal.usd.edu/uPortal/" class="btn btn-primary" title="return to config screen">Return</a>
</div>

<c:set var="control_row">
	<tr>
		<td class="pos_col" >
			<div class="btn-group" role="group">
				<button onclick="move_control('up',this);return false" class="btn btn-default">
					<i class="fa fa-arrow-up"></i>
				</button>
				<button onclick="move_control('down',this);return false" class="btn btn-default">
					<i class="fa fa-arrow-down"></i>
				</button>
			</div>
		</td>
		<td>
			<span>Label: </span><input name="label" type="text" class="form-control"></input>
		</td>
		<td>
			<span>Type: </span>
			<select name="type" class="form-control">
				<option value="text">Text</option>
				<option value="select">Drop Down</option>
				<option value="bool">True/False</option>
				<option value="checkbox">Checkbox</option>
				<option value="radiobutton">Radio Button</option>
				<option value="hr">Horrizontal Line</option>
				<option value="label">Label</option>
			</select>
		</td>
		<td>
			<span>Options: </span><input name="options" type="text" class="form-control"></input>
		</td>
		<td><button class="btn btn-danger" onclick="remove_control(this);return false;">Remove</button></td>
	</tr>
</c:set>

<SCRIPT LANGUAGE="javascript">
var ${n} = ${n} || {};
<c:choose>
	<c:when test="${!usePortalJsLibs}">
		${n}.jQuery = jQuery.noConflict(true);
	</c:when>
	<c:otherwise>
		${n}.jQuery = up.jQuery;
	</c:otherwise>
</c:choose>
var $ = ${n}.jQuery;
$(".chosen-select").chosen();

function remove_control(control)
{
	control.parentNode.parentNode.parentNode.deleteRow(control.parentNode.parentNode.rowIndex-1);
}
function move_control(dir,control)
{
	//alert("move: " + dir);
	var row = control.parentNode.parentNode.parentNode;
	var table = row.parentNode;
	//alert(table);
	var index = row.rowIndex;
	table.deleteRow(row.rowIndex-1);
	var nrow=null;
	if (dir=='up')
		nrow = table.insertRow(index-2);
	else
		nrow = table.insertRow(index);
	nrow.innerHTML = row.innerHTML;
	nrow.children[1].children[1].value = row.children[1].children[1].value;
	nrow.children[3].children[1].value = row.children[3].children[1].value;
	nrow.children[2].children[1].selectedIndex = row.children[2].children[1].selectedIndex;
}
function OnChange()
{
	var selector = document.getElementById("formSelector");
	var myindex = selector.selectedIndex;
	var doc_id = selector.options[myindex].value;
	
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getDocument.json",
		data:{"source":"Internal","id":doc_id},
		success:update_content});
}
function update_content(data, textStatus, jqXHR)
{
	var form_title = document.getElementById("formName");
	var form_id = document.getElementById("formPath");
	var form_resp = document.getElementById("formResp");
	form_title.value=data.doc.title;
	form_id.value=data.doc.id;
	var form = JSON.parse(data.doc.content);
	//alert("Form: " + form);
	var new_tbody = document.createElement('tbody')
	new_tbody.id = "control_body";
	for(var i=0; i<form.length; i++)
	{
		//alert("Form entry: " + JSON.stringify(form[i]));
		//{"options":"1223","label":"443","type":"label"}
		if(form[i]["type"] == "respType")
		{
			for(var j=0; j<form_resp.options.length; j++)
				if (form[i]["label"] == form_resp.options[j].value)
					form_resp.selectedIndex = j;
			continue;
		}
		var row = new_tbody.insertRow(new_tbody.length);
		row.innerHTML = `${control_row}`;
		row.children[1].children[1].value=form[i]["label"];
		row.children[3].children[1].value=form[i]["options"];
		for(var j=0; j< row.children[2].children[1].options.length; j++)
			if (row.children[2].children[1].options[j].value==form[i]["type"])
			{
				row.children[2].children[1].selectedIndex = j;
				break;
			}
	}
	var old_tbody = document.getElementById("control_body");
	old_tbody.parentNode.replaceChild(new_tbody, old_tbody)
}

function add_control()
{
	var table = document.getElementById("control_table");
	var row = table.insertRow(table.length);
	row.innerHTML = `${control_row}`;
}
function update()
{
	var table = document.getElementById("control_table");
	var form_json = [];
	for (var i=1,row; row = table.rows[i];i++)
	{
		var label, tp, ops, entry={};
		label = row.cells[1].children[1].value;
		tp = row.cells[2].children[1];
		tp = tp.options[tp.selectedIndex].value;
		ops = row.cells[3].children[1].value;
		entry['label'] = label;
		entry['type'] = tp;
		entry['options'] = ops;
		form_json.push(entry);
	}
	var doc_table = document.getElementById("doc_table");
	var doc_info = {};
	doc_info['name'] = doc_table.rows[0].cells[3].children[0].value;
	doc_info['id'] = doc_table.rows[0].cells[5].children[0].value;
	var resp = {};
	resp['label'] = doc_table.rows[1].cells[1].children[0].value;
	resp['type'] = "respType";
	resp['options'] = doc_table.rows[1].cells[3].children[0].value;
	form_json.push(resp);
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/saveForm.json",
		data:{"form":JSON.stringify({"form":form_json,"doc":doc_info})},
		success:form_saved});
}
function form_saved(data, textStatus, jqXHR)
{
	alert("form saved");
}

function delete_form()
{

	var doc_table = document.getElementById("doc_table");
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/delete.json",
		data:{"sanitybit":31415,"id":doc_table.rows[0].cells[5].children[0].value},
		success:form_deleted});
}
function form_deleted(data,textStatus, jqXHR)
{
	alert("form Deleted");
}

function responder_change()
{
	console.log("resp changed");
	var resp_selector = document.getElementById("formResp");
	var resp_name = resp_selector.options[resp_selector.selectedIndex].value;
	console.log(resp_name);
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getResponder.json",
		data:{"name":resp_name},
		success:responder_update});
}
function responder_update(data, textStatus, jqXHR)
{
	var resp_option = document.getElementById("formRespOptionLabel");
	resp_option.innerHTML = data['responder']['optionInfo'];
}

$(document).ready(function(){
	var ret_button = document.getElementById("return_btn");
	ret_button.href=document.referrer;
	<c:if test="${not empty parameters.get('doc')[0]}">
	OnChange();
	</c:if>
});
</script>


<!---

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
-->
