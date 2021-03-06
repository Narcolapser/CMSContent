<%-- Author: Toben Archer | Version $Id$ --%>
<%@ page contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<p>* marks required fields</p>
<form id="${id}">
	<div data-control="formInfo"><input data-control="formId" type="hidden" class="form-control" value="${id}"/></div>
	<div data-control="formInfo"><input data-control="username" type="hidden" class="form-control" value="${username}"/></div>
	<div data-control="formInfo"><input data-control="useremail" type="hidden" class="form-control" value="${useremail}"/></div>
	<c:forEach var="control" items="${json}">
		<c:set var="val">${control.getString("type")}</c:set>
		<c:set var="s"></c:set>
		<c:set var="e"></c:set>
		<c:if test='${control.getString("required")}'>
			<c:set var="s"><strong>*</c:set>
			<c:set var="e"></strong></c:set>
		</c:if>
		<c:choose>
			<c:when test="${val eq 'text'}">
				<div data-control="text" data-required='${control.getString("required")}'><p>${s}${control.getString("label")}${e}</p><input type="text" class="form-control"></input></div>
			</c:when>
			<c:when test="${val eq 'select'}">
				<div data-control="select" data-required='${control.getString("required")}'>
					<p>${s}${control.getString("label")}${e}</p>
					<select class="form-control">
						<option value="unselected" selected disabled hidden>Select One</option>
						<c:forEach var="op" items="${control.getString('options').split(',')}">
							<option value="${op}">${op}</option>
						</c:forEach>
					</select>
				</div>
			</c:when>
			<c:when test="${val eq 'multi-select'}">
				<div data-control="multi-select" data-required='${control.getString("required")}'>
					<p>${s}${control.getString("label")}${e}</p>
					<select class="form-control" multiple>
						<option value="unselected" selected disabled hidden>Select One</option>
						<c:forEach var="op" items="${control.getString('options').split(',')}">
							<option value="${op}">${op}</option>
						</c:forEach>
					</select>
				</div>
			</c:when>
			<c:when test="${val eq 'bool'}">
				<div data-control="bool" data-required='${control.getString("required")}'><label class="checkbox-inline"><input type="checkbox"></input><p>${s}${control.getString("label")}${e}</p></label></div>
			</c:when>
			<c:when test="${val eq 'checkbox'}">
				<div data-control="checkbox" data-required='${control.getString("required")}'>
					<p>${s}${control.getString("label")}${e}</p>
					<c:forEach var="op" items="${control.getString('options').split(',')}">
						<div class="checkbox" style="margin-left: 20px;">
							<input type="checkbox" name="${op.replace(' ','_')}" value="${op}">${op}</input>
						</div>
					</c:forEach>
				</div>
			</c:when>
			<c:when test="${val eq 'radiobutton'}">
				<div data-control="radiobutton" data-required='${control.getString("required")}'>
					<p>${s}${control.getString("label")}${e}</p>
					<c:forEach var="op" items="${control.getString('options').split(',')}">
						<div class="radio" style="margin-left: 20px;">
							<input type="radio" name="${control.getString('label').replace(' ','_')}" value="${op}">${op}</input>
						</div>
					</c:forEach>
				</div>
			</c:when>
			<c:when test="${val eq 'hr'}">
				<div data-control="hr" data-required='${control.getString("required")}'><hr/></div>
			</c:when>
			<c:when test="${val eq 'date'}">
				<div data-control="date" data-required='${control.getString("required")}'><p>${s}${control.getString("label")}${e}</p><input type="date" class="form-control"></input></div>
			</c:when>
			<c:when test="${val eq 'datetime'}">
				<div data-control="datetime" data-required='${control.getString("required")}'>
					<p>${s}${control.getString("label")}${e}</p>
					<div class="input-group">
						<select type="text" class="form-control month-picker" style="min-width: 106px;" onChange="monthChange(this);">
							<option value="" disabled selected>Month</option>
							<option value="01">January</option>
							<option value="02">February</option>
							<option value="03">March</option>
							<option value="04">April</option>
							<option value="05">May</option>
							<option value="06">June</option>
							<option value="07">July</option>
							<option value="08">August</option>
							<option value="09">September</option>
							<option value="10">October</option>
							<option value="11">November</option>
							<option value="12">December</option>
						</select>
						<span class="input-group-addon">/</span>
						<select type="text" class="form-control day-picker" disabled="true" title="Select month first">
							<option value="" disabled selected>Day</option>
						</select>
						<span class="input-group-addon">/</span>
						<select type="text" class="form-control year-picker" onChange="leapYearCheck(this);"/>
							<option value="" disabled selected>Year</option>
						</select>
						<span class="input-group-addon"> </span>
						<select type="text" class="form-control hour-picker">
							<option value="" disabled selected>Hour</option>
						</select>
						<span class="input-group-addon">:</span>
						<select type="text" class="form-control minute-picker">
							<option value="" disabled selected>Minute</option>
						</select>
						<span class="input-group-addon"> </span>
						<select type="text" class="form-control ampm-picker" style="min-width: 75px;">
							<option value="" disabled selected>AM/PM</option>
							<option value="am">AM</option>
							<option value="pm">PM</option>
						</select>
					</div>
				</div>
				<!-- this is how I want to do it. but until it's universally supported we cant. -->
				<!--<div data-control="datetime" data-required='${control.getString("required")}'><p>${s}${control.getString("label")}${e}</p><input type="datetime-local" class="form-control"></input></div>-->
			</c:when>
			<c:when test="${val eq 'multi-text'}">
				<div data-control="multi-text" data-required='${control.getString("required")}'><p>${s}${control.getString("label")}${e}</p><textarea class="form-control"></textarea></div>
			</c:when>
			<c:when test="${val eq 'p'}">
				<div data-control="p" data-required='${control.getString("required")}'><p>${s}${control.getString("label")}${e}</p></div>
			</c:when>
			<c:when test="${val eq 'respType'}">
				<div data-control="formInfo" ><input data-control="replyType" type="hidden" class="form-control" value="${control.getString('label')}"/></div>
				<c:set var="replyType">${control.getString('label')}</c:set>
			</c:when>
			<c:otherwise>
				<div data-control="label" data-required='${control.getString("required")}'><h2>${control.getString("label")}</h2></div>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<a data-control="submit" id="submit_btn" class="btn btn-default" onclick="upload_form('${id}')">Submit</a>
	<div id="loading_icon" style="display:none;">
		<img src="/CMSContent/loading.gif" alt="Please wait..." style="height:50px"/>
	</div>
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

