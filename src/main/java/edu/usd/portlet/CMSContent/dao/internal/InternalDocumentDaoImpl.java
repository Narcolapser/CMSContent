package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.usd.portlet.cmscontent.dao.CMSDocument;

/**
 * Hibernate class definition. This class handles the actual interactions with
 * hibernate to allow for database agnostic storage of internal documents. 
 *
 * @author Toben Archer
 * @version $Id$
 */

@Repository
@Transactional(readOnly = true)
public class InternalDocumentDaoImpl implements InternalDocumentDao
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<CMSDocument> getAllDocumentsContentLess(String type)
	{
		return getAllContentLess(type);
	}

	//Fetching part of the document using hibernate is a bit tricky.
	@SuppressWarnings("unchecked")
	private List<CMSDocument> getAllContentLess(String docType)
	{
		logger.debug("Getting documents with out content");
		
		//First create and execute a query that selects only the interesting columns.
		Session session = sessionFactory.openSession();
		String hql = "SELECT id, title, keyTerms, docType, source FROM CMSDocument WHERE removed = 0 and docType = '"+docType+"'";
		Query query = session.createQuery(hql);
		
		//Next prepare the datastructures we will need to deal with the results.
		List<CMSDocument> docList = new ArrayList<CMSDocument>();
		List<Object[]> list = query.list();
		
		//Then iterate over the list of results.
		for(Object[] obj: list)
		{
			//Extract the interesting columns' information and set those fields on the document.
			CMSDocument doc = new CMSDocument();
			doc.setId((String)obj[0]);
			doc.setTitle((String)obj[1]);
			doc.setKeyTerms((String)obj[2]);
			doc.setDocType((String)obj[3]);
			doc.setSource((String)obj[4]);
			
			//Finally set the other fields to defaults so that we have no null value issues. Since
			//our where clause exclused removed documents, we can assume the value of removed to be
			//false. Lastly, the whole point is that content is an empty string.
			doc.setRemoved(false);
			doc.setContent("");
			
			//Finally save the document in the list to be returned. 
			docList.add(doc);
		}
		return docList;
	}
	
	@SuppressWarnings("unchecked")
	public CMSDocument getDocumentById(String id)
	{
		Session session = sessionFactory.openSession();
		String hql = "FROM CMSDocument WHERE id = '" + id + "' and removed = 0";
		Query query = session.createQuery(hql);
		return (CMSDocument)query.uniqueResult();
	}
	
	@Transactional(readOnly = false)
	public void insertDocument(CMSDocument doc)
	{
		Session session = sessionFactory.openSession();
		session.saveOrUpdate(doc);
		session.flush();
	}
	
	@Transactional(readOnly = false)
	public void deleteDocument(String id)
	{
		Session session = this.sessionFactory.getCurrentSession();
		CMSDocument ret = (CMSDocument) session.load(CMSDocument.class, id);
		
		//Nothing is ever deleted. We only toggle it's removed flag.
		ret.setRemoved(true);
		session.saveOrUpdate(ret);
		session.flush();
	}
}
