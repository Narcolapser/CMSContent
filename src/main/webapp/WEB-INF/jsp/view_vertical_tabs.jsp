<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--vertical tabbed page view.-->
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
	width:400px;
}
.right_col{
	width: auto;
}
div.col_content{
	width: 100%;
}
a.tab-btn{
	width:342px;
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
			<ul class="nav nav-tabs nav-stacked" id="${channelId}_tabs">
				<c:set var="counter" value="${0}"/>
				<c:forEach var="page" items="${content}">
					<c:choose>
						<c:when test="${counter == active}">
							<li class="active">
								<div class="btn-group" role="group">
									<a href="#${channelId}-${counter}" data-toggle="tab" class="btn btn-default tab-btn">${page.title}</a>
									<button class="copy-btn btn btn-default" id="copy-btn-${counter}">
										<i class="fa fa-link"></i>
									</button>
								</div>
							</li>
						</c:when>
						<c:otherwise>
							<li>
								<div class="btn-group" role="group">
									<a href="#${channelId}-${counter}" data-toggle="tab" class="btn btn-default tab-btn">${page.title}</a>
									<button class="copy-btn btn btn-default" id="copy-btn-${counter}">
										<i class="fa fa-link"></i>
									</button>
								</div>
							</li>
						</c:otherwise>
					</c:choose>
					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>
			</ul>
		</div>
		<div id="right_col" class="right_col">
			<c:set var="counter" value="${0}"/>
			<div class="tab-content">
				<c:forEach var="page" items="${content}">
					<c:choose>
						<c:when test="${counter == active}">
							<div id="${channelId}-${counter}" class="tab-pane active">
								${page.content}
							</div>
						</c:when>
						<c:otherwise>
							<div id="${channelId}-${counter}" class="tab-pane">
								${page.content}
							</div>
						</c:otherwise>
					</c:choose>
					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>
			</div>
		</div>
	</div>
</div>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/zeroclipboard/dist/ZeroClipboard.js" type="text/javascript"></script>
<link rel="stylesheet" href="/ResourceServingWebapp/rs/jqueryui/1.10.3/theme/smoothness/jquery-ui-1.10.3-smoothness.min.css">
<script>
<c:set var="counter" value="${0}"/>
<c:forEach var="page" items="${content}">
	document.getElementById('copy-btn-${counter}').setAttribute("data-clipboard-text",
		location.protocol + '//' + location.host + location.pathname + "?tab=${counter}");
	var client = new ZeroClipboard( document.getElementById('copy-btn-${counter}') );
	<c:set var="counter" value="${counter + 1}"/>
</c:forEach>
</script>
