<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--expanding page view.-->
<style>
.collapsible {
	background-color: #777;
	color: white;
	cursor: pointer;
	padding: 18px;
	width: 100%;
	border: none;
	text-align: left;
	outline: none;
	font-size: 15px;
}

.active, .collapsible:hover {
	background-color: #AD0000;
	color: white;
}

.collapsible:after {
	content: '\002B';
	color: white;
	font-weight: bold;
	float: right;
	margin-left: 5px;
}

.active_content:after {
	content: "\2212";
}

.content {
	padding: 0 18px;
	max-height: 0;
	overflow: hidden;
	transition: max-height 0.2s ease-out;
	background-color: #f1f1f1;
}
</style>

<div class="usdChannel">
	<div id="wrapper_${channelId}">
		<c:forEach var="document" items="${content}">
			<button class="collapsible">${document.title}</button>
			<div class="content">${document.render()}</div>
		</c:forEach>
	</div>
</div>

<script>
var coll = document.getElementsByClassName("collapsible");
var i;

for (i = 0; i < coll.length; i++) {
  coll[i].addEventListener("click", function() {
	this.classList.toggle("active_content");
	var content = this.nextElementSibling;
	if (content.style.maxHeight){
	  content.style.maxHeight = null;
	} else {
	  content.style.maxHeight = content.scrollHeight + "px";
	} 
  });
}
</script>
