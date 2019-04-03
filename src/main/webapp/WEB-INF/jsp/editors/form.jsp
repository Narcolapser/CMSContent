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

<script src="https://vuejs.org/js/vue.js"></script>
<script src="/CMSContent/components/form-field.js"></script>
<script src="/CMSContent/components/form-fields.js"></script>

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
		<c:set var="search" value="${path[size-1]}"/>
		<c:set var="end" value="${fn:length(search)-4}" />
		<c:set var="search" value="${fn:substring(search,0,end)}"/>

		<!--${fn:length(path)} ${size}-->
	</c:if>
</c:if>

<style type="text/css">
	#${n}contentForm { min-height: 100px; padding: 10px; margin: 10px; }
	.cke_source { color: #000000; }
</style>

<div style="width:100%;" id="app">
	<div style="width:450px; float: left;">

		<div class="form-group">
			<label for="doc_title">Title:</label>
			<input type="text" class="form-control" id="formTitle" name="doc_title">
		</div>
		<div class="form-group">
			<label for="doc_source">Document:</label>
			<select id="doc_source" class="form-control" OnChange='onSourceChange();' title="CMS Source" style="display: none;">
				<option class="form-control" id="doc_source_InternalForms" value="InternalForms" data-saveEnabled="true" data-deleteEnabled="true" selected="selected">InternalForms</option>
			</select>
		</div>
		<div id="move_div" class="form-group" style="display: none;">
			<label for="move_from">Moving from:</label>
			<div>
				<input id="move_from" type="text" disabled="disabled" style="width: 301px;"></input>
				<div class="btn-group" role="group">
					<button id="move_confirm" class="btn btn-warning" onclick="confirm_move();return false" title="Click to move">Confirm</button>
					<button id="move_cancel" class="btn btn-danger" onclick="cancel_move();return false" title="Click to cancel">Cancel</button>
				</div>
			</div>
		</div>
		<input type="search" id="doc_tree_search" class="form-control" value="${search}" placeholder="Search..."></input>
		<div id="doc_tree" style="height: 250px;overflow: hidden;overflow-y: scroll;"></div>
		<div class="form-group" style="display: none;">
			<label for="doc_loc">Selected Path:</label>
			<input id="doc_loc" type="text" class="form-control" name="doc_loc" disabled="disabled">
		</div>
		<div class="form-group">
			<label for="doc_id">Document ID:</label>
			<input id="doc_id" type="text" class="form-control" name="doc_id" disabled="disabled">
		</div>
		<div class="form-group">
			<label for="doc_search">Search Terms:</label>
			<input id="doc_search" type="text" class="form-control" name="doc_search">
		</div>
		<p>
			<div class="btn-group" role="group" aria-label="Editor actions">
				<button id="load_btn" onclick="load();return false" class="btn btn-default" title="Load Selected Document">Load</button>
				<button id="save_btn" onclick="save();return false" class="btn btn-success" title="save document">Save</button>
				<button id="move_btn" onclick="move_doc();return false" class="btn btn-warning" title="Move Document" >Move</button>
				<button id="delete_btn" onclick="delete_doc();return false" class="btn btn-danger" title="delete document">Delete</button>
				<button id="new_btn" onclick="newFolder();return false" class="btn btn-info" title="New Folder">New Folder</button>
				<a id="return_btn" href="/uPortal/p/cmseditor" class="btn btn-primary">Return</a>
			</div>
		</p>
	</div>
	<div style="margin-left: 460px;">
		<c:set var="n"><portlet:namespace/></c:set>
		<c:set var="selected" value=""/>
		<c:if test="${not empty parameters.get('doc')[0]}">
			<c:set var="selected" value="form:${parameters.get('doc')[0]}"/>
		</c:if>

		<table id="responder_table" class="table table-striped">
			<thead>
				<th>Form Submission Action Type</th>
				<th></th>
				<th>Form Submission Action Configuration</th>
				<th></th>
			</thead>
			<tbody id="responder_body">
			</tbody>
		</table>

		<button onclick="add_responder();return false" class="btn btn-info" title="Add another control">
			<i class="fa fa-plus-square"></i>
		</button>
		<hr/>

		<c:set var="form_id"></c:set>
		<c:if test="${not empty parameters.get('doc')[0]}">
			<c:set var="form_id">${parameters.get('doc')[0]}</c:set>
		</c:if>
		<form-fields ref="fields" form_id="${form_id}"></form-fields>
	</div>
</div>

<c:set var="responder_row">
	<tr>
		<td>
			<select id="formResp" OnChange="responder_change(this)" class="form-control">
				<c:forEach var="res" items="${responders}">
					<c:if test="${res.autoRespond() == false}">
						<option value="${res.getName()}" data-option="${res.getOptionInfo()}">${res.getName()}</option>
					</c:if>
				</c:forEach>
			</select>
		</td>
		<td id="formRespOptionLabel">
			Responder Option:
		</td>
		<td>
			<input id="formRespOption" class="form-control"/>
		</td>
		<td><button class="btn btn-danger" onclick="remove_responder(this);return false;">Remove</button></td>
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

function optionChange(selector)
{
	var reqable = selector.options[selector.selectedIndex].getAttribute("data-reqable") == 'true';
	
	selector.parentNode.parentNode.children[4].children[0].disabled = !reqable;
	selector.parentNode.parentNode.children[4].children[0].checked = false;
}


function remove_responder(responder)
{
	responder.parentNode.parentNode.parentNode.deleteRow(responder.parentNode.parentNode.rowIndex-1);
}

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
//}
function load()
{
	//var selector = document.getElementById("docselector");
	//var myindex = selector.selectedIndex;
	//var doc_id = selector.options[myindex].value;
	
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	var doc_id = node.id;
	
	var source_selector = document.getElementById("doc_source");
	var source_index = source_selector.selectedIndex;
	var source_id = source_selector.options[source_index].value;
	
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v2/documents/"+source_id+"/"+doc_id,
		success:update_content});
}
function update_content(data, textStatus, jqXHR)
{
	if (Object.keys(data).length === 0)
	{
		console.log("empty object, skipping");
		return;//odd error. skip loading this, there is no data.
	}
	var form_title = document.getElementById("formTitle");
	var form_id = document.getElementById("doc_id");
	var form_search = document.getElementById("doc_search");
	var form_resp = document.getElementById("formResp");

	form_title.value=data.title;
	form_id.value=data.id;

	form_search.value=data.keyTerms;
	var form = JSON.parse(data.content);
	var rtable = document.createElement('tbody');
	rtable.id = "responder_body";
	for(var i=0; i<form.length; i++)
	{
		if(form[i]["type"] == "respType")
		{
			load_resp(rtable,form[i]['label'],form[i]['options']);
			continue;
		}
	}
	app.$refs.fields.load(form_id.value);
	var old_rbody = document.getElementById("responder_body");
	old_rbody.parentNode.replaceChild(rtable, old_rbody);
}
function load_resp(rtable,rtype,roption)
{
	var row = rtable.insertRow(rtable.length);
	row.innerHTML = `${responder_row}`;
	for(var i = 0; i < row.cells[0].children[0].options.length; i ++)
		if(row.cells[0].children[0].options[i].value == rtype)
			row.cells[0].children[0].options[i].selected = true;
	row.cells[2].children[0].value = roption;
}

