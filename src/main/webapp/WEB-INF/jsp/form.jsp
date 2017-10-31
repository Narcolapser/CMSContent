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
			<td>
				<select id="formSelector" class="chosen-select" OnChange='OnChange();' data-placeholder="Select document...">
					<c:forEach var="form" items="${Internal}">
						<c:if test="${form.id.contains('form:')}">
							<option value="${form.id}">${form.title}</option>
						</c:if>
					</c:forEach>
				</select>
			</td>
			<td>Form Name:</td>
			<td><input id="formName" type="text"></input></td>
			<td>Form Path:</td>
			<td><input id="formPath" type="text"></input></td>
			<td>Form Response Type:</td>
			<td>
				<select id="formResp">
					<c:forEach var="res" items="${responders}">
						<option value="${res}">${res}</option>
					</c:forEach>
				</select>
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
			<span>Label: </span><input name="label" type="text"></input>
		</td>
		<td>
			<span>Type: </span>
			<select name="type" >
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
			<span>Options: </span><input name="options" type="text"></input>
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

function OnChange()
{
	var selector = document.getElementById("formSelector");
	var myindex = selector.selectedIndex;
	var doc_id = selector.options[myindex].value;
	
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getDocument.json",
		data:{"source":"Internal","id":doc_id},
		success:update_text});
}
function update_text(data, textStatus, jqXHR)
{
	var form_title = document.getElementById("formName");
	var form_id = document.getElementById("formPath");
	var form_resp = document.getElementById("formResp");
	form_title.value=data.doc.title;
	form_id.value=data.doc.id.split(":")[1];
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
	resp['label'] = doc_table.rows[0].cells[7].children[0].value;
	resp['type'] = "respType";
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

<c:if test="${not empty parameters.get('doc')[0]}">

</c:if>
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
