<%@ include file="/WEB-INF/jsp/include.jsp" %><%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="<c:url value='/webjars/jquery/3.3.1-1/jquery.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-ui/1.12.1/jquery-ui.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-form/4.2.1/jquery.form.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/jquery-ui-themes/1.12.1/smoothness/jquery-ui.min.css'/>" />

<script src="<c:url value='/webjars/ckeditor/4.9.1/full/ckeditor.js'/>" type="text/javascript"></script>

<script src="<c:url value='/webjars/jstree/3.3.3/jstree.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/jstree/3.3.3/themes/default/style.min.css'/>" />

<script src="<c:url value='/webjars/chosen/1.8.2/chosen.jquery.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/chosen/1.8.2/chosen.min.css'/>" />

<c:set var="server">${hostname}.usd.edu</c:set>
<c:set var="n"><portlet:namespace/></c:set>
<c:set var="selected" value=""/>
<c:set var="search" value=""/>
<c:set var="path" value="notset"/>
<c:if test="${not empty parameters.get('doc')[0]}">
	<c:set var="selected" value="${parameters.get('doc')[0]}"/>
	<c:set var="search" value="${parameters.get('doc')[0]}"/>
	<c:if test="${fn:contains(search,'.cfm')}">
		<c:set var="path" value="${fn:split(search,'/')}" />
		<c:set var="size" value="${fn:length(path)}"/>
		<c:set var="search" value="${path[size]-1}"/>
		<c:set var="end" value="${fn:length(search)-4}" />
		<c:set var="search" value="${fn:substring(search,0,end)}"/>

		<!--${fn:length(path)} ${size}-->
	</c:if>
</c:if>

