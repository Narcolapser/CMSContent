<%@ include file="/WEB-INF/jsp/include.jsp" %>
<script src="<c:url value='/webjars/jquery/3.3.1-1/jquery.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-ui/1.12.1/jquery-ui.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/jquery-ui-themes/1.12.1/smoothness/jquery-ui.min.css'/>" />

<script src="<c:url value='/webjars/chosen/1.8.2/chosen.jquery.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/chosen/1.8.2/chosen.min.css'/>" />

<script src="<c:url value='/webjars/sprintf.js/1.0.0/sprintf.min.js'/>" type="text/javascript"></script>

<script src="https://vuejs.org/js/vue.js"></script>
<!--<script src="https://vuejs.org/js/vue.min.js"></script>-->
<script src="/CMSContent/components/config-bar.js"></script>
<script src="/CMSContent/components/config-layout-selector.js"></script>
<script src="/CMSContent/components/config-document-source.js"></script>
<script src="/CMSContent/components/config-document.js"></script>

<c:set var="n"><portlet:namespace/></c:set>
<portlet:actionURL var="getPages" name="getPages"></portlet:actionURL>
<portlet:renderURL var="config_done" portletMode="VIEW" windowState="NORMAL"/>
<!--${isDev}-->
<div class="usdChannel" id="content-wrapper">
	<div id="app">
		<div id="${n}_config_bar">
			<config-bar return_url="${config_done}"></config-bar>
		</div>
		<div id="${n}_preview">
		
		</div>
	</div>
</div>

<portlet:actionURL var="updateDocument">
	<portlet:param name="action" value="updateDocument"/>
</portlet:actionURL>
<portlet:actionURL var="updateView">
	<portlet:param name="action" value="updateView" />
</portlet:actionURL>
<portlet:actionURL var="updateProperty">
	<portlet:param name="action" value="updateProperty" />
</portlet:actionURL>
<portlet:actionURL var="updateSecurity">
	<portlet:param name="action" value="updateSecurity" />
</portlet:actionURL>
<portlet:actionURL var="reorderDocs">
	<portlet:param name="action" value="reorder" />
</portlet:actionURL>

<SCRIPT LANGUAGE="javascript">


$(".chosen-select").chosen({width: "100%"});
$(".chosen-select-multi").chosen({width: "100%",no_results_text:"Security role not found."});

function sec_change(val,mode,doc)
{
	var e = document.getElementById(val);
	var values = getSelectValues(e);
	$.ajax({dataType:"json",
		url:"${updateSecurity}",
		data:{"document":doc,
			"groups":JSON.stringify(values),
			"mode":mode}});
}

function getSelectValues(select) {
	var result = [];
	var options = select && select.options;
	var opt;

	for (var i=0; i<options.length; i++) {
		opt = options[i];

		if (opt.selected) {
			result.push(opt.value || opt.text);
		}
	}
	return result;
}

function edit_document(val)
{
	var e = document.getElementById(val+"_doc_select");
	var selected = e.options[e.selectedIndex].value;
	var source_selector = document.getElementById(val+"_source");
	var selected_type = e.options[e.selectedIndex].getAttribute("data-type");
	var source = source_selector.options[source_selector.selectedIndex].value;
	console.log("Selected type: " + selected_type);
	if (selected_type.includes("form"))
		window.location.href = "/uPortal/p/CMSForm.ctf2/max/render.uP?doc="+selected;
	else
		window.location.href = "/uPortal/p/cmseditor.ctf4/max/render.uP?doc="+selected+"&source="+source;
}

function quick_edit_document(edit_button)
{
	var source = edit_button.parentNode.parentNode.parentNode.children[3].innerText;
	var doc_id = edit_button.parentNode.parentNode.parentNode.children[2].innerText
	if(source == 'InternalForms')
		window.location.href = "/uPortal/p/CMSForm.ctf2/max/render.uP?doc="+doc_id;
	else
		window.location.href = "/uPortal/p/cmseditor.ctf4/max/render.uP?doc="+doc_id+"&source="+source;
}

function new_document(val)
{
	window.location.href = "/uPortal/p/cmseditor.ctf2/max/render.uP";
}

function new_form(val)
{
	window.location.href = "/uPortal/p/CMSForm.ctf2/max/render.uP";
}

