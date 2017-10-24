<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"> </script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/jquery.form.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/chosen.jquery.js" type="text/javascript"></script>
<link rel="stylesheet" href="/CMSContent/css/chosen.css">
<c:set var="n"><portlet:namespace/></c:set>

<table id="doc_table" class="table table-striped">
	<tbody>
		<tr>
			<td>Form:</td>
			<td><select class="chosen-select">
				<option value="new" selected="selected">New Form</option>
				<c:forEach var="form" items="${forms}">
					<option value="${form.id}">${form.title}</option>
				</c:forEach>
			</select></td>
			<td>Form Name:</td>
			<td><input type="text"></input></td>
			<td>Form Path:</td>
			<td><input type="text"></input></td>
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
	<tbody>
	</tbody>
</table>
<button onclick="add_control();return false" class="btn btn-default">
	<i class="fa fa-plus-square" colspan="4"></i>
</button>
<button onclick="update();return false" class="btn btn-default">Save</button>

<c:set var="control_row">
	<tr>
		<td class="pos_col" >
			<div class="btn-group" role="group">
				<button onclick="up_control(this);return false" class="btn btn-default">
					<i class="fa fa-arrow-up"></i>
				</button>
				<button onclick="down_control(this);return false" class="btn btn-default">
					<i class="fa fa-arrow-down"></i>
				</button>
			</div>
		</td>
		<td>
			<span>Label: </span><input type="text"></input>
		</td>
		<td>
			<span>Type: </span>
			<select>
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
			<span>Options: </span><input type="text"></input>
		</td>
		<td><button class="btn btn-danger" onclick="remove_document(this);return false;">Remove</button></td>
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
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/saveForm.json",
		data:{"form":JSON.stringify({"form":form_json,"doc":doc_info})},
		success:form_saved});
}
function form_saved(data, textStatus, jqXHR)
{
	alert("form saved");
}
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
