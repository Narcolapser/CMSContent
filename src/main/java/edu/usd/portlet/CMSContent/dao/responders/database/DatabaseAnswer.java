package edu.usd.portlet.cmscontent.dao;

import java.util.ArrayList;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import javax.persistence.ManyToOne;
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
@Table(name = "CMSResponseAnswer")
public class DatabaseAnswer
{
	@Transient
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "answer_id")
	protected int id;
	
	@Column(name = "field", columnDefinition="TEXT")
	protected String field;
	
	@Column(name = "answer", columnDefinition="TEXT")
	protected String answer;
	
	@ManyToOne
	@JoinColumn(name="response_id", nullable=false)
	protected DatabaseResponse response_id;
	
	public String getField()
	{
		return this.field;
	}
	
	public void setField(String val)
	{
		this.field = val;
	}
	
	public String getAnswer()
	{
		return this.answer;
	}
	
	public void setAnswer(String val)
	{
		this.answer = val;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int val)
	{
		this.id = val;
	}
	
	public DatabaseResponse getResp()
	{
		return this.response_id;
	}
	
	public void setResp(DatabaseResponse val)
	{
		this.response_id = val;
	}
}

//{"formId":"testingtext","username":"Toben.Archer","Da label yo":"tetss","The drop down":"unselected","replyType":"Database","Submit":"Database"}
