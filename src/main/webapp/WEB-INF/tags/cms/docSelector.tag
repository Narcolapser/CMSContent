<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag dynamic-attributes="attributes" isELIgnored="false" %>
<%@ attribute name="sources"            required="true" %>
<%@ attribute name="currentSource"      required="false" %>
<%@ attribute name="activeDocuments"    required="true" %>
<%@ attribute name="availableDocuments" required="true" %>
<%@ attribute name="mode"               required="true" %>


<div name="${mode}_doc_pane">
	<div name="${mode}_controls" class="form-inline">
		<div class="form-group">
			<button class="btn btn-default">new</button>
			<button class="btn btn-default">edit</button>
			<button class="btn btn-default">add</button>
			<select id="${mode}_source" class="form-control">
				<option id="${mode}_CS" value="cs">CommonSpot</option>
				<option id="${mode}_int" value="int">Internal</option>
			</select>
		</div>
		<div id="${mode}_doc_selector">
			<select id="${mode}_doc_select">
				<!-- ${availableDocuments}-->
				<c:forEach var="doc" items="${availableDocuments}">
					<!--${doc}-->
					<option value="${doc}">${doc}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div>
		<c:forEach var="doc" items="${activeDocuments}">
			<p>${doc}</p>
		</c:forEach>
	</div>
</div>