function add_control()
{
	var table = document.getElementById("control_table");
	var row = table.insertRow(table.length);
	row.innerHTML = `${control_row}`;
}

function add_responder()
{
	var table = document.getElementById("responder_table");
	var row = table.insertRow(table.length);
	row.innerHTML = `${responder_row}`;
}

function save()
{
	//assemble document information:
	var form_title = document.getElementById("formTitle");
	form_title = form_title.value;
	
	//assemble document json body
	var table = document.getElementById("control_table");
	var form_json = [];
//	for (var i=1,row; row = table.rows[i];i++)
//		form_json.push(get_form_entry(row));
	console.log(app.$refs);
	var fields = app.$refs.fields.get_fields();
	console.log(fields);
	for (var i=0; i < fields.length; i++)
		form_json.push(fields[i]);

	//assemble responder options.
	var responder_table = document.getElementById("responder_table");
	for (var i=1,row; row = responder_table.rows[i]; i++)
		form_json.push(get_responder_config(row));

	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	var path = getNodePath(node);

	//get key terms:
	var keyterms = document.getElementById("doc_search").value;
	data = JSON.stringify({"content":form_json,"id":get_doc_id(),'title':form_title,
							'source':'InternalForms','docType':'form',
							'keyTerms':keyterms,'removed':false,'path':path})
	while (data.includes('  '))
		data = data.replace('  ',' ');
	
	console.log(data);
//	${n}.jQuery.ajax({dataType:"json",
//		type: "POST",
//		url:"/CMSContent/v2/documents/InternalForms/"+get_doc_id(),
//		data:{"document":data},
//		success:doc_saved});
}
function get_doc_id()
{
	var id = document.getElementById("doc_id").value;
	
	if (id == "")
	{
		id = get_random_id(document.getElementById("doc_title").value);
		document.getElementById("doc_id").value = id;
	}
	
	return id;
}
function get_form_entry(row)
{
	var label, tp, ops, entry={};
	label = row.cells[1].children[1].value;
	tp = row.cells[2].children[1];
	tp = tp.options[tp.selectedIndex].value;
	ops = row.cells[3].children[1].value;
	req = row.cells[4].children[0].checked;
	entry['label'] = label;
	entry['type'] = tp;
	entry['options'] = ops;
	entry['required'] = req;
	return entry;
}
function get_responder_config(resp_row)
{
	var resp ={};
	//resp['label'] = resp_row.cells[0].children[0].options[resp_row.cells[0].children[0].selectedIndex];
	resp['label'] = resp_row.cells[0].children[0].selectedOptions[0].value;
	resp['type'] = "respType";
	resp['options'] = resp_row.cells[2].children[0].value;
	return resp;
}
function doc_saved(data, textStatus, jqXHR)
{
	alert("form saved");
}



