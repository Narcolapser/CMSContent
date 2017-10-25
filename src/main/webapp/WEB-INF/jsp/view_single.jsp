<%@ include file="/WEB-INF/jsp/include.jsp" %>
<!--Single page view.-->
<c:choose>
	<c:when test="${isForm[content[0].id]}">
		<form id="content[0].id">
			<c:forEach var="control" items="${formContent[content[0].id]}">
				<c:set var="val">${control.getString("type")}</c:set>
				<c:choose>
					<c:when test="${val eq 'text'}">
						<p>${control.getString("label")}</p><input type="text" class="form-control"></input>
					</c:when>
					<c:when test="${val eq 'select'}">
						<p>${control.getString("label")}</p>
						<select class="form-control">
							<c:forEach var="op" items="${control.getString('options').split(',')}">
								<option value="${op}">${op}</option>
							</c:forEach>
						</select>
					</c:when>
					<c:when test="${val eq 'bool'}">
						<label class="checkbox-inline"><input type="checkbox"></input><p>${control.getString("label")}</p></label>
					</c:when>
					<c:when test="${val eq 'checkbox'}">
						<p>${control.getString("label")}</p>
						<c:forEach var="op" items="${control.getString('options').split(',')}">
							<div class="checkbox" style="margin-left: 20px;">
								<input type="checkbox" name="${op.replace(' ','_')}" value="${op}">${op}</input>
							</div>
						</c:forEach>
					</c:when>
					<c:when test="${val eq 'radiobutton'}">
						<p>${control.getString("label")}</p>
						<c:forEach var="op" items="${control.getString('options').split(',')}">
							<div class="radio" style="margin-left: 20px;">
								<input type="radio" name="${op.replace(' ','_')}" value="${op}">${op}</input>
							</div>
						</c:forEach>
					</c:when>
					<c:when test="${val eq 'hr'}">
						<hr/>
					</c:when>
					<c:otherwise>
						<p>${control.getString("label")}</p>
					</c:otherwise>
				</c:choose>
			</c:forEach>
			<a class="btn" onclick="submit('${content[0].id}');false">Submit</a>
	</c:when>
	<c:otherwise>
		<p>not form</p>
		<div class="usdChannel">${content[0].content}</div>
	</c:otherwise>
</c:choose>
<script>
function submit(formId)
{
	var form = document.getElementsByClassName(formId);
	alert(form.children);
}
</script>
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
