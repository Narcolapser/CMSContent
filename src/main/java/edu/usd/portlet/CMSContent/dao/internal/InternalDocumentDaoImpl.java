package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.usd.portlet.cmscontent.dao.CMSDocument;

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
	
	@SuppressWarnings("unchecked")
	public List<CMSDocument> getAllDocuments()
	{
		Session session = sessionFactory.openSession();
		String hql = "FROM CMSDocument WHERE removed = 0 and docType = 'html'";
		Query query = session.createQuery(hql);
		List<CMSDocument> docList = query.list();
		return docList;
	}
	
	public List<CMSDocument> getAllDocumentsContentLess()
	{
		return getAllContentLess("html");
	}
	
	@SuppressWarnings("unchecked")
	public List<CMSDocument> getAllForms()
	{
		Session session = sessionFactory.openSession();
		String hql = "FROM CMSDocument WHERE removed = 0 and docType = 'form'";
		Query query = session.createQuery(hql);
		List<CMSDocument> docList = query.list();
		return docList;
	}
	public List<CMSDocument> getAllFormsContentLess()
	{
		return getAllContentLess("form");
	}
	
	@SuppressWarnings("unchecked")
	public List<CMSDocument> getAllReports()
	{
		Session session = sessionFactory.openSession();
		String hql = "FROM CMSDocument WHERE removed = 0 and docType = 'report'";
		Query query = session.createQuery(hql);
		List<CMSDocument> docList = query.list();
		return docList;
	}
	public List<CMSDocument> getAllReportsContentLess()
	{
		return getAllContentLess("report");
	}
	
	@SuppressWarnings("unchecked")
	private List<CMSDocument> getAllContentLess(String docType)
	{
		logger.debug("Getting documents with out content");
		Session session = sessionFactory.openSession();
		String hql = "SELECT id, title, keyTerms, docType FROM CMSDocument WHERE removed = 0 and docType = '"+docType+"'";
		Query query = session.createQuery(hql);
		
		List<CMSDocument> docList = new ArrayList<CMSDocument>();
		List<Object[]> list = query.list();
		for(Object[] obj: list)
		{
			CMSDocument doc = new CMSDocument();
			doc.setId((String)obj[0]);
			doc.setTitle((String)obj[1]);
			doc.setKeyTerms((String)obj[2]);
			doc.setDocType((String)obj[3]);
			
			doc.setSource("Internal");
			doc.setRemoved(false);
			doc.setContent("");
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
		ret.setRemoved(true);
		session.saveOrUpdate(ret);
		session.flush();
	}
}
