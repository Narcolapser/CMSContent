<%@ include file="/WEB-INF/jsp/include.jsp" %>
<style type="text/css">.btn-lg, .btn-group-lg>.btn {
		font-weight: bold;
		margin: 0;
		padding: 0;
		font-size: 24px;
		line-height: 3em;
		border-radius: 6px;
		width: 4em;
		text-align: center;
		vertical-align: middle;
	}
	.buttonContainer{
		float:left;
		padding:1em 0;
		width:33.3%;
	}
	.buttonsGroupContainer{
		text-align:center;
		width:100%;
	}
	.clearer{
		clear:both;
	}
	.btn-danger {
	color: #fff;
	background-color: #AD0000;
	border-color: #f54843;
	}
</style>
<c:set var="col" value="0"/>
<c:forEach var="document" items="${content}">
	<c:set var="col" value="${col + 1}"/>
	<c:if test="${col eq 1}">
		<div class="buttonsGroupContainer">
	</c:if>
		<div class="buttonContainer">${document.render()}</div>
	<c:if test="${col eq 3}">
		<div class="clearer">&nbsp;</div>
		</div>
		<c:set var="col" value="0"/>
	</c:if>
</c:forEach>
