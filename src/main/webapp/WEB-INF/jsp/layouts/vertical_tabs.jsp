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
<c:set var="active" value="${0}"/>
<c:if test="${not empty parameters.get('tab')[0]}">
	<c:set var="active" value="${parameters.get('tab')[0]}"/>
</c:if>
<div class="usdChannel">
	<div id="content">
		<div id="left_col" class="left_col" >
			<ul class="nav nav-tabs nav-pills" id="${channelId}_tabs">
				<c:set var="counter" value="${0}"/>
				<c:forEach var="document" items="${content}">
					<c:set var="aclass"></c:set>
					<c:if test="${counter == active}">
						<c:set var="aclass">active</c:set>
					</c:if>
					<li class="btn-group ${aclass}" role="group">
						<a href="#${channelId}-${counter}" data-toggle="tab" class="btn btn-default tab-btn">
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
					<c:choose>
						<c:when test="${counter == active}">
							<div id="${channelId}-${counter}" class="tab-pane active">
								<c:choose>
									<c:when test="${document.docType eq 'form'}">
										<cms:form content="${document}" username="${username}"/>
									</c:when>
									<c:otherwise>
										<div class="usdChannel">${document.render()}</div>
									</c:otherwise>
								</c:choose>
							</div>
						</c:when>
						<c:otherwise>
							<div id="${channelId}-${counter}" class="tab-pane">
								<div class="usdChannel">${document.render()}</div>
							</div>
						</c:otherwise>
					</c:choose>
					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>
			</div>
		</div>
	</div>
</div>
<script src="<c:url value='/webjars/jquery/3.3.1-1/jquery.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-ui/1.12.1/jquery-ui.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/zeroclipboard/2.2.0/ZeroClipboard.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/jquery-ui-themes/1.12.1/smoothness/jquery-ui.min.css'/>" />
<script>
<c:set var="counter" value="${0}"/>
<c:forEach var="document" items="${content}">
	document.getElementById('copy-btn-${counter}').setAttribute("data-clipboard-text",
		location.protocol + '//' + location.host + location.pathname + "?tab=${counter}");
	var client = new ZeroClipboard( document.getElementById('copy-btn-${counter}') );
	<c:set var="counter" value="${counter + 1}"/>
</c:forEach>
</script>
