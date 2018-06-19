<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--tabbed page view.-->
<div class="usdChannel">
	<div id="${channelId}" class="tabbed-channel-content">
		<ul>
			<c:set var="counter" value="${0}"/>
			<c:forEach var="document" items="${content}">
				<li><a href="#${channelId}-${counter}">${document.title}</a></li>
				<c:set var="counter" value="${counter + 1}"/>
			</c:forEach>
		</ul>
		<c:set var="counter" value="${0}"/>
		<c:forEach var="document" items="${content}">
			<div id="${channelId}-${counter}">
				${document.render()}
				<c:set var="counter" value="${counter + 1}"/>
			</div>
		</c:forEach>
	</div>
</div>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="/ResourceServingWebapp/rs/jqueryui/1.10.3/theme/smoothness/jquery-ui-1.10.3-smoothness.min.css">
<script>
	var ${channelId} = ${channelId} || {}; 
	${channelId}.jQuery = jQuery.noConflict(true); 
	(function($) {
		$( "#${channelId}" ).tabs();
	})(${channelId}.jQuery);
</script>
