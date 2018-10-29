Vue.component('config-document-source', {
	data: function () {
		return {
		}
	},
	props: ['selected'],
	template: `<div>
	<select id="source_options" v-on:change="source_change">
		<option disabled>Loading...</option>
	</select>
</div>`,
	mounted: function() {
			var v = this;
			var request = new XMLHttpRequest();
			request.open('GET','/CMSContent/v2/document/sources',true);
			request.onload = function()
			{
				var sources = JSON.parse(request.responseText);
				var sel = document.getElementById("source_options");
				sel.options.length = 0;
				var option = document.createElement("option");
				option.text = " - Select - ";
				option.disabled = "disabled";
				sel.add(option);
				for(source in sources)
				{
					var option = document.createElement("option");
					option.text = sources[source]['disp_name'];
					option.value = sources[source]['name'];
					if (option.value == v.selected)
						option.selected = "selected";
					sel.add(option);
				}
			}
			request.send();
			//console.log("config-layout-selector loaded.");
	},
	updated: function() {
	},
	methods:
	{
		source_change: function()
		{
			var sel = document.getElementById("source_options");
			//console.log(sel.value);
			this.$emit('sourceChanged',sel.value);
		}
	}
})



