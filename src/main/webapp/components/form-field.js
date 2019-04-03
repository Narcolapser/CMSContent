Vue.component('form-field', {
	data: function () {
		return {
			"field_types":{
				"label":{"reqable":false,"options":false},
				"hr":{"reqable":false,"options":false},
				"p":{"reqable":false,"options":false},
				"text":{"reqable":true,"options":false},
				"multi-text":{"reqable":true,"options":false},
				"date":{"reqable":true,"options":false},
				"datetime":{"reqable":true,"options":false},
				"select":{"reqable":true,"options":true},
				"multi-select":{"reqable":true,"options":true},
				"checkbox":{"reqable":false,"options":true},
				"radiobutton":{"reqable":true,"options":true}
			},
		}
	},
	props: ['type','label','required','options','id'],
	template: `
		<div>
			<div style="width: 50%;float: left;" >
				<h2>Form Field</h2>
				<table style="width:100%">
					<tr>
						<td>Field Type:</td>
						<td>
							<select name="type" class="form-control field_type" v-on:change="type_change()" >
								<optgroup label="Informative/Structural" >
									<option value="label" >Label</option>
									<option value="hr" >Horrizontal Line</option>
									<option value="p" >Paragraph</option>
								</optgroup>
								<optgroup label="Entry" >
									<option value="text" >Text</option>
									<option value="multi-text" >Multi-line Text</option>
									<option value="date" >Date picker</option>
									<option value="datetime" >Date Time</option>
									<option value="select" >Drop Down</option>
									<option value="multi-select" >Multi-select</option>
									<option value="checkbox" >Checkbox</option>
									<option value="radiobutton" >Radio Button</option>
								</optgroup>
							</select>
						</td>
					</tr>
					<tr>
						<td>Field Label:</td>
						<td><textarea name="label" type="text" class="form-control field_label" ></textarea></td>
					</tr>
					<tr class="required_row" >
						<td>Input Required:</td>
						<td>
							<input class="field_required" type="checkbox" name="Required" value="required"></input>
						</td>
					</tr>
				</table>
			</div>
			<div style="width: 40%;float: left;" >
				<div class="options" >
					<h2>Options</h2>
					<table style="width: 100%;" >
						<tr>
							<td><select multiple class="form-control field_options" ></select></td>
							<td><button v-on:click="remove_option()" ><i class="fa fa-times" ></i></button></td>
						</tr>
						<tr>
							<td>New item: <input type="text" class="option_in" ></input></td>
							<td><button v-on:click="add_option()" ><i class="fa fa-plus" ></i></button></td>
						</tr>
					</table>
				</div>
			</div>
			<div style="width: 10%;float: left;" ><button class="btn btn-danger" v-on:click="$emit('remove')">Remove</button></div>
		</div>
	`,
	mounted: function() {
		var field_type = this.$el.getElementsByClassName("field_type")[0];
		for(var i = 0; i < field_type.options.length; i++)
			if(field_type.options[i].value == this.type)
				field_type.options[i].selected = true;

		this.$el.getElementsByClassName("field_label")[0].value = this.label;

		var options = this.$el.getElementsByClassName("field_options")[0];
		var op_array = [];
		if(typeof this.options == 'string')
			op_array = this.options.split(",");
		else
			op_array = this.options;
		for(var i = 0; i < op_array.length; i++)
		{
			var new_op = document.createElement("option");
			new_op.text = op_array[i];
			options.add(new_op);
		}
		
		this.type_change();
		
		this.$el.getElementsByClassName("field_required")[0].checked = this.required;
	},
	updated: function() {
	},
	methods:
	{
		type_change()
		{
			var field_type = this.$el.getElementsByClassName("field_type")[0].value;
			if (this.field_types[field_type]["reqable"])
			{
				this.$el.getElementsByClassName("required_row")[0].style="";
				this.$el.getElementsByClassName("field_required")[0].checked = false;
			}
			else
				this.$el.getElementsByClassName("required_row")[0].style="visibility:hidden";
			
			if (this.field_types[field_type]["options"])
				this.$el.getElementsByClassName("options")[0].style=""
			else
				this.$el.getElementsByClassName("options")[0].style="visibility:hidden;"
		},
		get_structure()
		{
			var struct = {};
			struct['type'] = this.$el.getElementsByClassName("field_type")[0].value;
			struct['label'] = this.$el.getElementsByClassName("field_label")[0].value;
			struct['required'] = this.$el.getElementsByClassName("field_required")[0].checked;
			var options_html = this.$el.getElementsByClassName("field_options")[0];
			var options = [];
			for(var i = 0; i < options_html.options.length; i++)
			{
				options.push(options_html.options[i].value);
			}
			options.sort();
			struct['options'] = options.join();
			return struct;
		},
		remove_option()
		{
			var options = this.$el.getElementsByClassName("field_options")[0];
			for(var i = 0; i < options; i++)
			{
				if(options.options[i].selected)
				{
					options.remove(i);
					i--;
				}
			}
		},
		add_option()
		{
			var option = this.$el.getElementsByClassName("option_in")[0];
			var options = this.$el.getElementsByClassName("field_options")[0];
			var new_op = document.createElement("option");
			new_op.text = option.value;
			options.add(new_op);
			option.value = "";
		},
	}
})
