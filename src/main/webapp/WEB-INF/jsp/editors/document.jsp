<%@ include file="/WEB-INF/jsp/include.jsp" %>
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
			<input type="text" class="form-control" id="doc_title" name="doc_title">
		</div>
<!--${sources}
<c:forEach var="source" items="${sources}">
	${source}
</c:forEach>
-->

		<div class="form-group">
			<label for="doc_source">Document:</label>
			<select id="doc_source" class="form-control" OnChange='onSourceChange();' title="CMS Source">
				<c:forEach var="source" items="${sources}">
					<c:choose>
						<c:when test="${parameters.get('source')[0] == source}">
							<option class="form-control" id="doc_source_${source}" value="${source}" data-saveEnabled="${saveEnabled[source]}" data-deleteEnabled="${deleteEnabled[source]}" selected="selected">${source}</option>
						</c:when>
						<c:otherwise>
							<option class="form-control" id="doc_source_${source}" value="${source}" data-saveEnabled="${saveEnabled[source]}" data-deleteEnabled="${deleteEnabled[source]}">${source}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
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
				<button id="save_btn" onclick="save();return false" class="btn btn-success" title="save document" disabled="disabled">Save</button>
				<button id="delete_btn" onclick="delete_doc();return false" class="btn btn-danger" title="delete document">Delete</button>
				<button id="new_btn" onclick="newFolder();return false" class="btn btn-info" title="New Folder">New Folder</button>
				<a id="return_btn" href="https://${server}/uPortal/p/cmseditor" class="btn btn-primary">Return</a>
				<a id="att_btn" href="https://${server}/uPortal/p/attman" class="btn btn-default" title="Go to attachments manager"><i class="fa fa-paperclip"></i></a>
			</div>
		</p>
	</div>
	<div style="margin-left: 410px;">
		<textarea id="${n}content" name="content">put content here.</textarea>
	</div>
</div>


<SCRIPT LANGUAGE="javascript">
//$(".chosen-select").chosen();

var ${n} = ${n} || {};
<c:choose>
	<c:when test="${!usePortalJsLibs}">
		${n}.jQuery = jQuery.noConflict(true);
	</c:when>
	<c:otherwise>
		${n}.jQuery = up.jQuery;
	</c:otherwise>
</c:choose>
${n}.jQuery(function(){
var $ = ${n}.jQuery;
$(document).ready(function(){
	CKEDITOR.dtd.$removeEmpty['span'] = false;  // allow empty span elements for font-awesome
	// Create an CKEditor 4.x Editor
	CKEDITOR.replace('${n}content', {
		toolbarGroups : [
			{ name: 'document',    groups: [ 'mode', 'document', 'doctools' ] },
			{ name: 'clipboard',   groups: [ 'clipboard', 'undo' ] },
			{ name: 'editing',     groups: [ 'find', 'selection', 'spellchecker' ] },
			{ name: 'tools' },
			{ name: 'others' },
			{ name: 'about' },
			'/',
			{ name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
			{ name: 'paragraph',   groups: [ 'list', 'indent', 'blocks', 'align' ] },
			{ name: 'links' },
			'/',
			{ name: 'styles' },
			{ name: 'colors' },
			{ name: 'insert' }
		],
		allowedContent: true
	});
	$('#${n}contentForm').ajaxForm(function() {
		//alert("Page updated.");
	});
	var ret_button = document.getElementById("return_btn");
	ret_button.href=document.referrer;
	
	//Setup docTree
	$('#doc_tree').on('changed.jstree', function (e, data)
	{
		console.log("jstree selected");
		var i, j, r = [];
		for(i = 0, j = data.selected.length; i < j; i++)
		{
			r.push(data.instance.get_node(data.selected[i]).text);
		}
		console.log(r.join(','));
	}).jstree();
	
	//make sure everything lines up.
	onSourceChange();
	
	
	
	});
});


function addtext()
{
	setText("<p>new</br>text</p>");
}
function setText(val)
{
	CKEDITOR.instances["${n}content"].setData(val);
}
function OnChange()
{
	load();
}
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
	setText("<p>Loading...</p>");
	
	var source_selector = document.getElementById("doc_source");
	var source_index = source_selector.selectedIndex;
	var source_id = source_selector.options[source_index].value;
	
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getDocument.json",
		data:{"source":source_id,"id":doc_id},
		success:update_text});
}
function update_text(data, textStatus, jqXHR)
{
	setText(data.doc.content);
	var doc_title = document.getElementById("doc_title");
	var doc_id = document.getElementById("doc_id");
	var doc_source = document.getElementById("doc_source");
	var doc_search = document.getElementById("doc_search");
	doc_title.value=data.doc.title;
	var idsplit = data.doc.id.split('/');
	var filename = idsplit[idsplit.length-1];
	if (filename.substring(filename.length-4) == '.cfm')
		filename = filename.substring(0,filename.length-4);
	doc_id.value=filename;
	doc_source.value=data.doc.source;
	doc_search.value=data.doc.keyTerms;
}
function update()
{
	save();
}
function save()
{
	var node = ${n}.jQuery("#doc_tree").jstree("get_selected",true)[0];
	var doc_title = document.getElementById("doc_title").value;
	var doc_source = document.getElementById("doc_source").value;
	var doc_search = document.getElementById("doc_search").value;
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
	
	
	${n}.jQuery.ajax({dataType:"json",
		type: "POST",
		url:"/CMSContent/v1/api/saveDoc.json",
		data:{"content":CKEDITOR.instances["${n}content"].getData(),
			"doc_id":doc_id,
			"doc_title":doc_title,
			"doc_source":doc_source,
			"doc_search":doc_search},
		success:doc_saved});
}
function doc_saved(data, textStatus, jqXHR)
{
	alert("document saved");
}

function delete_doc()
{
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
	
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/delete.json",
		data:{"sanitybit":31415,"id":doc_id},
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
<!-- empty check: ${not empty parameters.get('doc')[0]} -->
<c:if test="${not empty parameters.get('doc')[0]}">
CKEDITOR.on("instanceReady", function(event)
{
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getDocument.json",
		data:{"source":"${parameters.get('source')[0]}","id":"${parameters.get('doc')[0]}"},
		success:update_text});
});
</c:if>
</script>
