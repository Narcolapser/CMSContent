package edu.usd.portlet.cmscontent.dao;

/**
 * @author Toben Archer
 * @version $Id$
 */

import java.util.*;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Session;

import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;


import edu.usd.portlet.cmscontent.dao.CMSDocument;

public class InternalDocumentStore
{
	private Log logger = LogFactory.getLog(getClass());


	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

//	@Override
	public void storeDocument(CMSDocument doc)
	{
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(doc);
		logger.info("Persisted document");
	}
	
//	@Override
	public CMSDocument getDocument(String path)
	{
		Session session = this.sessionFactory.getCurrentSession();
		CMSDocument ret = (CMSDocument) session.load(CMSDocument.class, path);
		logger.info("Document loaded sucessfully, details: " + ret);
		return ret;
	}
	
	public List<CMSDocument> getAll()
	{
		logger.info("Getting all documents " + this.sessionFactory);
		Session session = this.sessionFactory.getCurrentSession();
		List<CMSDocument> docList = session.createQuery("from CMSDocument").list();
		return docList;
	}

//	public ArrayList<CMSDocumentInfo> getPages()
//	{

//	}

}
