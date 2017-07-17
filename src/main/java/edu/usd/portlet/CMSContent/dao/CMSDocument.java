package edu.usd.portlet.cmscontent.dao;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;

import org.hibernate.annotations.Type;


/**
 * @author Toben Archer
 * @version $Id$
 */

@Entity
@Table(name = "CMSDocument")
public class CMSDocument
{
	@Id
	@Column(name = "id")
	protected String Id;
	
	@Column(name = "title")
	protected String title;
	
	@Column(name = "source")
	protected String source;
	
	@Column(name = "content", columnDefinition="TEXT")
	protected String content;

	public CMSDocument(){}

	public CMSDocument(String title, String Id, String source, String content)
	{
		this.title = title;
		this.Id = Id;
		this.source = source;
		this.content = content;
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
	
	public String toString()
	{
		String ret = "Title: " + this.title;
		ret += " Id: " + this.Id;
		ret += " Source: " + this.source;
		if (this.content != null)
			ret += " Content Hash: " + this.content.hashCode();
		else
			ret += " Content Hash: empty";
		return ret;
	}
}
