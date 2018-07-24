package edu.usd.portlet.cmscontent.dao;

import java.util.List;

import edu.usd.portlet.cmscontent.dao.CMSDocument;

/**
 * Hibernate interface definition. Provides the description of all the available
 * methods to operate on the CMSDocument table.
 *
 * @author Toben Archer
 * @version $Id$
 */

public interface InternalDocumentDao{
	public List<CMSDocument> getAllDocumentsContentLess(String type);
	public CMSDocument getDocumentById(String id);
	public void insertDocument(CMSDocument doc);
	public void deleteDocument(String id);
}
