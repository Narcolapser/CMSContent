package edu.usd.portlet.cmscontent.dao;

import java.util.List;

import edu.usd.portlet.cmscontent.dao.CMSDocument;

public interface InternalDocumentDao{
	public List<CMSDocument> getAllDocuments();
	public List<CMSDocument> getAllDocumentsContentLess();
	public List<CMSDocument> getAllForms();
	public List<CMSDocument> getAllFormsContentLess();
	public void insertDocument(CMSDocument doc);
	public CMSDocument getDocumentById(String id);
	public void deleteDocument(String id);
}