<style type="text/css">
	#${n}contentForm { min-height: 100px; padding: 10px; margin: 10px; }
	.cke_source { color: #000000; }
</style>

<div style="width:100%;">
	<div style="width:400px; float: left;">

		<div class="form-group">
			<label for="doc_title">Title:</label>
			<input type="text" class="form-control" id="formTitle" name="doc_title">
		</div>
<!--${sources}
<c:forEach var="source" items="${sources}">
	${source}
</c:forEach>
-->

		<div class="form-group">
			<label for="doc_source">Document:</label>
			<select id="doc_source" class="form-control" OnChange='onSourceChange();' title="CMS Source" style="display: none;">
				<option class="form-control" id="doc_source_InternalForms" value="InternalForms" data-saveEnabled="true" data-deleteEnabled="true" selected="selected">InternalForms</option>
			</select>
		</div>
		<input type="search" id="doc_tree_search" class="form-control" value="${search}" placeholder="Search..."></input>
		<div id="doc_tree" style="height: 250px;overflow: hidden;overflow-y: scroll;"></div>
		<div class="form-group" style="display: none;">
			<label for="doc_loc">Selected Path:</label>
			<input id="doc_loc" type="text" class="form-control" name="doc_loc" disabled="disabled">
		</div>
		<div class="form-group">
			<label for="doc_id">Document Name/ID:</label>
			<input id="doc_id" type="text" class="form-control" name="doc_id">
		</div>
		<div class="form-group">
			<label for="doc_search">Search Terms:</label>
			<input id="doc_search" type="text" class="form-control" name="doc_search">
		</div>
		<p>
			<div class="btn-group" role="group" aria-label="Editor actions">
				<button id="load_btn" onclick="load();return false" class="btn btn-default" title="Load Selected Document">Load</button>
				<button id="save_btn" onclick="save();return false" class="btn btn-success" title="save document">Save</button>
				<button id="delete_btn" onclick="delete_form();return false" class="btn btn-danger" title="delete document">Delete</button>
				<button id="new_btn" onclick="newFolder();return false" class="btn btn-info" title="New Folder">New Folder</button>
				<button id="return_btn" href="https://${server}/uPortal/p/cmseditor" class="btn btn-primary">Return</button>
			</div>
		</p>
	</div>
	<div style="margin-left: 410px;">
		<c:set var="n"><portlet:namespace/></c:set>
		<c:set var="selected" value=""/>
		<c:if test="${not empty parameters.get('doc')[0]}">
			<c:set var="selected" value="form:${parameters.get('doc')[0]}"/>
		</c:if>

		<table id="doc_table" class="table table-striped">
			<tbody>
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
		<button onclick="add_control();return false" class="btn btn-info" title="Add another control">
			<i class="fa fa-plus-square"></i>
		</button>
	</div>
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

$(document).ready(function()
{
	onSourceChange();
});

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
//function load()
//{
//	var selector = document.getElementById("formSelector");
//	var myindex = selector.selectedIndex;
//	var doc_id = selector.options[myindex].value;
//	
//	${n}.jQuery.ajax({dataType:"json",
//		url:"/CMSContent/v1/api/getDocument.json",
//		data:{"source":"Internal","id":doc_id},
//		success:update_content});
//}
function load()
{
	//var selector = document.getElementById("docselector");
	//var myindex = selector.selectedIndex;
	//var doc_id = selector.options[myindex].value;
	
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	var doc_id = getNodePath(node);
	if(doc_id.length == 0)
		doc_id = node.text;
	else
		doc_id += "/" + node.text;
	console.log(doc_id);
	
	var source_selector = document.getElementById("doc_source");
	var source_index = source_selector.selectedIndex;
	var source_id = source_selector.options[source_index].value;
	
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getDocument.json",
		data:{"source":source_id,"id":doc_id},
		success:update_content});
}
function update_content(data, textStatus, jqXHR)
{
	var form_title = document.getElementById("formTitle");
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	var form_id = getNodePath(node);
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
function save()
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

	//doc_info['id'] = doc_table.rows[0].cells[5].children[0].value;
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	var doc_id = getNodePath(node);
	if(node.data['type'] == "folder")
		if(doc_id.length == 0)
			doc_id = node.text;
		else
			doc_id += "/" + node.text;

	if(doc_id.length == 0)
		doc_id = document.getElementById("doc_id").value;
	else
		doc_id += "/" + document.getElementById("doc_id").value;
	console.log(doc_id);
	doc_info['id'] = doc_id;
	
	var resp = {};
	resp['label'] = doc_table.rows[0].cells[1].children[0].value;
	resp['type'] = "respType";
	resp['options'] = doc_table.rows[0].cells[3].children[0].value;
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

function onSourceChange()
{
	var source_selector = document.getElementById("doc_source");
	var myindex = source_selector.selectedIndex;
	var source_id = source_selector.options[myindex].value;
	console.log("Changing source to: " + source_id + " can save: " + source_selector.options[myindex].getAttribute("data-saveEnabled"));

	var index = "doc_tree";
	${n}.jQuery("#doc_source").trigger("chosen:updated");
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getPagesWithIndex.json",
		data:{"source":source_id,"index":index},
		success:populate_documents});
	
	if(source_selector.options[myindex].getAttribute("data-saveEnabled") == 'true')
	{
		console.log("Capable of saving. activating save button");
		var save_btn = document.getElementById("save_btn");
		save_btn.removeAttribute("disabled");
	}
	else
	{
		console.log("in capable of saving. deactivating the save button");
		var save_btn = document.getElementById("save_btn");
		save_btn.setAttribute("disabled","disabled");
	}

	if(source_selector.options[myindex].getAttribute("data-deleteEnabled") == 'true')
	{
		console.log("Capable of deleting. activating delete button");
		var save_btn = document.getElementById("delete_btn");
		save_btn.removeAttribute("disabled");
	}
	else
	{
		console.log("in capable of deleting. deactivating the delete button");
		var save_btn = document.getElementById("delete_btn");
		save_btn.setAttribute("disabled","disabled");
	}
}

