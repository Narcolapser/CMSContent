<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/chosen.jquery.js" type="text/javascript"></script>
<script src="/CMSContent/js/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="/CMSContent/js/jquery.form.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="/ResourceServingWebapp/rs/jqueryui/1.10.3/theme/smoothness/jquery-ui-1.10.3-smoothness.min.css">
<link rel="stylesheet" href="/CMSContent/css/chosen.css">

<c:set var="n"><portlet:namespace/></c:set>
<c:set var="selected" value=""/>
<c:if test="${not empty parameters.get('doc')[0]}">
	<c:set var="selected" value="${parameters.get('doc')[0]}"/>
</c:if>

<style type="text/css">
	#${n}contentForm { min-height: 100px; padding: 10px; margin: 10px; }
	.cke_source { color: #000000; }
</style>

<div>
	<table style="width:100%;">
		<tr>
			<td style="width:50%;">
				<div class="form-group">
					<label for="doc_source">Source:</label>
					<select id="doc_source" class="form-control" OnChange='onSourceChange();'>
						<c:forEach var="source" items="${sources}">
							<option class="form-control" id="doc_source_${source}" value="${source}" data-saveEnabled="${saveEnabled[source]}">${source}</option>
						</c:forEach>
					</select>
				</div>
			</td>
			<td style="width:50%;">
				<label for="docselector">Document:</label>
				<select id="docselector" class="chosen-select" data-placeholder="Select document..." OnChange='OnChange();'>
					<option value="new" selected>New page</option>
					<c:forEach var="doc" items="${Internal}">
						<c:choose>
							<c:when test="${doc.id == selected}">
								<option selected="selected" value="${doc.id}">${doc.title}</option>
							</c:when>
							<c:otherwise>
								<option value="${doc.id}">${doc.title}</option>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</td>
		</tr>
	</table>
	<div class="form-group">
		<label for="doc_title">Title:</label>
		<input type="text" class="form-control" id="doc_title" name="doc_title">
	</div>
	<div class="form-group">
		<label for="doc_title">ID:</label>
		<input type="text" class="form-control" id="doc_id" name="doc_id">
	</div>
	<input type="hidden" id="doc_source_hidden" name="doc_source" value="Internal">
	<textarea id="${n}content" name="content">put content here.</textarea>
	<p>
		<button id="save_btn" onclick="update();return false" class="btn btn-success" title="save document" disabled="disabled">Save</button>
		<button id="delete_btn" onclick="delete_doc();return false" class="btn btn-danger" title="delete document">Delete</button>
		<a id="return_btn" href="https://dev-uportal.usd.edu/uPortal/p/cmseditor" class="btn btn-primary">Return</a>
	</p>
</div>


<SCRIPT LANGUAGE="javascript">
$(".chosen-select").chosen();
$(".chosen-select").chosen({width: "100%"});

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
	
	var doc_selector = document.getElementById("docselector_chosen");
	doc_selector.style.width = "100%";
	
	//make sure everything lines up.
	onSourceChange()
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
	var selector = document.getElementById("docselector");
	var myindex = selector.selectedIndex;
	var doc_id = selector.options[myindex].value;

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
	var doc_source_hidden = document.getElementById("doc_source_hidden");
	doc_title.value=data.doc.title;
	doc_id.value=data.doc.id;
	doc_source.value=data.doc.source;
	doc_source_hidden.value=data.doc.source;
}
function update()
{
	var doc_title = document.getElementById("doc_title");
	var doc_id = document.getElementById("doc_id");
	var doc_source = document.getElementById("doc_source");
	var doc_source_hidden = document.getElementById("doc_source_hidden");
	
	${n}.jQuery.ajax({dataType:"json",
		type: "POST",
		url:"/CMSContent/v1/api/saveDoc.json",
		data:{"content":CKEDITOR.instances["${n}content"].getData(),
			"doc_id":doc_id.value,
			"doc_title":doc_title.value,
			"doc_source":doc_source.value},
		success:doc_saved});
}
function doc_saved(data, textStatus, jqXHR)
{
	alert("document saved");
}

function delete_doc()
{
	alert("Coming soon...");
//	var doc_table = document.getElementById("doc_table");
//	${n}.jQuery.ajax({dataType:"json",
//		url:"/CMSContent/v1/api/delete.json",
//		data:{"sanitybit":31415,"id":doc_table.rows[0].cells[5].children[0].value},
//		success:doc_deleted});
}
function doc_deleted(data,textStatus, jqXHR)
{
	alert("form Deleted");
}
function onSourceChange()
{
	var source_selector = document.getElementById("doc_source");
	var myindex = source_selector.selectedIndex;
	var source_id = source_selector.options[myindex].value;
	console.log("Changing source to: " + source_id + " can save: " + source_selector.options[myindex].getAttribute("data-saveEnabled"));

	var documents = document.getElementById("docselector")
	documents.options.length=0;
	documents.options[0] = new Option("Loading...","");
	${n}.jQuery("#doc_source").trigger("chosen:updated");
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getPagesWithIndex.json",
		data:{"source":source_id,"index":"docselector"},
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
}

function populate_documents(data, textStatus, jqXHR)
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
	${n}.jQuery("#"+CID).trigger("chosen:updated");
}
<c:if test="${not empty parameters.get('doc')[0]}">
CKEDITOR.on("instanceReady", function(event)
{
	OnChange();
});
</c:if>
</script>

<!--${CommonSpot}-->
<!--${availablePages}-->
<!--${None}-->
<!--${pageUris}-->
<!--${sources}-->
<!--${displayType}-->
<!--${displayTypes}-->
