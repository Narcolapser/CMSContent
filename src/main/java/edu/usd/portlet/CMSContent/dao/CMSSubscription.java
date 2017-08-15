package edu.usd.portlet.cmscontent.dao;

import java.util.List;

public class CMSSubscription
{
	private String docId;
	private String docSource;
	private List<String> securityGroups;
	
	public CMSSubscription(){}
	
	public CMSSubscription(String DocId, String DocSource, List<String> secGroups)
	{
		this.docId = DocId;
		this.docSource = DocSource;
		this.securityGroups = secGroups;
	}
	
	public String getDocId()
	{
		return this.docId;
	}
	
	public void setDocId(String val)
	{
		this.docId = val;
	}
	
	public String getDocSource()
	{
		return this.docSource;
	}
	
	public void setDocSource(String val)
	{
		this.docSource = val;
	}
	
	public List<String> getSecurityGroups()
	{
		return this.securityGroups;
	}
	
	public void setSecurityGroups(List<String> val)
	{
		this.securityGroups = val;
	}
}
