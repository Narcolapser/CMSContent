<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--Single page view.-->
<c:choose>
	<c:when test="${isForm[content[0].id]}">
		<cms:form content="${content}" formContent="${formContent}"/>
	</c:when>
	<c:otherwise>
		<div class="usdChannel">${content[0].content}</div>
	</c:otherwise>
</c:choose>
<!--
${windowID}
${content[0].id}
${isForm}

				<option value="text">Text</option>
				<option value="select">Drop Down</option>
				<option value="bool">True/False</option>
				<option value="checkbox">Checkbox</option>
				<option value="radiobutton">Radio Button</option>
				<option value="hr">Horrizontal Line</option>
				<option value="label">Label</option>
-->
