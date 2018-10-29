Vue.component('config-document', {
	data: function () {
		return {
		}
	},
	props: ['source'],
	template: `<div>
	<select id="doc_options" v-on:change="doc_change">
		<option disabled>Loading...</option>
	</select>
	<button class="btn btn-info" >Add</button>
</div>`,
	mounted: function() {
			var v = this;
			var request = new XMLHttpRequest();
			request.open('GET','/CMSContent/v2/document/list?source='+this.source,true);
			request.onload = function()
			{
				var documents = JSON.parse(request.responseText);
				var sel = document.getElementById("doc_options");
				sel.options.length = 0;
				var option = document.createElement("option");
				option.text = " - Select - ";
				option.disabled = "disabled";
				sel.add(option);
				for(doc in documents)
				{
					var option = document.createElement("option");
					option.text = documents[doc]['title'] + " id: " + documents[doc]['id'];
					option.value = documents[doc]['id'];
					sel.add(option);
				}
			}
			request.send();
	},
	updated: function() {
	},
	methods:
	{
		doc_change: function()
		{
			var sel = document.getElementById("source_options");
			this.$emit('docChanged',sel.value);
		}
	}
})



