<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--expanding page view.-->
<script src="<c:url value='/webjars/jquery/3.3.1-1/jquery.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-ui/1.12.1/jquery-ui.min.js'/>" type="text/javascript"></script>
<script src="<c:url value='/webjars/jquery-form/4.2.1/jquery.form.min.js'/>" type="text/javascript"></script>
<link rel="stylesheet" href="<c:url value='/webjars/jquery-ui-themes/1.12.1/smoothness/jquery-ui.min.css'/>" />
<style type="text/css">
	#wrapper_${channelId} #head_links{text-align:right; width:100%; border-bottom:solid 1px black;}
	#wrapper_${channelId} .section:not(:first-child){margin-top:1em;}
	#wrapper_${channelId} .section_title p{margin:0px;}
	#wrapper_${channelId} .section_collapsed,.section_expanded{
		background-position: left center;
		background-color: #ececec;
		background-repeat: no-repeat;
		color:#AD0000;
		font-weight:bold;
		padding:.1em;
		padding-left:1.2em;
		cursor:pointer;
	}
	#wrapper_${channelId} .section_collapsed{background-image: url('${portletPath}/arrow_right.png');}
	#wrapper_${channelId} .section_expanded{background-image:url('${portletPath}/arrow_down.png');}
	#wrapper_${channelId} .section_body{
		padding:.4em;
	}
</style>


<script type="text/javascript">
	function expandingCms_showEle(ele){
		up.jQuery(ele).slideDown("fast");
	}
	function expandingCms_hideEle(ele){
		up.jQuery(ele).slideUp("fast");
	}
	function expandingCms_swapClasses(ele,oldClass,newClass){
		up.jQuery(ele).removeClass(oldClass);
		up.jQuery(ele).addClass(newClass);
	}
	function expandingCms_toggleSection(sectId,randId){
		var bodyEle = "#wrapper_" + randId +" #" + sectId + "_body"; 
		var titleEle = "#wrapper_" + randId +" #" + sectId + "_title"; 
		if (up.jQuery(bodyEle).css("display") == 'none'){
			expandingCms_showEle(bodyEle);
			expandingCms_swapClasses(titleEle,"section_collapsed","section_expanded");
		}else{
			expandingCms_hideEle(bodyEle);
			expandingCms_swapClasses(titleEle,"section_expanded","section_collapsed");
		}
	}
	function expandingCms_expandAll(randId){
		expandingCms_showEle("#wrapper_" + randId +" .section_body");
		expandingCms_swapClasses("#wrapper_" + randId +" .section_title","section_collapsed","section_expanded");
	}
	function expandingCms_collapseAll(randId){
		expandingCms_hideEle("#wrapper_" + randId +" .section_body");
		expandingCms_swapClasses("#wrapper_" + randId +" .section_title","section_expanded","section_collapsed");
	}

</script>

<div class="usdChannel">
	<div id="wrapper_${channelId}">
		<div id="head_links">
			<div>
				<a href="javascript: expandingCms_expandAll('${channelId}');">Expand all</a> |
				<a href="javascript: expandingCms_collapseAll('${channelId}');">Collapse all</a>
			</div>
		</div>
		<c:set var="counter" value="${0}"/>
		<c:forEach var="document" items="${content}">
			<div class="section">
				<div class="section_title section_collapsed" id="${channelId}_${counter}_title" 
					onClick="expandingCms_toggleSection('${channelId}_${counter}','${channelId}');">
					${document.title}
				</div>
				<div class="section_body" id="${channelId}_${counter}_body" style="display: none;">
					<div class="usdChannel">${document.render()}</div>
				</div>
			</div>
			<c:set var="counter" value="${counter + 1}"/>
		</c:forEach>
	</div>
</div>
