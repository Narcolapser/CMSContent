<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--vertical tabbed page view.-->

<c:set var="tab_width" value="${properties.get('Tab column width (in pixels)') - 124}"/>
<c:if test="${properties.get('Link buttons (True/False)') == 'True'}">
	<c:set var="tab_width" value="${238}"/>
</c:if><!--Tab width: ${tab_width}-->
<style>
#content-wrapper{
	display:table;
	width: 100%;
}
#content{
	display:table-row;
}
#content>div{
	display:table-cell;
	padding: 10px;
}
.left_col{
	width: ${tab_width+15}px;
	float: left;
}
.right_col{
	width: auto;
	float: left;
}
div.col_content{
	width: 100%;
}
a.tab-btn{
	width:${tab_width}px;
	padding: 10px 0px;
	text-align: left
}
button.copy-btn{
	padding: 10px 12px;
	border: 0px;
}
.nav-pills > li.active > a, .nav-pills > li.active > a:hover, .nav-pills > li.active > a:focus{
	background-color: #AD0000;
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
		<div id="left_col" class="left_col" >
			<ul class="nav nav-tabs nav-pills" id="${channelId}_tabs">
				<c:set var="counter" value="${0}"/>
				<c:forEach var="document" items="${content}">
					<c:set var="aclass"></c:set>
					<!-- active vs current: ${document.id } ${active}-->
					<c:if test="${document.id == active}">
						<c:set var="aclass">active</c:set>
					</c:if>
					<li class="btn-group ${aclass}" role="group">
						<a href="#${channelId}-${counter}" data-toggle="tab" class="btn btn-default tab-btn"
							data-docid="${document.id}" onclick="setUrl(this)" data-doctitle="${document.title}">
							${document.title}
						</a>
						<c:if test="${properties.get('Link buttons (True/False)') == 'True'}">
							<button class="copy-btn btn btn-default" id="copy-btn-${counter}">
								<i class="fa fa-link"></i>
							</button>
						</c:if>
					</li>
					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>
			</ul>
		</div>
		<div id="right_col" class="right_col">
			<c:set var="counter" value="${0}"/>
			<div class="tab-content">
				<c:forEach var="document" items="${content}">
					<c:set var="aclass"></c:set>
					<c:if test="${document.id == active}">
						<c:set var="aclass">active</c:set>
					</c:if>
					<div id="${channelId}-${counter}" class="tab-pane ${aclass}">
						<div class="usdChannel">${document.render()}</div>
					</div>
					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>
			</div>
		</div>
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
</script>
