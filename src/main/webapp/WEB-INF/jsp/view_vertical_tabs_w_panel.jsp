<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--vertical tabbed page view.-->
<style>
.ui-tabs-vertical .ui-tabs-nav { padding: .2em .1em .2em .2em; float: left; }
.ui-tabs-vertical .ui-tabs-nav li { clear: left; width: 400px; border-bottom-width: 1px !important; border-right-width: 0 !important; margin: 0 -1px .2em 0; }
.ui-tabs-vertical .ui-tabs-nav li a { display: block; width: 356px }/*width: 400px;*/
.ui-tabs-vertical .ui-tabs-nav li.ui-tabs-active { padding-bottom: 0; padding-right: .1em; border-right-width: 1px;}
.ui-tabs-vertical .ui-tabs-panel { padding: 1em; margin-left:411px; margin-right:600px}
.side-panel {display: block; width: 600px; float: right;}
/*.ui-tabs-vertical .ui-tabs-panel .ui-widget-content { }*/

</style>

<c:set var="req" value="${pageContext.request}" />

<div class="usdChannel">
	<div id="${channelId}" class="tabbed-channel-content">
		<div class="tabbed-channel-tabs-panel">
			<ul>
				<c:set var="counter" value="${-1}"/>
				<c:forEach var="page" items="${content}">
					<c:if test="${counter ne -1}">
						<li>
							<a href="#${channelId}-${counter}">${page.title}</a>
							<button class="copy-btn" id="copy-btn-${counter}">
							<svg aria-hidden="true" class="octicon octicon-clippy" height="16" version="1.1" viewBox="0 0 14 16" width="14">
								<path fill-rule="evenodd" d="M2 13h4v1H2v-1zm5-6H2v1h5V7zm2 3V8l-3 3 3 3v-2h5v-2H9zM4.5 9H2v1h2.5V9zM2 12h2.5v-1H2v1zm9 1h1v2c-.02.28-.11.52-.3.7-.19.18-.42.28-.7.3H1c-.55 0-1-.45-1-1V4c0-.55.45-1 1-1h3c0-1.11.89-2 2-2 1.11 0 2 .89 2 2h3c.55 0 1 .45 1 1v5h-1V6H1v9h10v-2zM2 5h8c0-.55-.45-1-1-1H8c-.55 0-1-.45-1-1s-.45-1-1-1-1 .45-1 1-.45 1-1 1H3c-.55 0-1 .45-1 1z"></path>
							</svg>
							</button><!-- 🔗 onclick="copy_link(${counter})"-->
						</li>
					</c:if>
					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>
			</ul>
		</div>
		<div class="tabbed-channel-content-pannel">
			<c:set var="counter" value="${-1}"/>
			<c:forEach var="page" items="${content}">
				<c:if test="${counter ne -1}">
					<div id="${channelId}-${counter}">
						${page.content}
						<c:set var="counter" value="${counter + 1}"/>
					</div>
				</c:if>
			</c:forEach>
		</div>
	</div>
</div>




<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"></script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<script src="/CMSContent/js/zeroclipboard/dist/ZeroClipboard.js" type="text/javascript"></script>
<link rel="stylesheet" href="/ResourceServingWebapp/rs/jqueryui/1.10.3/theme/smoothness/jquery-ui-1.10.3-smoothness.min.css">

<script>
$( function() {
	var tabs = $( "#${channelId}" ).tabs({ selected: null }).addClass( "ui-tabs-vertical ui-helper-clearfix" );
	tabs.tabs( "option", "active", ${parameters.get("tab")[0]} );
	$( "#${channelId} li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
} );

//var client = new ZeroClipboard();
//var client = new ZeroClipboard( document.getElementById('d_clip_button') );
<c:set var="counter" value="${-1}"/>
<c:forEach var="page" items="${content}">
	<c:if test="${counter ne -1}">
		document.getElementById('copy-btn-${counter}').setAttribute("data-clipboard-text",
			location.protocol + '//' + location.host + location.pathname + "?tab=${counter}");
		var client = new ZeroClipboard( document.getElementById('copy-btn-${counter}') );
	</c:if>
	<c:set var="counter" value="${counter + 1}"/>
</c:forEach>


function copy_link(val)
{
	alert(location.protocol + '//' + location.host + location.pathname + "?tab=" + val);
	client.setText(location.protocol + '//' + location.host + location.pathname + "?tab=" + val);
}


</script>
