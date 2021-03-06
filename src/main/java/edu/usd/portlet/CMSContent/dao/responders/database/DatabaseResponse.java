package edu.usd.portlet.cmscontent.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;


import org.hibernate.annotations.Type;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * This class holds the primary part of the form response. specifically the
 * specific response id, the user who responded, the time of response, and the
 * form that was responded to. It will map to DatabaseAnswers with the specific
 * field and it's answers.
 * @author Toben Archer
 * @version $Id$
 */

@Entity
@Table(name = "CMSResponse")
public class DatabaseResponse
{
	@Transient
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "response_id")
	protected int id;
	
	@Column(name = "username")
	protected String username;
	
	@Column(name = "form")
	protected String form;
	
	@Column(name = "response_time")
	protected String responseTime;
	
	@OneToMany(mappedBy="response_id")
	private Set<DatabaseAnswer> answers;
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int val)
	{
		this.id = val;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public void setUsername(String val)
	{
		this.username = val;
	}
	
	public String getForm()
	{
		return this.form;
	}
	
	public void setForm(String val)
	{
		this.form = val;
	}
	
	public String getResponseTime()
	{
		return this.responseTime;
	}
	
	public void setResponseTime(String val)
	{
		this.responseTime = val;
	}
	
	public Set<DatabaseAnswer> getAnswers()
	{
		return this.answers;
	}
	
	public void setAnswers(Set<DatabaseAnswer> val)
	{
		this.answers = val;
	}
	
	public List<String> getFields()
	{
		List<String> ret = new ArrayList<>();
		ret.add("Response Number");
		ret.add("Date Time");
		ret.add("Username");
		for(DatabaseAnswer answer:answers)
			if(!ret.contains(answer.getField()))
				ret.add(answer.getField());
		return ret;
	}
	public String getAnswer(String field)
	{
		for(DatabaseAnswer answer:answers)
			if(answer.getField().equals(field))
				return answer.getAnswer();
		return "";
	}
	public String json()
	{
		String ret = "{";
		for (DatabaseAnswer ans:this.answers)
		{
			ret += "\"" + ans.getField().replace("\"","\\\"")+"\":\""+ans.getAnswer().replace("\"","\\\"")+"\",";
		}
		ret += "\"Response Number\":\""+this.id+"\",";
		ret += "\"Username\":\""+this.username.replace("\"","\\\"")+"\",";
		ret += "\"Date Time\":\""+this.responseTime.replace("\"","\\\"")+"\",";
		ret = ret.replace("\t","");
		return ret.substring(0,ret.length()-1) + "}";
	}
	public String csvRow()
	{
		String ret = "";
		ret += this.id;
		ret += ",\"" + this.responseTime.replace("\"","\"\"")+"\"";
		ret += ",\"" + this.username.replace("\"","\"\"")+"\"";
		for (DatabaseAnswer ans:this.answers)
			ret += ",\"" + ans.getAnswer().replace("\"","\"\"")+"\"";
		return ret;
	}
	
	public Map<String,String> asMap()
	{
		Map<String,String> ret = new HashMap<>();
		ret.put("Response Number",""+this.id);
		ret.put("Username",this.username);
		ret.put("Date Time",this.responseTime);
		for (DatabaseAnswer ans:this.answers)
			ret.put(ans.getField(),ans.getAnswer());
		return ret;
	}
}
