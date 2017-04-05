package edu.usd.portlet.cmscontent.dao;

/**
 * @author Toben Archer
 * @version $Id$
 */

import java.util.*;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.hibernate.Criteria;
//import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
//import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;


import edu.usd.portlet.cmscontent.dao.CMSPage;

public class InternalPageStore extends HibernateDaoSupport
{
	private Log log = LogFactory.getLog(getClass());

//	@Override
	public void storePage(CMSPage Page)
	{

	}
	
//	@Override
	public CMSPage getPage(String path)
	{
		return new CMSPage();
	}

	public ArrayList<CMSPageInfo> getPages()
	{
		final Session session = this.getSession(false);
		ArrayList<CMSPageInfo> pages = new ArrayList<CMSPageInfo>();
		Query query = session.createQuery("select title,path from CMSPageStore");
		for(final Object o : query.list())
		{
			pages.add((CMSPageInfo)o);
		}
		return pages;
	}

}