function populate_documents(data, textStatus, jqXHR)
{
	CID = data["index"]
	${n}.jQuery('#doc_tree').jstree("destroy").empty();
	var doc_tree = document.getElementById(CID);
	var nodes = [];
	var paths = [];
	for( i = 0; i < data["pages"].length; i++ )
	{
		var val = data["pages"][i]['id'];
		if(val.charAt(0) == '/')
			val = val.substring(1);
		paths.push(val);
	}
	
	var source_selector = document.getElementById("doc_source");
	var myindex = source_selector.selectedIndex;
	nodes = getNodes(paths,source_selector.options[myindex].value,"");
	nodes['state'] = 'opened';
	${n}.jQuery('#doc_tree').jstree({'core' : {'check_callback' : true, 'multiple': false, 'data' : nodes},"plugins":["search"]});
	${n}.jQuery('#doc_tree').on('changed.jstree', function (e, data)
	{
		var i, j, r = [];
		for(i = 0, j = data.selected.length; i < j; i++)
		{
			
			r.push(getNodePath(data.instance.get_node(data.selected[i])));
		}
		var doc_loc = document.getElementById("doc_loc");
		doc_loc.value = r[0];
		
		var doc_id = document.getElementById("doc_id");
		if(data.instance.get_node(data.selected[0]).data['type']=='document')
			doc_id.value = data.instance.get_node(data.selected[0]).text;
	})
	var to = false;
	${n}.jQuery('#doc_tree_search').keyup(function () {
		console.log("Key stroke up");
		if(to) { clearTimeout(to); }
		to = setTimeout(function () {
			var v = ${n}.jQuery('#doc_tree_search').val();
			${n}.jQuery('#doc_tree').jstree(true).search(v);
		}, 250);
	});
}


function getNodes(val,name,parent)
{
	var keys = {}
	var nodes = []
	var files = []
	for(i = 0; i < val.length; i++)
	{
		var parts = val[i].split('/');
		if (parts.length > 1)
		{
			if (parts[0] in keys)
			{
				keys[parts[0]].push(val[i].substring(val[i].indexOf('/')+1));
			}
			else
			{
				keys[parts[0]] = [val[i].substring(val[i].indexOf('/')+1)];
			}
		}
		else
		{
			//strip .cfm from files:
			var filename = val[i];
			if (filename.substring(filename.length-4) == '.cfm')
				filename = filename.substring(0,filename.length-4);
			files.push({"text":filename,"icon":"fa fa-file","data":{"type":"document","path":parent+'/'+val[i]},"id":parent+val[i]});
		}
	}
	for(key in keys)
	{
		if (parent.length == 0)
			nodes.push(getNodes(keys[key],key,key));
		else
			nodes.push(getNodes(keys[key],key,parent+key));
	}
	for(file in files)
	{
		nodes.push(files[file]);
	}
	return {"text":name,"children":nodes,"data":{"type":"folder"}};
}
function getNodePath(node)
{
	//var parents = data.instance.get_node(data.selected[i]).parents;
	var parents = node.parents;
	var pathArray = [];
	var n;
	for(n = 2; n < parents.length; n++)
	{
			pathArray.push(${n}.jQuery('#doc_tree').jstree(true).get_node(parents[(parents.length - 1 ) - n]).text);
	}
	//pathArray.push(data.instance.get_node(data.selected[i]).text);
	return pathArray.join('/');
}
function newFolder()
{
	var fname = window.prompt("Enter new folder name:","New Folder");
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	if (node.data['type'] == 'document')
		var parent = node.parent;
	else
		var parent = ${n}.jQuery("#doc_tree").jstree('get_selected')[0];
	console.log(parent);
	var position = 'inside';
	var newNode = {"text":fname,"data":"folder"}
	${n}.jQuery("#doc_tree").jstree().create_node('#'+parent, newNode, position, false, false);
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
