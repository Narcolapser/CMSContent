package edu.usd.portlet.cmscontent.dao;

import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

/**
 * 
 * @author Toben Archer
 * @version $Id$
 */

public class CMSPageContent
{
	private String title;
	private String content;
	private CMSPageInfo info;

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