window.onload = function() {
	var yps = document.getElementsByClassName("year-picker");
	date = new Date();
	year = date.getFullYear() -2;
	for(var i = 0; i<yps.length; i++)
		for(var j = 0; j <= 10; j++)
			yps[i].options[j+1] = new Option(year+j,year+j,false,false);
	
	var hps = document.getElementsByClassName("hour-picker");
	var mps = document.getElementsByClassName("minute-picker");
	
	for(var i = 0; i < hps.length; i++)
		for(var j = 0; j < 12; j++)
			hps[i].options[j+1] = new Option(j+1,j+1,false,false);
	
	for(var i = 0; i < mps.length; i++)
		for(var j = 0; j < 60; j++)
			if(j<10)
				mps[i].options[j+1] = new Option("0"+j,"0"+j,false,false);
			else
				mps[i].options[j+1] = new Option(j,j,false,false);
};

function monthChange(selector)
{
	var dayInput = selector.parentNode.children[2];
	dayInput.disabled=false;
	var days = 30;
	if (['01','03','05','07','08','10','12'].includes(selector.value))
		days=31;
	else if (selector.value == '02')
		days=28;
	dayInput.options.length=0;
	dayInput.options[0] = new Option(1,1,true,false);
	for(var i = 2; i <= days; i++)
		dayInput.options[i-1] = new Option(i,i,false,false);
	dayInput.title="Day selector";
	leapYearCheck(selector);
}

function leapYearCheck(caller)
{
	var month = caller.parentNode.children[0];
	var day = caller.parentNode.children[2];
	var year = caller.parentNode.children[4];
	if (month.value == '02')
		if (year.value%4 == 0)
			day.options[day.options.length] = new Option("29","29",false,false);
		else if (day.options.length == 29)
			day.remove(28);
}

