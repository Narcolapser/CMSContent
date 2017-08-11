<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--tabbed page view.-->
<c:set var="active" value="${0}"/>
<c:if test="${not empty parameters.get('tab')[0]}">
	<c:set var="active" value="${parameters.get('tab')[0]}"/>
</c:if>
<div class="usdChannel">
	<div id="${channelId}" class="tabbed-channel-content">
		<ul class="nav nav-tabs" id="${channelId}_tabs">
			<c:set var="counter" value="${0}"/>
			<c:forEach var="page" items="${content}">
				<c:choose>
					<c:when test="${counter == active}">
						<li class="active">
							<div class="btn-group" role="group">
								<a href="#${channelId}-${counter}" data-toggle="tab" class="btn btn-default">${page.title}</a>
								<button class="copy-btn btn btn-default" id="copy-btn-${counter}">
									<i class="fa fa-link"></i>
								</button>
							</div>
						</li>
					</c:when>
					<c:otherwise>
						<li>
							<div class="btn-group" role="group">
								<a href="#${channelId}-${counter}" data-toggle="tab" class="btn btn-default">${page.title}</a>
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
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="/ResourceServingWebapp/rs/jqueryui/1.10.3/theme/smoothness/jquery-ui-1.10.3-smoothness.min.css">
<script>
</script>
