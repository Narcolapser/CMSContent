Vue.component('config-layout-selector', {
	data: function () {
		return {
		}
	},
	props: ['selected'],
	template: `<div>
	<select id="layout_options" v-on:change="layout_change">
		<option disabled>Loading...</option>
	</select>
</div>`,
	mounted: function() {
			var v = this;
			var request = new XMLHttpRequest();
			request.open('GET','/CMSContent/v2/layout/list',true);
			request.onload = function()
			{
				var layouts = JSON.parse(request.responseText);
				var sel = document.getElementById("layout_options");
				sel.options.length = 0;
				var option = document.createElement("option");
				option.text = " - Select - ";
				option.disabled = "disabled";
				sel.add(option);
				for(key in layouts)
				{
					var option = document.createElement("option");
					option.text = layouts[key];
					option.value = key;
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
		layout_change: function()
		{
			var sel = document.getElementById("layout_options");
			this.$emit('layoutChanged',sel.value);
		}
	}
})

