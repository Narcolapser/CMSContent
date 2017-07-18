<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--vertical tabbed page view.-->

<style>
.ui-tabs-vertical .ui-tabs-nav { padding: .2em .1em .2em .2em; float: left; }
.ui-tabs-vertical .ui-tabs-nav li { clear: left; width: 400px; border-bottom-width: 1px !important; border-right-width: 0 !important; margin: 0 -1px .2em 0; }
.ui-tabs-vertical .ui-tabs-nav li a { display:block; width: 362px }/*width: 400px;*/
.ui-tabs-vertical .ui-tabs-nav li.ui-tabs-active { padding-bottom: 0; padding-right: .1em; border-right-width: 1px;}
.ui-tabs-vertical .ui-tabs-panel { padding: 1em; margin-left:411px;}
/*.ui-tabs-vertical .ui-tabs-panel .ui-widget-content { }*/
</style>

<div class="usdChannel">
	<div id="${channelId}" class="tabbed-channel-content">
		<div class="tabbed-channel-tabs-panel">
			<ul>
				<c:set var="counter" value="${0}"/>
				<c:forEach var="page" items="${content}">
					<li>
						<a href="#${channelId}-${counter}">${page.title}</a>
						<button id="copy" >🔗</button><!--onclick="copy_link(${counter})"-->
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
<link rel="stylesheet" href="/ResourceServingWebapp/rs/jqueryui/1.10.3/theme/smoothness/jquery-ui-1.10.3-smoothness.min.css">

<script>
$( function() {
	var tabs = $( "#${channelId}" ).tabs({ selected: null }).addClass( "ui-tabs-vertical ui-helper-clearfix" );
	tabs.tabs( "option", "active", ${parameters.get("tab")[0]} );
	$( "#${channelId} li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
} );
function copy_link(val)
{
	alert(location.protocol + '//' + location.host + location.pathname + "?tab=" + val);
	copyTextToClipboard(location.protocol + '//' + location.host + location.pathname + "?tab=" + val);
}

function copy() {
	alert("Yo");
	var copyText = document.querySelector("#input");
	copyText.select();
	document.execCommand("Copy");
}

document.querySelector("#copy").addEventListener("click", copy);

</script>


<!--<c:set var="counter" value="${0}"/>-->
<!--<c:forEach var="page" items="${content}">-->
<!--	var copy${counter}Btn = document.querySelector('.js-copy-${counter}-btn')-->
<!--	copy${counter}Btn.addEventListener('click', function(event) {-->
<!--		copyTextToClipboard(location.protocol + '//' + location.host + location.pathname + "?tab=${counter}");-->
<!--	});-->
<!--	<c:set var="counter" value="${counter + 1}"/>-->
<!--</c:forEach>-->


<!--function copyTextToClipboard(text) {-->
<!--	alert(text);-->
<!--	var textArea = document.createElement("textarea");-->

<!--	//-->
<!--	// *** This styling is an extra step which is likely not required. ***-->
<!--	//-->
<!--	// Why is it here? To ensure:-->
<!--	// 1. the element is able to have focus and selection.-->
<!--	// 2. if element was to flash render it has minimal visual impact.-->
<!--	// 3. less flakyness with selection and copying which **might** occur if-->
<!--	//		the textarea element is not visible.-->
<!--	//-->
<!--	// The likelihood is the element won't even render, not even a flash,-->
<!--	// so some of these are just precautions. However in IE the element-->
<!--	// is visible whilst the popup box asking the user for permission for-->
<!--	// the web page to copy to the clipboard.-->
<!--	//-->

<!--	// Place in top-left corner of screen regardless of scroll position.-->
<!--	textArea.style.position = 'fixed';-->
<!--	textArea.style.top = 0;-->
<!--	textArea.style.left = 0;-->

<!--	// Ensure it has a small width and height. Setting to 1px / 1em-->
<!--	// doesn't work as this gives a negative w/h on some browsers.-->
<!--	textArea.style.width = '2em';-->
<!--	textArea.style.height = '2em';-->

<!--	// We don't need padding, reducing the size if it does flash render.-->
<!--	textArea.style.padding = 0;-->

<!--	// Clean up any borders.-->
<!--	textArea.style.border = 'none';-->
<!--	textArea.style.outline = 'none';-->
<!--	textArea.style.boxShadow = 'none';-->

<!--	// Avoid flash of white box if rendered for any reason.-->
<!--	textArea.style.background = 'transparent';-->


<!--	textArea.value = text;-->

<!--	document.body.appendChild(textArea);-->

<!--	textArea.select();-->

<!--	try {-->
<!--		var successful = document.execCommand('copy');-->
<!--		var msg = successful ? 'successful' : 'unsuccessful';-->
<!--		console.log('Copying text command was ' + msg);-->
<!--	} catch (err) {-->
<!--		console.log('Oops, unable to copy');-->
<!--	}-->

<!--	document.body.removeChild(textArea);-->
<!--}-->
