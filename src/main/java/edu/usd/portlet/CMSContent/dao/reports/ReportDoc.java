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
	
	private Token token;

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
			model.put("responses",this.responses);
			model.put("fields",this.fields);
			model.put("report",this.id);
			model.put("token",this.token);
			
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
	public Token getToken()
	{
		return this.token;
	}
	
	public void setToken(Token val)
	{
		this.token = val;
	}
}
