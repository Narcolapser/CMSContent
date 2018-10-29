Vue.component('config-bar', {
	data: function () {
		return {
			icon: "fa fa-plus-square",
			status: "collapse"
		}
	},
	props: ['return_url'],
	template: `<div>
	<table style="width: 100%;">
		<tr>
			<td>
				Layout: 
			</td>
			<td>
				<config-layout-selector selected="layouts/expanding" v-on:layoutChanged="layoutChanged"></config-layout-selector>
			</td>
			<td>
				Document Source:
			</td>
			<td>
				<config-document-source selected="Internal" v-on:sourceChanged="sourceChanged"></config-document-source>
			</td>
			<td>
				Document:
			</td>
			<td>
				<config-document source="Internal"></config-document>
			</td>
			<td>
				<a type="button" :href="return_url" class="btn btn-default">Done</a>
			</td>
		</tr>
	</table>
</div>`,
	mounted: function() {
		console.log("config-bar loaded.");
	},
	updated: function() {
	},
	methods:
	{
		layoutChanged: function(layout)
		{
			console.log("Layout has changed: " + layout);
		},
		sourceChanged: function(source)
		{
			console.log("Source has changed: " + source);
		}
	}
})

