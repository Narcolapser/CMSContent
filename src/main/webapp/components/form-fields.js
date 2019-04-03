Vue.component('form-fields', {
	data: function () {
		return {
			fields: [],
			responders: []
		}
	},
	props: ['form_id'],
	template: `
	<div>
		<table id="control_table" class="table table-striped">
			<tbody id="control_body">
				<tr v-for="(field, index) in fields" :key="field.id">
					<td class="pos_col" >
						<div class="btn-group" role="group">
							<button v-on:click="move_up(index)" class="btn btn-default">
								<i class="fa fa-arrow-up"></i>
							</button>
							<button v-on:click="move_down(index)" class="btn btn-default">
								<i class="fa fa-arrow-down"></i>
							</button>
						</div>
					</td>
					<td id="fields_cell">
						<form-field ref="field" 
								:label="field['label']" 
								:type="field['type']" 
								:required="field['required']" 
								:options="field['options']"
								:id="field['id']"
								v-on:remove="fields.splice(index,1)"></form-field>
					</td>
				</tr>
			</tbody>
		</table>
		<button v-on:click="add_field()" class="btn btn-info" title="Add another control">
			<i class="fa fa-plus-square"></i>
		</button>
	</div>
	`,
	mounted: function() {
		load(this.form_id);
	},
	updated: function() {
	},
	methods:
	{
		add_field()
		{
			this.fields.push({ type: "label", label: "", required: false, options: [], id:this.new_id()});
		},
		get_fields()
		{
			var fields = [];
			for(var i = 0; i < this.fields.length; i++)
				for(var j=0; j < this.$refs.field.length; j++)
					if (this.fields[j]['id'] == this.$refs.field[i]['id'])
						fields.push(this.$refs.field[j].get_structure());
			
			return fields;
		},
		load(form_id)
		{
			var self = this;
			var request = new XMLHttpRequest();
			this.clear();
			request.open('GET','/CMSContent/v2/documents/InternalForms/'+form_id,true);
			request.onload = function()
			{
				var document = JSON.parse(request.responseText);
				var content = JSON.parse(document['content']);
				for(var i = 0; i < content.length; i++)
				{
					if (content[i].type=='respType')
						self.responders.push(content[i]);
					else
					{
						content[i]['id'] = self.new_id();
						self.fields.push(content[i]);
					}
				}
			}
			request.send();
		},
		clear()
		{
			while (this.fields.length > 0) this.fields.pop();
			while (this.responders.length > 0) this.responders.pop();
		},
		new_id(size)
		{
			size = size || 21;
			var url = 'Uint8ArdomValuesObj012345679BCDEFGHIJKLMNPQRSTWXYZ_cfghkpqvwxyz-';
			var id = '';
			var bytes = crypto.getRandomValues(new Uint8Array(size));
			while (0 < size--) 
				id += url[bytes[size] & 63];
			return id;
		},
		move_up(index)
		{
			if (index == 0)
				return;
			var val = this.fields.splice(index,1)[0];
			this.fields.splice(index-1,0,val);
		},
		move_down(index)
		{
			if (index == this.fields.length -1)
				return;
			var val = this.fields.splice(index,1)[0];
			this.fields.splice(index+1,0,val);
		}
	}
})
