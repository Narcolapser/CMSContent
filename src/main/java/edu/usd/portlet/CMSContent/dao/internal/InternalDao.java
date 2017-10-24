package edu.usd.portlet.cmscontent.dao;

public interface InternalDao extends CMSDocumentDao{
	public CMSDocument getDocument(String Id);
	public void saveDocument(CMSDocument val);
	}