function upload_form(formId)
{
	console.log('Submitting form: ' + formId);
	var form = document.getElementById(formId);
	var item,value,data={};
	var missed_reqs = []
	for(var i = 0; i < form.children.length ; i++)
	{
		//set defaults
		value = "";
		item = "";
		if(form.children[i].innerHTML != "Submit")
			item = form.children[i].children[0].innerText.replace('*','');
		else
			item = "Submit";
		if (form.children[i].dataset.control == 'text')
		{
			value = form.children[i].children[1].value;
			if(form.children[i].dataset.required == 'true')
				if(value == '')
					missed_reqs.push(form.children[i].children[0].innerText.replace("*",""))
		}
		if (form.children[i].dataset.control == 'multi-text')
		{
			value = form.children[i].children[1].value;
			if(form.children[i].dataset.required == 'true')
				if(value == '')
					missed_reqs.push(form.children[i].children[0].innerText.replace("*",""))
		}
			
		if (form.children[i].dataset.control == 'date')
		{
			value = form.children[i].children[1].value;
			if(form.children[i].dataset.required == 'true')
				if(value == '')
					missed_reqs.push(form.children[i].children[0].innerText.replace("*",""))
		}
			
		if (form.children[i].dataset.control == 'datetime')
		{
			var div = form.children[i];
			var day = div.getElementsByClassName("day-picker")[0].value;
			var month = div.getElementsByClassName("month-picker")[0].value;
			var year = div.getElementsByClassName("year-picker")[0].value;
			var hour = div.getElementsByClassName("hour-picker")[0].value;
			var minute = div.getElementsByClassName("minute-picker")[0].value;
			var ampm = div.getElementsByClassName("ampm-picker")[0].value;
			
			var value = month + '/' + day + '/' + year + ' ' + hour + ':' + minute + ' ' + ampm;
			
			if(form.children[i].dataset.required == 'true')
			{
				var missing = [];
				if(month == '')
					missing.push('Month');
				if(day == '')
					missing.push('Day');
				if(year == '')
					missing.push('Year');
				if(hour == '')
					missing.push('Hour');
				if(minute == '')
					missing.push('Minute');
				if(ampm == '')
					missing.push('AM/PM');
				
				if(missing.length != 0)
					missed_reqs.push(form.children[i].children[0].innerText.replace("*","") + ' is missing: ' + missing.join(", "));
			}
		}

		if (form.children[i].dataset.control == 'select')
		{
			value = form.children[i].children[1].options[form.children[i].children[1].selectedIndex].value;
			if(form.children[i].dataset.required == 'true')
				if(value == '')
					missed_reqs.push(form.children[i].children[0].innerText.replace("*",""))
		}
		if (form.children[i].dataset.control == 'multi-select')
		{
			value = [];
			for(var j = 0; j < form.children[i].children[1].length; j++)
				if(form.children[i].children[1].options[j].selected) value.push(form.children[i].children[1].options[j].value);
			if(form.children[i].dataset.required == 'true')
				if(value == "unselected")
					missed_reqs.push(form.children[i].children[0].innerText.replace("*",""))
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
			if(form.children[i].dataset.required == 'true')
				if(value == [])
					missed_reqs.push(form.children[i].children[0].innerText.replace("*",""))
		}
		if (form.children[i].dataset.control == 'radiobutton')
		{
			for (var j = 1; j < form.children[i].children.length; j++)
			{
				var jval = form.children[i].children[j];
				if(jval.children[0].checked)
					value = jval.children[0].value;
			}
			if(form.children[i].dataset.required == 'true')
				if(value == '')
					missed_reqs.push(form.children[i].children[0].innerText.replace("*",""))
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
		if (form.children[i].dataset.control == 'p')
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
	if(missed_reqs.length > 0)
	{
		console.log("Missed some requirements.");
		var outs = missed_reqs.join('\n');
		console.log(outs);
		alert("You missed some required fields:\n" + outs);
	}
	else
	{
		//alert(JSON.stringify(data));
		console.log(data);
		$.ajax({dataType:"json", type: "POST",
			url:"/CMSContent/v2/forms/"+formId,
			data:{"form":JSON.stringify(data)},
			success:formReponseRecieved});
		var sub = document.getElementById("submit_btn");
		//sub.setAttribute("disabled","disabled");
		sub.style = "display:none;";
		var loading = document.getElementById("loading_icon");
		loading.style = "";
	}
}
function formReponseRecieved(data, textStatus, jqXHR)
{
	var sub = document.getElementById("submit_btn");
	sub.removeAttribute("disabled");
	var sub = document.getElementById("submit_btn");
	//sub.setAttribute("disabled","disabled");
	sub.style = "";
	var loading = document.getElementById("loading_icon");
	loading.style = "display:none;";
	//alert("Response recieved" + JSON.stringify(data));
	if(data["result"] == "failure")
		alert("Error submitting your response. Please try again later");
	else
		alert("Your response has been submitted");
}
</script>
