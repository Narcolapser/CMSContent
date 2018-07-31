<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--vertical tabbed page view.-->

<c:set var="tab_width" value="${properties.get('Tab column width (in pixels)') - 124}"/>
<c:if test="${properties.get('Link buttons (True/False)') == 'True'}">
	<c:set var="tab_width" value="${238}"/>
</c:if><!--Tab width: ${tab_width}-->
<style>
* {box-sizing: border-box}
body {font-family: "Lato", sans-serif;}

/* Style the tab */
.tab {
	float: left;
	border: 1px solid #ccc;
	background-color: #f1f1f1;
	width: 30%;
}

/* Style the buttons inside the tab */
.tab button {
	display: block;
	background-color: inherit;
	color: black;
	padding: 22px 16px;
	width: 100%;
	border: none;
	outline: none;
	text-align: left;
	cursor: pointer;
	transition: 0.3s;
	font-size: 17px;
}

/* Change background color of buttons on hover */
.tab button:hover {
	background-color: #ddd;
}

/* Create an active/current "tab button" class */
.tab button.active {
	background-color: #AD0000;
	color: white;
}

/* Style the tab content */
.tabcontent {
	float: left;
	padding: 0px 12px;
	border: 1px solid #ccc;
	width: 70%;
	border-left: none;
}
</style>
<c:set var="req" value="${pageContext.request}" />
<c:set var="active" value="0"/>
<c:if test="${not empty parameters.get('tab')[0]}">
	<c:set var="active" value="${parameters.get('tab')[0]}"/>
</c:if>
<c:if test="${active.matches('[0-9]+')}">
	<c:set var="active" value="${content[active].id}"/>
</c:if>
<!-- active document ${active}-->
<div class="usdChannel">
	<div id="content">
		<div class="tab">
			<c:forEach var="document" items="${content}">
				<c:set var="aclass"></c:set>
				<c:if test="${document.id == active}">
					<c:set var="aclass">id="defaultOpen"</c:set>
				</c:if>
				<button class="tablinks" onclick="openTab(event,'${channelId}-${document.id}',this)" ${aclass}
					data-docid="${document.id}" data-doctitle="${document.title}">${document.title}</button>
			</c:forEach>
		</div>
		<c:forEach var="document" items="${content}">
			<div id="${channelId}-${document.id}" style="display:none;" class="tabcontent">
				${document.render()}
			</div>
		</c:forEach>
	</div>
</div>
<script src="<c:url value='/webjars/jquery/3.3.1-1/jquery.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-ui/1.12.1/jquery-ui.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/jquery-ui-themes/1.12.1/smoothness/jquery-ui.min.css'/>" />
<script>
function setUrl(anchor)
{
	console.log(anchor);
	var docid = anchor.getAttribute("data-docid");
	console.log(docid);
	var title = anchor.getAttribute("data-doctitle");
	window.history.pushState("",title,location.protocol + '//' + location.host + location.pathname + "?tab=" + docid);
}
function openTab(evt, tabid,button)
{
	var i, tabcontent, tablinks;
	tabcontent = document.getElementsByClassName("tabcontent");
	for (i = 0; i < tabcontent.length; i++)
		tabcontent[i].style.display = "none";
	
	tablinks = document.getElementsByClassName("tablinks");
	for (i = 0; i < tablinks.length; i++)
		tablinks[i].className = tablinks[i].className.replace(" active","");
	
	document.getElementById(tabid).style.display = "block";
	evt.currentTarget.className += " active";
	setUrl(button);
}
document.getElementById("defaultOpen").click();
</script>
