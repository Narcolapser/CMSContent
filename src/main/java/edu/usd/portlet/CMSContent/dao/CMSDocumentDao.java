package edu.usd.portlet.cmscontent.dao;

import java.util.Collection;
import java.util.ArrayList;

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

	public ArrayList<String> getAvailableDocuments();
	// Get a list of available documents, returning just the doc's title.

	public ArrayList<CMSDocument> getAllDocumentsContentless();
	// Get the a list of available documents. It returns a list of all the
	// documents but with out the content associted with them. So it is a
	// list of titles, Ids, and the source. This can be used for when a list
	// of all available documents is necessary but one does not want the
	// entire database of documents. 

	public CMSDocument getDocument(String Id);
	// Get the requested document. The Id is some unique identifier for the
	// the data source. This will usually be a path of some sort, but it
	// need not be.
}
