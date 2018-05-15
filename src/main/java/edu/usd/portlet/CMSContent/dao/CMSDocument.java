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
	protected String Id;
	
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
	
	protected String contentJson;

	public CMSDocument(){}

	public CMSDocument(String title, String Id, String source, String docType, String content, String keyTerms, boolean removed)
	{
		this.title = title;
		this.Id = Id;
		this.source = source;
		this.docType = docType;
		this.content = content;
		this.keyTerms = keyTerms;
		this.removed = removed;
	}
	public CMSDocument(String title, String Id, String source, String docType, String content)
	{
		this.title = title;
		this.Id = Id;
		this.source = source;
		this.docType = docType;
		this.content = content;
		this.keyTerms = "";
		this.removed = false;
	}
	public CMSDocument(String title, String Id)
	{
		this.title = title;
		this.Id = Id;
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
		return this.Id;
	}
	public void setId(String val)
	{
		this.Id = val;
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
		ret += " Id: " + this.Id;
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
}
