<%-- Author: Toben Archer | Version $Id$ --%>
<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script src="https://vuejs.org/js/vue.js"></script>
<!--<script src="https://vuejs.org/js/vue.min.js"></script>-->
<script src="/CMSContent/components/report-display.js"></script>

	<div id="app">
		<report-display report="${report}" token="${token.hash}">
		</report-display>
	</div>

<script>
var app = new Vue({
	el: '#app',
});
</SCRIPT>


