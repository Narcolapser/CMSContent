Vue.component('form-fields', {
	data: function () {
		return {
			fields: []
		}
	},
	props: ['form_id'],
	template: `
	<div>
		<table id="control_table" class="table table-striped">
			<thead>
				<th>Position</th>
				<th>Form Field</th>
				<td></td>
			</thead>
			<tbody id="control_body">
				<tr v-for="field in fields">
					<td class="pos_col" >
						<div class="btn-group" role="group">
							<button onclick="move_control('up',this);return false" class="btn btn-default">
								<i class="fa fa-arrow-up"></i>
							</button>
							<button onclick="move_control('down',this);return false" class="btn btn-default">
								<i class="fa fa-arrow-down"></i>
							</button>
						</div>
					</td>
					<td>
						<form-field ref="field" :label="field['label']" :type="field['type']" :required="field['required']" :options="field['options']"></form-field>
					</td>
					<td><button class="btn btn-danger" onclick="remove_control(this);return false;">Remove</button></td>
				</tr>
			</tbody>
		</table>
		<button v-on:click="add_field()" class="btn btn-info" title="Add another control">
			<i class="fa fa-plus-square"></i>
		</button>
		<button v-on:click="get_fields()">test save</button>
	</div>
	`,
	mounted: function() {
		console.log(this.form_id);
		var self = this;
		var request = new XMLHttpRequest();
		//http://localhost:8080/CMSContent/v2/documents/InternalForms/19964-press-release-request-form
		request.open('GET','/CMSContent/v2/documents/InternalForms/'+this.form_id,true);
		request.onload = function()
		{
			var document = JSON.parse(request.responseText);
			var content = JSON.parse(document['content']);
			for(var i = 0; i < content.length; i++)
				self.fields.push(content[i]);
		}
		request.send();
		
	},
	updated: function() {
	},
	methods:
	{
		add_field()
		{
			this.fields.push({ type: "label", label: "", required: false, options: []});
		},
		get_fields()
		{
			console.log("Getting fields");
			var fields = [];
			for(var i = 0; i < this.$refs.field.length; i++)
				fields.push(this.$refs.field[i].get_structure());
			
			console.log(fields);
			return fields;
		}
	}
})


