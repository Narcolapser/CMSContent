package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

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

public class ReportDoc extends CMSDocument
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private Map<String,Object> attributes;

	private SwallowingJspRenderer jspRenderer;
	
	private List<DatabaseResponse> responses;
	
	private String[] fields;

	public SwallowingJspRenderer getJspRenderer()
	{
		return this.jspRenderer;
	}
	
	public void setJspRenderer(SwallowingJspRenderer val)
	{
		this.jspRenderer = val;
	}

	public ReportDoc(){}

	public ReportDoc(CMSDocument val)
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
		logger.debug("Personalizing from ReportDoc");
		this.attributes = attributes;
	}
	public String render()
	{
		String html = "error rendering";
		try
		{
			Map<String,Object> model = new HashMap<String,Object>();
//			JSONArray obj = new JSONArray(this.content);
//			ArrayList<JSONObject> jobj = new ArrayList<JSONObject>();
//			for(int i = 0; i < obj.length(); i++)
//			{
//				JSONObject val = obj.getJSONObject(i);
//				if(!val.has("required"))
//				{
//					logger.debug("Object did not have the 'required' field, this just means the form was made before the 'required' field was a thing.");
//					val.put("required",false);
//				}
//				jobj.add(obj.getJSONObject(i));
//			}
//			model.put("content",model);
//			model.put("id",this.id);
//			model.put("json",jobj);
			model.put("responses",this.responses);
			
//			HashSet<String> fields = new HashSet<>();
//			for(DatabaseResponse resp:this.responses)
//				fields.addAll(resp.getFields());
			model.put("fields",this.fields);
			
			if (this.attributes != null)
				model.put("username",this.attributes.get("username"));
			html = jspRenderer.render("report",model);
		}
		catch (Exception e)
		{
			logger.debug("Error rendering:");
			logger.debug(e);
		}
		
		return html;
	}
	public List<DatabaseResponse> getResponses()
	{
		return this.responses;
	}
	
	public void setResponses(List<DatabaseResponse> val)
	{
		this.responses = val;
	}
	
	public String[] getFields()
	{
		return this.fields;
	}
	
	public void setFields(String[] val)
	{
		this.fields = val;
	}
}
