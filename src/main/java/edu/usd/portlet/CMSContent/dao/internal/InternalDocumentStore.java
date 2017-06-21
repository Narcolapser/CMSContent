package edu.usd.portlet.cmscontent.dao;

/**
 * @author Toben Archer
 * @version $Id$
 */

import java.util.*;

import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;


import edu.usd.portlet.cmscontent.dao.CMSDocument;

public class InternalDocumentStore
{
	private Log log = LogFactory.getLog(getClass());


	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

//	@Override
	public void storePage(CMSDocument Page)
	{

	}
	
//	@Override
	public CMSDocument getPage(String path)
	{
		return new CMSDocument();
	}

//	public ArrayList<CMSDocumentInfo> getPages()
//	{

//	}

}
