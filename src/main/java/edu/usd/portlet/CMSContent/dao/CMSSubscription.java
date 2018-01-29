package edu.usd.portlet.cmscontent.dao;

import java.util.List;

public class CMSSubscription
{
	private String docId;
	private String docSource;
	private String docTitle;
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
	public String getDocTitle()
	{
		return this.docTitle;
	}
	
	public void setDocTitle(String val)
	{
		this.docTitle = val;
	}
	public CMSDocumentDao getDao(List<CMSDocumentDao> dataSources)
	{
		for(CMSDocumentDao ds:dataSources)
			if(ds.getDaoName().equals(this.docSource))
				return ds;
		return null;
	}
	public String toString()
	{
		return "Doc ID: " + this.docId + " Source: " + this.docSource + " Title: " + this.docTitle + " Number of security groups: " + this.securityGroups.size();
	}
}
