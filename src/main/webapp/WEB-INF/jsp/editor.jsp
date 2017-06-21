<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"> </script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/chosen.jquery.js" type="text/javascript"></script>
<script src="/CMSContent/js/ckeditor/ckeditor.js" type="text/javascript"></script>
<link rel="stylesheet" href="/CMSContent/css/chosen.css">

<c:set var="n"><portlet:namespace/></c:set>

<style type="text/css">
    #${n}contentForm { min-height: 100px; padding: 10px; margin: 10px; }
    .cke_source { color: #000000; }
</style>

<p>This is an editor</p>

<select id="docselector" class="chosen-select" data-placeholder="Select document..." OnChange='OnChange(this.form.docselector);'>
	<c:forEach var="doc" items="${CommonSpot}">
		<option value="${doc}">${doc}</option>
	</c:forEach>
</select>

<h2>Hopefully there is an editor below this line.</h2>

<form id="${n}contentForm" commandName="form" action="" method="post"
    <textarea id="${n}content" name="content">sample text</textarea>

    <p>
        <a  onclick="addtext()">da link</a>
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
function onChange()
{
	
}
</script>


<!--${CommonSpot}-->
<!--${availablePages}-->
<!--${None}-->
<!--${pageUris}-->
<!--${sources}-->
<!--${displayType}-->
<!--${displayTypes}-->
