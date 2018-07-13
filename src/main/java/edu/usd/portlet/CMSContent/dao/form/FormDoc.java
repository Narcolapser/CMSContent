package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.usd.portlet.cmscontent.components.SwallowingJspRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

/**
 * @author Toben Archer
 * @version $Id$
 */

public class FormDoc extends CMSDocument
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private Map<String,Object> attributes;

	private SwallowingJspRenderer jspRenderer;

	public SwallowingJspRenderer getJspRenderer()
	{
		return this.jspRenderer;
	}
	
	public void setJspRenderer(SwallowingJspRenderer val)
	{
		this.jspRenderer = val;
	}

	public FormDoc(){}

	public FormDoc(CMSDocument val)
	{
		this.title = val.title;
		this.id = val.id;
		this.source = val.source;
		this.docType = val.docType;
		this.content = val.content;
		this.keyTerms = val.keyTerms;
		this.removed = val.removed;
	}
	public void personalize(Map<String,Object> attributes)
	{
		logger.debug("Personalizing from formdoc");
		this.attributes = attributes;
	}
	public String render()
	{
		String html = "error rendering";
		try
		{
			Map<String,Object> model = new HashMap<String,Object>();
			JSONArray obj = new JSONArray(this.content);
			ArrayList<JSONObject> jobj = new ArrayList<JSONObject>();
			for(int i = 0; i < obj.length(); i++)
			{
				JSONObject val = obj.getJSONObject(i);
				if(!val.has("required"))
				{
					logger.debug("Object did not have the 'required' field, this just means the form was made before the 'required' field was a thing.");
					val.put("required",false);
				}
				jobj.add(obj.getJSONObject(i));
			}
			model.put("content",model);
			model.put("id",this.id);
			model.put("json",jobj);
			if (this.attributes != null)
				model.put("username",this.attributes.get("username"));
			html = jspRenderer.render("form",model);
		}
		catch (Exception e)
		{
			logger.debug("Error rendering:");
			logger.debug(e);
		}
		
		return html;
	}
	public String[] getFields()
	{
		try
		{
			List<String> fields = new ArrayList<String>();
			JSONArray obj = new JSONArray(this.content);
			for(int i = 0; i < obj.length(); i++)
			{
				JSONObject val = obj.getJSONObject(i);
				if(!(val.getString("type").equals("label") || val.getString("type").equals("hr") || val.getString("type").equals("p")))
				{
					logger.debug("found a field with response");
					fields.add(val.getString("label"));
				}
			}
			return fields.toArray(new String[0]);
		}
		catch (Exception e)
		{
			logger.debug("Error rendering:");
			logger.debug(e);
			return null;
		}
	}
}
