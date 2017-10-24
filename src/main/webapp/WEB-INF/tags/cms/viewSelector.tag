<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ tag dynamic-attributes="attributes" isELIgnored="false" %>
<%@ attribute name="availableViews"   required="true" %>
<%@ attribute name="currentView"      required="true" %>

	<portlet:actionURL name="updateDisplay" var="updateDisplay">
		<portlet:param name="action" value="updateDisplay" />
	</portlet:actionURL>
	
	<h2>Portlet view type:</h2>
	<!--${availableViews}-->
	<!--${currentView}-->
	<form id="disp_type_form" action="${updateDisplay}">
		<select id="disp_type" name="disp_type" class="form-control">
			<c:forEach var="disp" items="${availableViews}">
				<c:choose>
					<c:when test="${disp == currentView}">
						<option value="${disp}" selected="selected">${disp}</option>
					</c:when>
					<c:otherwise>
						<option value="${disp}">${disp}</option>
					</c:otherwise>
				</c:choose>
			</c:forEach>
		</select>
		<input type="submit" name="action" class="btn btn-default" value="updateDisplay"/>
	</form>