function get_random_id(title)
{
	var ret = "";
	for(var i= 0; i<5; i++)
		ret += Math.floor(Math.random() * 10)
	ret += title.toLowerCase().replace(/[^a-zA-Z0-9-_]/g, '');
	return ret;
}

function move_doc()
{
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	if(node.data['type'] == "folder")
	{
		alert("Cannot move folders");
	}
	else
	{
		var path = getNodePath(node);

		var buttons = ["load_btn","save_btn","move_btn","delete_btn","new_btn"]
		for(var i=0; i < buttons.length; i++)
		{
			var btn = document.getElementById(buttons[i]);
			var was_disabled = btn.getAttribute("disabled");
			console.log(was_disabled);
			if (was_disabled == null || was_disabled == "")
				btn.setAttribute("data-was_disabled","false");
			else
				btn.setAttribute("data-was_disabled","true");
			btn.setAttribute("disabled","disabled");
		}
		var move_div = document.getElementById("move_div");
		move_div.removeAttribute("style");
		var move_from = document.getElementById("move_from");
		move_from.value = path;
		move_from.setAttribute("data-doc_id",document.getElementById("doc_id").value);
	}
}

function confirm_move()
{
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	var path = getNodePath(node);
	if(node.data['type'] == "folder")
		if(path.length == 0)
			path = node.text;
		else
			path += "/" + node.text;
	path += "/";

	var id = move_from.getAttribute("data-doc_id");
	var source = "InternalForms";
	
	${n}.jQuery.ajax({dataType:"json",
		type: "POST",
		url:"/CMSContent/v2/documents/"+source+"/"+id,
		data:{"path":path},
		success:doc_moved});
}
function doc_moved(data, textStatus, jqXHR)
{
	onSourceChange();
	cancel_move();
	alert("document moved");
}

function cancel_move()
{
	var buttons = ["load_btn","save_btn","move_btn","delete_btn","new_btn"]
	for(var i=0; i < buttons.length; i++)
	{
		var btn = document.getElementById(buttons[i]);
		var was_disabled = btn.getAttribute("data-was_disabled");
		console.log(was_disabled);
		if (was_disabled == "true")
			btn.setAttribute("disabled","disabled");
		else
			btn.removeAttribute("disabled");
	}
	var move_div = document.getElementById("move_div");
	move_div.setAttribute("style","display: none");
}

