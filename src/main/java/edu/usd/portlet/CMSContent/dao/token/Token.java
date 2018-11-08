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
@Table(name = "CMSToken")
public class Token
{
	@Transient
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Id
	@Column(name = "token_hash")
	protected String hash;
	
	@Column(name = "token_time")
	protected long time;
	
	public String getHash()
	{
		return this.hash;
	}
	
	public void setHash(String val)
	{
		this.hash = val;
	}
	
	public long getTime()
	{
		return this.time;
	}
	
	public void setTime(long val)
	{
		this.time = val;
	}
}
