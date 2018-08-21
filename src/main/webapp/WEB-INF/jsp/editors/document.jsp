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

<script src="https://rawgit.com/webcomponents/webcomponents-lite/master/webcomponents-lite.js"></script>
<link rel="import" href="/CMSContent/components/doc-tree.html">

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
		<doc-tree sources="${sources}"></doc-tree>
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
	//ret_button.href=document.referrer;
	
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
