Vue.component('config-bar', {
	data: function () {
		return {
			icon: "fa fa-plus-square",
			status: "collapse"
		}
	},
	template: `<div>
	<table style="width: 100%;">
		<tr>
			<td>
				Layout: 
			</td>
			<td>
				<config-layout-selector selected="layouts/expanding"></config-layout-selector>
			</td>
			<td>
				Document Source:
			</td>
			<td>
				<Select>
					<option>Internal Document</option>
					<option>Internal Form</option>
					<option>Internal Report</option>
				</select>
			</td>
			<td>
				Document:
			</td>
			<td>
				<select>
					<option>Doc 1</option>
					<option>Doc 2</option>
					<option>Doc 3</option>
				</select>
			</td>
			<td>
				<a type="button" href="return_url" class="btn btn-default">Done</a>
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
	}
})

