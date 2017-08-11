<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--vertical tabbed page view.-->
<style>
.ui-tabs-vertical .ui-tabs-nav { padding: .2em .1em .2em .2em; float: left; }
.ui-tabs-vertical .ui-tabs-nav li { clear: left; width: 400px; border-bottom-width: 1px !important; border-right-width: 0 !important; margin: 0 -1px .2em 0; }
.ui-tabs-vertical .ui-tabs-nav li a { display:block; width: 356px }/*width: 400px;*/
.ui-tabs-vertical .ui-tabs-nav li.ui-tabs-active { padding-bottom: 0; padding-right: .1em; border-right-width: 1px;}
.ui-tabs-vertical .ui-tabs-panel { padding: 1em; margin-left:411px;}
/*.ui-tabs-vertical .ui-tabs-panel .ui-widget-content { }*/

@font-face {
    font-family: "symbola";
    src: url(/CMSContent/fonts/Symbola_hint.ttf) format("truetype");
}
button.copy-btn { 
    font-family: "symbola";
}


</style>

<c:set var="req" value="${pageContext.request}" />

<div class="usdChannel">
	<div id="${channelId}" class="tabbed-channel-content">
		<div class="tabbed-channel-tabs-panel">
			<ul>
				<c:set var="counter" value="${0}"/>
				<c:forEach var="page" items="${content}">
					<li>
						<div class="btn-group" role="group">
							<a class="btn btn-default" href="#${channelId}-${counter}">${page.title}</a>
							<button class="copy-btn btn btn-default" id="copy-btn-${counter}">
								<i class="fa fa-link"></i>
							</button>
						</div>
					</li>
					<c:set var="counter" value="${counter + 1}"/>
				</c:forEach>
			</ul>
		</div>
		<div class="tabbed-channel-content-pannel">
			<c:set var="counter" value="${0}"/>
			<c:forEach var="page" items="${content}">
				<div id="${channelId}-${counter}">
					${page.content}
					<c:set var="counter" value="${counter + 1}"/>
				</div>
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
<c:set var="counter" value="${0}"/>
<c:forEach var="page" items="${content}">
	document.getElementById('copy-btn-${counter}').setAttribute("data-clipboard-text",
		location.protocol + '//' + location.host + location.pathname + "?tab=${counter}");
	var client = new ZeroClipboard( document.getElementById('copy-btn-${counter}') );
	<c:set var="counter" value="${counter + 1}"/>
</c:forEach>


function copy_link(val)
{
	alert(location.protocol + '//' + location.host + location.pathname + "?tab=" + val);
	client.setText(location.protocol + '//' + location.host + location.pathname + "?tab=" + val);
}


</script>