function delete_doc()
{
	var doc_source = document.getElementById("doc_source").value;
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	var doc_id = document.getElementById("doc_id").value;
	
	if(confirm('Are you sure you want to delete "'+doc_id+'"?'))
		${n}.jQuery.ajax({dataType:"json",
			type:"DELETE",
			url:"/CMSContent/v2/documents/"+doc_source+"/"+doc_id,
			success:doc_deleted});
}

function doc_deleted(data,textStatus, jqXHR)
{
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	document.getElementById(node.id).style.display = 'none';
	console.log("Deleted node");
}

function onSourceChange()
{
	var source_selector = document.getElementById("doc_source");
	var myindex = source_selector.selectedIndex;
	var source_id = source_selector.options[myindex].value;

	${n}.jQuery("#doc_source").trigger("chosen:updated");
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v2/documents/"+source_id,
		success:populate_documents});

	var save_btn = document.getElementById("save_btn");
	save_btn.removeAttribute("disabled");

	var save_btn = document.getElementById("delete_btn");
	save_btn.removeAttribute("disabled");
}

function populate_documents(data, textStatus, jqXHR)
{
	console.log(data);
	${n}.jQuery('#doc_tree').jstree("destroy").empty();
	var doc_tree = document.getElementById("doc_tree");
	var nodes = [];
	var paths = [];
	for( i = 0; i < data.length; i++ )
	{
		var val = data[i]['path'];
		if (val == null)
		{
//			console.log(data[i]);
			val = '';
		}
		else
			if(val.charAt(0) == '/')
				val = val.substring(1);
		paths.push({'path':val,'title':data[i]['title'],'id':data[i]['id']});
	}
	
	var source_selector = document.getElementById("doc_source");
	var myindex = source_selector.selectedIndex;
	nodes = getNodes(paths,source_selector.options[myindex].value,"");
	nodes['state'] = 'opened';
	${n}.jQuery('#doc_tree').jstree({'core' : {'check_callback' : true, 'multiple': false, 'data' : nodes},"search":{'case_insensitive':true,'show_only_matches':true},"plugins":["search"]});
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
		{
			console.log(data.instance.get_node(data.selected[0]));
			doc_id.value = data.instance.get_node(data.selected[0]).id;
		}
		else
			doc_id.value = "";
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
	var keys = {};
	var nodes = [];
	var files = [];
	for(i = 0; i < val.length; i++)
	{
		//console.log(val[i]);
		try
		{
			var parts = val[i]['path'].split('/');
			if (parts.length > 1)
			{
				if (parts[0] in keys)
				{
					keys[parts[0]].push({'path':val[i]['path'].substring(val[i]['path'].indexOf('/')+1),
						'id':val[i]['id'],'title':val[i]['title']});
				}
				else
				{
					keys[parts[0]] = [{'path':val[i]['path'].substring(val[i]['path'].indexOf('/')+1),
						'id':val[i]['id'],'title':val[i]['title']}];
				}
			}
			else
			{
				files.push({"text":val[i]['title'],
							"icon":"fa fa-file",
							"data":{"type":"document","path":parent+'/'+val[i]},"id":val[i]['id']});
			}
		}
		catch{
			console.log(val[i]);
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
	var newNode = {"text":fname,"data":{"type":"folder"}}
	${n}.jQuery("#doc_tree").jstree().create_node('#'+parent, newNode, position, false, false);
}

function responder_change(resp_selector)
{
	console.log("resp changed");
	resp_option = resp_selector.options[resp_selector.selectedIndex].getAttribute("data-option");
	console.log(resp_option);
	resp_selector.parentNode.parentNode.children[1].innerHTML = resp_option;
}

$(document).ready(function(){
	var ret_button = document.getElementById("return_btn");
	ret_button.href=document.referrer;
	<c:if test="${not empty parameters.get('doc')[0]}">
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v2/documents/InternalForms/${parameters.get('doc')[0]}",
		success:update_content});
	</c:if>
});

var app = new Vue({
	el: '#app',
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
