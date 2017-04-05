package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


import org.hibernate.Criteria;
//import org.hibernate.HibernateException;
//import org.hibernate.Query;
import org.hibernate.Session;
//import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.*;

import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

public class InternalDaoImpl implements CMSDataDao, DisposableBean
{

	public ArrayList<CMSPageContent> getContent(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		String content = "", title = "";
		CMSPageContent page;
		ArrayList<CMSPageContent> ret = new ArrayList<CMSPageContent>();

		String pageUri = preferences.getValue("pageUri","/404ErrorPage");

		try
		{
			
		}
		catch(Exception e)
		{
//			content = "There was a problem retrieving the requested content. " + e.getMessage();
		}
		finally
		{

		}
		return ret;
	}

	public ArrayList<CMSPageInfo> getAvailablePages()
	{
		List<InternalPageStore> raw_pages = new ArrayList<InternalPageStore>();
		ArrayList<CMSPageInfo> pages = new ArrayList<CMSPageInfo>();


		try
		{
//			final Session session = this.getSession(false);
//			Criteria crit = session.createCriteria(InternalPageStore.class);
//			crit.addOrder(Order.desc("id"));
//			raw_pages = crit.list();
		}
		catch(Exception e)
		{
			
		}
		finally
		{

		}

		
		return pages;
	}

	public Collection<String> getAvailableGroups()
	{
		return new ArrayList<String>();
	}

	public void destroy() throws Exception {
	}
}
