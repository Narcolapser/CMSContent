package edu.usd.portlet.cmscontent.dao;

import java.util.List;
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
		logger.info("Setting session factory");
		this.sessionFactory = sessionFactory;
	}
	
	@SuppressWarnings("unchecked")
	public List<CMSDocument> getAllDocuments()
	{
		logger.debug("All the way down! woohoo!");
		Session session = sessionFactory.openSession();
		String hql = "FROM CMSDocument";
		Query query = session.createQuery(hql);
		List<CMSDocument> docList = query.list();
		return docList;
	}
	
	@Transactional(readOnly = false)
	public void insertDocument(CMSDocument doc)
	{
		Session session = sessionFactory.openSession();
		session.save(doc);
	}
}
