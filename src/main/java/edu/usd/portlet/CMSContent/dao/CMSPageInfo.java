package edu.usd.portlet.cmscontent.dao;

/**
 * Java disgusts me.
 * This is a wrapper class because making tuples in java isn't a simple thing.
 * It stores a pair of strings, the title of a page and the path to it. 
 * It exists just so I can pass them back because that's idomatic java. 
 * @author Toben Archer
 * @version $Id$
 */

public class CMSPageInfo
{
	protected String title;
	protected String path;

	public CMSPageInfo(String title, String path)
	{
		this.title = title;
		this.path = path;
	}
	public String getTitle()
	{
		return this.title;
	}
	public void setTitle(String val)
	{
		this.title = val;
	}
	public String getPath()
	{
		return this.path;
	}
	public void setPath(String val)
	{
		this.path = val;
	}
	public String toString()
	{
		String ret = "Title: " + this.title;
		ret += " Path: " + this.path;
		return ret;
	}
}
