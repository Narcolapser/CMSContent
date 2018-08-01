package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.usd.portlet.cmscontent.components.SwallowingJspRenderer;
import org.springframework.beans.factory.annotation.Autowired;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

/**
 * This class provides a hand full of customizations to make the CMSForms work.
 * The biggest one is the custom render method which converts the json string in
 * which the form's structure is stored into an HTML string which the layout can
 * display. Doing it this way seperates the two concerns. Layouts need not know
 * anything about what a form is, and the forms don't need to worry about the
 * structure of the layout.
 * 
 * The method "getFields" is actually primarily for CMSReports. It allows for a
 * consistent listing of the fields in the form so that the reports can them
 * selves be consistent in their display.
 * 
 * @author Toben Archer
 * @version $Id$
 */

public class FormDoc extends CMSDocument
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private Map<String,Object> attributes;
	
	private String[] skip_fields = new String[]{"label","hr","p","respType"};

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
			{
				model.put("username",this.attributes.get("username"));
				model.put("useremail",this.attributes.get("useremail"));
			}
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
				if(!(Arrays.asList(skip_fields).contains(val.getString("type"))))
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
