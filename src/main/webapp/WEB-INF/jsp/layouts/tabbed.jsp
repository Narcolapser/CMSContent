<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--tabbed page view.-->

<script src="<c:url value='/webjars/jquery/3.3.1-1/jquery.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-ui/1.12.1/jquery-ui.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-form/4.2.1/jquery.form.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/jquery-ui-themes/1.12.1/smoothness/jquery-ui.min.css'/>" />

<script src="<c:url value='/webjars/zeroclipboard/2.2.0/ZeroClipboard.min.js'/>" type="text/javascript"></script>

<c:set var="active" value="${0}"/>
<c:if test="${not empty parameters.get('tab')[0]}">
	<c:set var="active" value="${parameters.get('tab')[0]}"/>
</c:if>
<div class="usdChannel">
	<div id="${channelId}" class="tabbed-channel-content">
		<ul class="nav nav-tabs" id="${channelId}_tabs">
			<c:set var="counter" value="${0}"/>
			<c:forEach var="document" items="${content}">
				<c:choose>
					<c:when test="${counter == active}">
						<li class="active">
							<div class="btn-group" role="group">
								<a href="#${channelId}-${counter}" data-toggle="tab" class="btn btn-default">${document.title}</a>
								<c:if test="${properties.get('Link buttons (True/False)') == 'True'}">
									<button class="copy-btn btn btn-default" id="copy-btn-${counter}">
										<i class="fa fa-link"></i>
									</button>
								</c:if>
							</div>
						</li>
					</c:when>
					<c:otherwise>
						<li>
							<div class="btn-group" role="group">
								<a href="#${channelId}-${counter}" data-toggle="tab" class="btn btn-default">${document.title}</a>
								<c:if test="${properties.get('Link buttons (True/False)') == 'True'}">
									<button class="copy-btn btn btn-default" id="copy-btn-${counter}">
										<i class="fa fa-link"></i>
									</button>
								</c:if>
							</div>
						</li>
					</c:otherwise>
				</c:choose>
				<c:set var="counter" value="${counter + 1}"/>
			</c:forEach>
		</ul>
		<c:set var="counter" value="${0}"/>
		<div class="tab-content">
			<c:forEach var="document" items="${content}">
				<c:choose>
					<c:when test="${counter == active}">
						<div id="${channelId}-${counter}" class="tab-pane active">
							<c:choose>
								<c:when test="${document.docType eq 'form'}">
									<cms:form content="${document}" username="${username}" replyType="coming soon"/>
								</c:when>
								<c:otherwise>
									<div class="usdChannel">${document.render()}</div>
								</c:otherwise>
							</c:choose>
						</div>
					</c:when>
					<c:otherwise>
						<div id="${channelId}-${counter}" class="tab-pane">
							<c:choose>
								<c:when test="${document.docType eq 'form'}">
									<cms:form content="${document}" username="${username}" replyType="coming soon"/>
								</c:when>
								<c:otherwise>
									<div class="usdChannel">${document.render()}</div>
								</c:otherwise>
							</c:choose>
						</div>
					</c:otherwise>
				</c:choose>
				<c:set var="counter" value="${counter + 1}"/>
			</c:forEach>
		</div>
	</div>
</div>
<script>
<c:set var="counter" value="${0}"/>
<c:forEach var="document" items="${content}">
	document.getElementById('copy-btn-${counter}').setAttribute("data-clipboard-text",
		location.protocol + '//' + location.host + location.pathname + "?tab=${counter}");
	var client = new ZeroClipboard( document.getElementById('copy-btn-${counter}') );
	<c:set var="counter" value="${counter + 1}"/>
</c:forEach>
</script>
