Vue.component('report-display', {
	data: function () {
		return {
			start:0,
			end:25,
			loadAmount:25,
			rows:0,
			fields:[]
		}
	},
	props: ["report","token"],
	template: `<div>
	<table id="reportTable" class="table table-striped">
		<tr id="headerRow" style="background: #f9f9f9;border-top: 1px solid lightgray;">
		</tr>
	</table>
	<div style="text-align:center;">
		<button class="btn btn-default" id="loadButton" v-on:click="loadRows()">Load more</button>
		<a class="btn btn-info" id="downloadButton" v-bind:href="'/CMSContent/v2/report/'+ report +'.xlsx?token='+token">
			<i class="fa fa-file-excel-o" aria-hidden="true"></i> Download Spreadsheet
		</a>
	</div>
</div>
	`,
	mounted: function() {
		var report = this.report;
		self = this;
		var request = new XMLHttpRequest();
		request.open('GET','/CMSContent/v2/report/fields?report='+report,true);
		request.onload = function()
		{
			var f = JSON.parse(request.responseText);
			self.fields = f;
			var row = document.getElementById("headerRow");
			for(i = 0; i < f.length; i++)
			{
				var cell = row.insertCell();
				var snippet = f[i];
				if (snippet.length > 20)
					snippet = snippet.substring(0,20) + "...";
				cell.innerHTML = "<strong>" + snippet + "</strong>";
				cell.setAttribute("style","padding: 8px;");
				cell.title = f[i];
			}
			self.getRows();
		}
		request.send();
	},
	updated: function() {
	},
	methods:
	{
		loadRows()
		{
			var report = this.report;
			var f = this.fields;
			var token = this.token
			var request = new XMLHttpRequest();
			request.open('GET','/CMSContent/v2/report/pagination?report='+report+"&start="+this.start+"&end="+this.end+"&token="+token,true);
			request.onload = function()
			{
				var responses = JSON.parse(request.responseText);
				if ("reason" in responses)
				{
					if(responses['reason'] == 'Invalid token')
						alert("Error fetching more rows. Refresh page and try again.");
				}
				else
				{
					var table = document.getElementById("reportTable");
					for(j = responses.length - 1; j >= 0; j--)
					{
						var row = table.insertRow();
						for(i = 0; i < f.length; i++)
						{
							var cell = row.insertCell();
							cell.innerHTML = responses[j][f[i]];
							cell.setAttribute("style","padding: 8px;");
						}
						var rcount = table.getElementsByTagName("tr").length;
						var style = "border-top: 1px solid lightgray;";
						if(rcount%2 == 1)
							style = "border-top: 1px solid lightgray;background: #f9f9f9;";
						row.setAttribute("style",style);
					}
				}
			}
			request.send();
			if (this.start == 0)
			{
				var button = document.getElementById("loadButton");
				button.innerHTML = "all loaded";
				button.disabled = true;
			}
			else
			{
				this.start = this.start - this.loadAmount;
				this.end = this.end - this.loadAmount;
				if (this.start < 0)
					this.start = 0;
			}
		},
		getRows()
		{
			var request = new XMLHttpRequest();
			var report = this.report;
			var self = this;
			request.open('GET','/CMSContent/v2/report/rows?report='+report,true);
			request.onload = function()
			{
				var response = JSON.parse(request.responseText);
				self.rows = response['rowCount'];
				self.end = self.rows;
				self.start = self.rows - self.loadAmount;
				if (self.start < 0)
					self.start = 0;
				self.loadRows();
			}
			request.send();
		},
	}
})
