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

/**
 * @author Toben Archer
 * @version $Id$
 */

@Entity
@Table(name = "CMSPageInfo")
public class CMSPageInfo
{
	@Column(name = "TITLE", nullable = true, unique = false)
	protected String title;
	@Column(name = "PATH", nullable = false, unique = true)
	protected String path;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID", updatable = false)
	protected int id;

	public CMSPageInfo(){}

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
	public int getId()
	{
		return this.id;
	}
	public void setId(int val)
	{
		this.id = val;
	}
	public String toString()
	{
		String ret = "Title: " + this.title;
		ret += " Path: " + this.path;
		return ret;
	}
}
