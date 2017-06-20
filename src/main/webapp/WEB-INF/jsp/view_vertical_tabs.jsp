<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--vertical tabbed page view.-->

<style>
.ui-tabs-vertical { width: 55em; }
.ui-tabs-vertical .ui-tabs-nav { padding: .2em .1em .2em .2em; float: left; width: 12em; }
.ui-tabs-vertical .ui-tabs-nav li { clear: left; width: 100%; border-bottom-width: 1px !important; border-right-width: 0 !important; margin: 0 -1px .2em 0; }
.ui-tabs-vertical .ui-tabs-nav li a { display:block; }
.ui-tabs-vertical .ui-tabs-nav li.ui-tabs-active { padding-bottom: 0; padding-right: .1em; border-right-width: 1px; }
.ui-tabs-vertical .ui-tabs-panel { padding: 1em; float: right; width: 40em;}
</style>

<div class="usdChannel">
	<div id="${channelId}" class="tabbed-channel-content">
		<ul>
			<c:set var="counter" value="${0}"/>
			<c:forEach var="page" items="${content}">
				<li><a href="#${channelId}-${counter}">${page.title}</a></li>
				<c:set var="counter" value="${counter + 1}"/>
			</c:forEach>
		</ul>
		<c:set var="counter" value="${0}"/>
		<c:forEach var="page" items="${content}">
			<div id="${channelId}-${counter}">
				${page.content}
				<c:set var="counter" value="${counter + 1}"/>
			</div>
		</c:forEach>
	</div>
</div>




<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="/ResourceServingWebapp/rs/jqueryui/1.10.3/theme/smoothness/jquery-ui-1.10.3-smoothness.min.css">--

<script>
$( function() {
	$( "#${channelId}" ).tabs().addClass( "ui-tabs-vertical ui-helper-clearfix" );
	$( "#${channelId} li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
} );
</script>
