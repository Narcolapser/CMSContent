package edu.usd.portlet.cmscontent.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
}

//{"formId":"testingtext","username":"Toben.Archer","Da label yo":"tetss","The drop down":"unselected","replyType":"Database","Submit":"Database"}