function add_document(val)
{
	var e = document.getElementById(val+"_doc_select");
	var selected = e.options[e.selectedIndex].value;
	var selected_title = e.options[e.selectedIndex].getAttribute("data-title");
	var source_e = document.getElementById(val+"_source");
	var source_selected = source_e.options[source_e.selectedIndex].value;
	$.ajax({dataType:"json",
		url:"${updateDocument}",
		data:{"document":selected,
			"source":source_selected,
			"index":e.length,
			"mode":val}});
	
	var table = document.getElementById(val+"_docs_table");
	table = table.children[1];
	var last_row = table.children[table.children.length - 1];
	var row = new_doc_row(selected_title,selected,val,source_selected);
	if (last_row != null)
		table.insertBefore(row,last_row.nextSibling);
	else
		table.appendChild(row);
	$(".chosen-select-multi").chosen({width: "100%",no_results_text:"Security role not found."});
}
function new_doc_row(title,id,mode,source)
{
	var new_row = document.createElement("TR");
	var content = `${doc_row}`;
	new_row.innerHTML = sprintf(content,mode,id,title,source);
	console.log(sprintf("This is a test. Mode: %1$s Title: %2$s ID: %3$s Mode: %1$s",mode,title,id));
	return new_row;
}

function remove_document(mode,button)
{
	var table = document.getElementById(mode+"_docs_table")
	var val = button.parentNode.parentNode.parentNode.rowIndex;
	$.ajax({dataType:"json",
		url:"${updateDocument}",
		data:{"index":val,
			"mode":mode}});
	table.deleteRow(val);
	
}

function up_document(mode,button)
{
	var row = button.parentNode.parentNode.parentNode;
	if (row.rowIndex == 0)
		return false;
	var index = row.rowIndex;
	var table = row.parentNode;
	var rows = table.getElementsByTagName('tr');
	var prev = rows[row.rowIndex - 2];
	table.insertBefore(row,prev);
	$.ajax({dataType:"json",
		url:"${reorderDocs}",
		data:{"index":index,
			"direction":"up",
			"mode":mode}});
}

function down_document(mode,button)
{
	var row = button.parentNode.parentNode.parentNode;
	var index = row.rowIndex;
	var table = row.parentNode;
	var rows = table.getElementsByTagName('tr');
	var next = rows[row.rowIndex + 1];

	table.removeChild(row);
	if (next != null)
		table.insertBefore(row,next);
	else
		table.appendChild(row);

	$.ajax({dataType:"json",
		url:"${reorderDocs}",
		data:{"index":index,
			"direction":"down",
			"mode":mode},});
}

function update_view(mode)
{
	var e = document.getElementById(mode+"_view_type");
	var selected = e.options[e.selectedIndex].value;
	$.ajax({dataType:"json",
		url:"${updateView}",
		data:{"view_type":selected,
			"mode":mode},
		success:new_properties});
}

function new_properties(data, textStatus, jqXHR)
{
	//Todo: Make it so that when you update a layout, it fetches the new properties.
}

function update_property(prop)
{
	var e = document.getElementById(prop);
	var value = e.value;
	$.ajax({dataType:"json",
		url:"${updateProperty}",
		data:{"property":prop,
			"value":value}});
}

function viewChange(mode)
{
	var all = document.getElementsByClassName(mode + "_desc");
	var e = document.getElementById(mode+"_view_type");
	var selected = e.options[e.selectedIndex].value;
	for(i = 0; i < all.length; i ++)
	{
		var obj = all[i];
		var classes = obj.className.split(" ");
		comp = mode + "_" + selected;
		if(obj.id == comp)
		{
			obj.className = mode + "_desc show";
		}
		else
		{
			obj.className = mode + "_desc hide";
		}
	}
	
}

function OnChange(sources_id,doc_selector_id)
{
	var sources = document.getElementById(sources_id);
	var pages = document.getElementById(doc_selector_id);
	var myindex = sources.selectedIndex;
	var SelValue = sources.options[myindex].value;
	pages.options.length=0;
	pages.options[0] = new Option("Loading...","");
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/getPagesWithIndex.json",
		data:{"source":SelValue,"index":doc_selector_id},
		success:populate_pages});
}

function populate_pages(data, textStatus, jqXHR)
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
		pages.options[i].setAttribute("data-type",data["pages"][i]["docType"]);
		pages.options[i].setAttribute("data-title",data["pages"][i]["title"]);
	}
	$("#"+CID).trigger("chosen:updated");
}
//$(document).ready(function(){
//	OnChange("normal_source","normal_doc_select");
//	OnChange("maximized_source","maximized_doc_select");
//});
var app = new Vue({
  el: '#app',
  data: {
    message: 'Hello Vue!'
  }
});
</SCRIPT>
