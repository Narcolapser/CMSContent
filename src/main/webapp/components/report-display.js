Vue.component('report-display', {
	data: function () {
		return {
			start:0,
			end:25,
			loadAmount:25
		}
	},
	props: ["fields","report","rows"],
	template: `<div>
	<table id="reportTable" class="table table-striped">
		<tr id="headerRow" style="background: #f9f9f9;border-top: 1px solid lightgray;">
		</tr>
	</table>
	<div style="text-align:center;">
		<button class="btn btn-default" id="loadButton" v-on:click="loadMore()">Load more</button>
	</div>
</div>
	`,
	mounted: function() {
			this.end = this.rows;
			this.start = this.rows - this.loadAmount;
			if (this.start < 0)
				this.start = 0;
			var f = this.fields;
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
			this.loadRows();
	},
	updated: function() {
	},
	methods:
	{
		loadMore()
		{
			this.loadRows();
		},
		loadRows()
		{
			var report = this.report;
			var f = this.fields;
			var request = new XMLHttpRequest();
			request.open('GET','/CMSContent/v2/report/pagination?report='+report+"&start="+this.start+"&end="+this.end,true);
			request.onload = function()
			{
				var responses = JSON.parse(request.responseText);
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
		}
	}
})
