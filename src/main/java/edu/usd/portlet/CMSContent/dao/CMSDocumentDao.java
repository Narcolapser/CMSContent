package edu.usd.portlet.cmscontent.dao;

import java.util.Collection;
import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;
import edu.usd.portlet.cmscontent.dao.CMSDocument;

/**
 * This is the interface definition for external CMS content. Any CMS content
 * providers must provide a method that retrieves document content and lists
 * available document.
 * @author Toben Archer
 * @version $Id$
 */

public interface CMSDocumentDao
{

	public List<String> getAvailableDocuments();
	// Get a list of available documents, returning just the doc's title.

	public List<CMSDocument> getAllDocumentsContentless();
	// Get the a list of available documents. It returns a list of all the
	// documents but with out the content associted with them. So it is a
	// list of titles, Ids, and the source. This can be used for when a list
	// of all available documents is necessary but one does not want the
	// entire database of documents. 

	public CMSDocument getDocument(String Id);
	// Get the requested document. The Id is some unique identifier for the
	// the data source. This will usually be a path of some sort, but it
	// need not be.
	
	public String getDaoName();
	// return the name of this DAO for choosing it in controllers.
	
	public void saveDocument(CMSDocument val);
	
	public boolean saveEnabled();
	//return whether or not this Dao has the ability to save or if it is a
	//read only Dao.
	
	public boolean deleteDocument(String Id);
	//delete's a document if such can be done.
	
	public boolean deleteEnabled();
	//returns whether or not this Dao has the ability to delete documents from
	//it's source. 
}
