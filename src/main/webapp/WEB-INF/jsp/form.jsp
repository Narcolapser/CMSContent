<%-- Author: Toben Archer | Version $Id$ --%>
<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<form id="${id}">
	<div data-control="formInfo"><input data-control="formId" type="hidden" class="form-control" value="${id}"/></div>
	<div data-control="formInfo"><input data-control="username" type="hidden" class="form-control" value="${username}"/></div>
	<c:forEach var="control" items="${json}">
		<c:set var="val">${control.getString("type")}</c:set>
		<c:choose>
			<c:when test="${val eq 'text'}">
				<div data-control="text"><p>${control.getString("label")}</p><input type="text" class="form-control"></input></div>
			</c:when>
			<c:when test="${val eq 'select'}">
				<div data-control="select">
					<p>${control.getString("label")}</p>
					<select class="form-control">
						<option value="unselected" selected disabled hidden>Select One</option>
						<c:forEach var="op" items="${control.getString('options').split(',')}">
							<option value="${op}">${op}</option>
						</c:forEach>
					</select>
				</div>
			</c:when>
			<c:when test="${val eq 'bool'}">
				<div data-control="bool"><label class="checkbox-inline"><input type="checkbox"></input><p>${control.getString("label")}</p></label></div>
			</c:when>
			<c:when test="${val eq 'checkbox'}">
				<div data-control="checkbox">
					<p>${control.getString("label")}</p>
					<c:forEach var="op" items="${control.getString('options').split(',')}">
						<div class="checkbox" style="margin-left: 20px;">
							<input type="checkbox" name="${op.replace(' ','_')}" value="${op}">${op}</input>
						</div>
					</c:forEach>
				</div>
			</c:when>
			<c:when test="${val eq 'radiobutton'}">
				<div data-control="radiobutton">
					<p>${control.getString("label")}</p>
					<c:forEach var="op" items="${control.getString('options').split(',')}">
						<div class="radio" style="margin-left: 20px;">
							<input type="radio" name="${control.getString('label').replace(' ','_')}" value="${op}">${op}</input>
						</div>
					</c:forEach>
				</div>
			</c:when>
			<c:when test="${val eq 'hr'}">
				<div data-control="hr"><hr/></div>
			</c:when>
			<c:when test="${val eq 'respType'}">
				<div data-control="formInfo"><input data-control="replyType" type="hidden" class="form-control" value="${control.getString('label')}"/></div>
				<c:set var="replyType">${control.getString('label')}</c:set>
			</c:when>
			<c:when test="${val eq 'date'}">
				<div data-control="date"><p>${control.getString("label")}</p><input type="date" class="form-control"></input></div>
			</c:when>
			<c:when test="${val eq 'multi-text'}">
				<div data-control="multi-text"><p>${control.getString("label")}</p><textarea class="form-control"></textarea></div>
			</c:when>
			<c:otherwise>
				<div data-control="label"><h2>${control.getString("label")}</h2></div>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<a data-control="submit" id="submit_btn" class="btn btn-default" onclick="submit('${id}');false">Submit</a>
</form>
<script src="/ResourceServingWebapp/rs/jquery/1.10.2/jquery-1.10.2.min.js" type="text/javascript"> </script>
<script src="/ResourceServingWebapp/rs/jqueryui/1.10.3/jquery-ui-1.10.3.min.js" type="text/javascript"></script>
<c:set var="n">formnamespace</c:set>
<script>

var ${n} = ${n} || {};
<c:choose>
	<c:when test="${!usePortalJsLibs}">
		${n}.jQuery = jQuery.noConflict(true);
	</c:when>
	<c:otherwise>
		${n}.jQuery = up.jQuery;
	</c:otherwise>
</c:choose>
var $ = ${n}.jQuery;

function submit(formId)
{
	//alert(formId);
	var form = document.getElementById(formId);
	var item,value,data={};
	for(var i = 0; i < form.children.length ; i++)
	{
		//alert("Child" + i + ":" + form.children[i].children[0].innerHTML);
		if(form.children[i].innerHTML != "Submit")
			item = form.children[i].children[0].innerHTML;
		else
			item = "Submit";
		if (form.children[i].dataset.control == 'text')
			value = form.children[i].children[1].value;
			
		if (form.children[i].dataset.control == 'multi-text')
			value = form.children[i].children[1].value;
			
		if (form.children[i].dataset.control == 'date')
			value = form.children[i].children[1].value;
			
		if (form.children[i].dataset.control == 'select')
			value = form.children[i].children[1].options[form.children[i].children[1].selectedIndex].value;
			
		if (form.children[i].dataset.control == 'bool')
		{
			value = form.children[i].children[0].children[0].value;
			item = form.children[i].children[0].children[1].innerHTML;
		}
		if (form.children[i].dataset.control == 'checkbox')
		{
			value = [];
			for (var j = 1; j < form.children[i].children.length; j++)
			{
				var jval = form.children[i].children[j];
				if(jval.children[0].checked)
					value.push(jval.children[0].value);
			}
		}
		if (form.children[i].dataset.control == 'radiobutton')
			for (var j = 1; j < form.children[i].children.length; j++)
			{
				var jval = form.children[i].children[j];
				if(jval.children[0].checked)
					value = jval.children[0].value;
			}
		if (form.children[i].dataset.control == 'label')
		{
			value = ""
			item = ""
		}
		if (form.children[i].dataset.control == 'hr')
		{
			value = ""
			item = ""
		}
		if (form.children[i].dataset.control == 'formInfo')
		{
			value = form.children[i].children[0].value;
			item = form.children[i].children[0].dataset.control;
		}
		if(item!="")
		{
			data[item] = value;
			//alert("item: " + item + " value: " + value);
		}
	}
	//alert(JSON.stringify(data));
	$.ajax({dataType:"json",
		url:"/CMSContent/v1/api/formResponse.json",
		data:{"form":JSON.stringify(data),"replyType":"${replyType}"},
		success:formReponseRecieved});
	var sub = document.getElementById("submit_btn");
	sub.setAttribute("disabled","disabled");
}
function formReponseRecieved(data, textStatus, jqXHR)
{
	var sub = document.getElementById("submit_btn");
	sub.removeAttribute("disabled");
	//alert("Response recieved" + JSON.stringify(data));
	if(data["result"] == "failure")
		alert("Error submitting your response. Please try again later");
	else
		alert("Your response has been submitted");
}
</script>
