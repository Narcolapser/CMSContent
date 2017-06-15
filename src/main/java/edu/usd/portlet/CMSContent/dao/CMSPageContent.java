package edu.usd.portlet.cmscontent.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.hibernate.annotations.Type;

import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

/**
 * 
 * @author Toben Archer
 * @version $Id$
 */
@Entity
public class CMSPageContent
{
	@Id
	protected int id;
	protected String title;
	protected String content;
	protected CMSPageInfo info;
	
	public CMSPageContent(){}

	public CMSPageContent(String content, String title)
	{
		this.title = title;
		this.content = content;
	}
	public String getTitle()
	{
		return this.title;
	}
	public void setTitle(String val)
	{
		this.title = val;
	}
	public String getContent()
	{
		return this.content;
	}
	public void setContent(String val)
	{
		this.content = val;
	}
	public CMSPageInfo getInfo()
	{
		return this.info;
	}
	public void setInfo(CMSPageInfo val)
	{
		this.info = val;
	}
	public String toString()
	{
		return this.title;
	}
}
