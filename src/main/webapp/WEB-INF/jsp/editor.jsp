<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"> </script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/chosen.jquery.js" type="text/javascript"></script>
<script src="/CMSContent/js/ckeditor/ckeditor.js" type="text/javascript"></script>
<script src="/CMSContent/js/jquery.form.min.js" type="text/javascript"></script>
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

<portlet:actionURL name="updateDocument" var="updateDocument"/>
<form id="${n}contentForm" commandName="form" action="${updateDocument}" method="post">
	<div class="form-group">
		<label for="doc_title">Title:</label>
		<input type="text" class="form-control" id="doc_title" name="doc_title">
	</div>
	<div class="form-group">
		<label for="doc_title">ID:</label>
		<input type="text" class="form-control" id="doc_id" name="doc_id">
	</div>
	<div class="form-group">
		<label for="doc_source">Source:</label>
		<input type="text" class="form-control" id="doc_source" value="Internal" disabled="disabled">
	</div>
	<input type="hidden" id="doc_source_hidden" name="doc_source" value="Internal">
	<textarea id="${n}content" name="content">put content here.</textarea>
	<p>
		<input type="submit" name="action" value="Update" class="btn btn-primary"/>
		<a id="return_btn" href="https://dev-uportal.usd.edu/uPortal/p/cmseditor" class="btn btn-danger">Return</a>
	</p>
	
</form>


<SCRIPT LANGUAGE="javascript">
$(".chosen-select").chosen();

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
		alert("Page updated.");
	});
	var ret_button = document.getElementById("return_btn");
	ret_button.href=document.referrer;
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
	
	${n}.jQuery.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getDocument.json",
		data:{"source":"Internal","id":doc_id},
		success:update_text});
}
function update_text(data, textStatus, jqXHR)
{
	alert("update_text");
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
