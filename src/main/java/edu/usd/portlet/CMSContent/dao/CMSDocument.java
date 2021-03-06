package edu.usd.portlet.cmscontent.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author Toben Archer
 * @version $Id$
 */

@Entity
@Table(name = "CMSDocument")
public class CMSDocument
{
	@Transient
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Id
	@Column(name = "id")
	protected String id;
	
	@Column(name = "docPath")
	protected String path;
	
	@Column(name = "title")
	protected String title;
	
	@Column(name = "source")
	protected String source;
	
	@Column(name = "docType")
	protected String docType;
	//Current supported types: html, form

	@Column(name = "content", columnDefinition="TEXT")
	protected String content;
	
	@Column(name = "keyTerms")
	protected String keyTerms;
	
	@Column(name = "removed")
	protected boolean removed;

	public CMSDocument(){}
	
	public CMSDocument(CMSDocument val)
	{
		this.id = val.id;
		this.path = val.path;
		this.title = val.title;
		this.source = val.source;
		this.docType = val.docType;
		this.content = val.content;
		this.keyTerms = val.keyTerms;
		this.removed = val.removed;
	}

	public CMSDocument(String title, String path, String id, String source, String docType, String content, String keyTerms, boolean removed)
	{
		this.title = title;
		this.path = path;
		this.id = id;
		this.source = source;
		this.docType = docType;
		this.content = content;
		this.keyTerms = keyTerms;
		this.removed = removed;
	}
	public CMSDocument(String title, String path, String id, String source, String docType, String content)
	{
		this.title = title;
		this.path = path;
		this.id = id;
		this.source = source;
		this.docType = docType;
		this.content = content;
		this.keyTerms = "";
		this.removed = false;
	}
	public CMSDocument(String json)
	{
		this.title = "";
		this.id = "";
		this.path = "";
		this.source = "";
		this.docType = "";
		this.content = "";
		this.keyTerms = "";
		this.removed = true;
		try
		{
			JSONObject obj = new JSONObject(json);
			this.id = obj.getString("id");
			this.path = obj.getString("path");
			this.title = obj.getString("title");
			this.source = obj.getString("source");
			this.docType = obj.getString("docType");
			this.content = obj.getString("content");
			this.keyTerms = obj.getString("keyTerms");
			this.removed = obj.getBoolean("removed");
		}
		catch(JSONException e)
		{
			logger.error("Failure to decode json: " + json + " error: " + e);
		}
	}
	public CMSDocument(String title, String id)
	{
		this.title = title;
		this.id = id;
	}
	public String getTitle()
	{
		return this.title;
	}
	public void setTitle(String val)
	{
		this.title = val;
	}
	public String getId()
	{
		return this.id;
	}
	public void setId(String val)
	{
		this.id = val;
	}
	public String getPath()
	{
		return this.path;
	}
	
	public void setPath(String val)
	{
		this.path = val;
	}
	public String getSource()
	{
		return this.source;
	}
	public void setSource(String val)
	{
		this.source = val;
	}
	public String getContent()
	{
		return this.content;
	}
	public void setContent(String val)
	{
		this.content = val;
	}
	public String getDocType()
	{
		return this.docType;
	}
	
	public void setDocType(String val)
	{
		this.docType = val;
	}
	
	public String getKeyTerms()
	{
		return this.keyTerms;
	}
	
	public void setKeyTerms(String val)
	{
		if(val == null)
			this.keyTerms = "";
		else
			this.keyTerms = val;
	}
	public boolean getRemoved()
	{
		return this.removed;
	}
	
	public void setRemoved(boolean val)
	{
		this.removed = val;
	}
	
	public String toString()
	{
		String ret = "Title: " + this.title;
		ret += " id: " + this.id;
		ret += " Source: " + this.source;
		ret += " Type: " + this.docType;
		if (this.content != null)
			ret += " Content Hash: " + this.content.hashCode();
		else
			ret += " Content Hash: empty";
		return ret;
	}
	public void personalize(Map<String,Object> attributes)
	{
		logger.debug("personalizing in cmsdocument");
		//this method to be extended by downstream objects for when personalization is needed.
		//Currently the only expected attribute is UID.
	}
	public String render()
	{
		return this.content;
	}
	
	public String toJSON()
	{
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("id",this.id);
			obj.put("title",this.title);
			obj.put("source",this.source);
			obj.put("docType",this.docType);
			obj.put("content",this.content);
			obj.put("keyTerms",this.keyTerms);
			obj.put("removed",this.removed);
			obj.put("docPath",this.path);
			return obj.toString();
		}
		catch(JSONException e)
		{
			logger.error("Failure to encode json: " + this.id + " error: " + e);
			return "{}";
		}
	}
}
