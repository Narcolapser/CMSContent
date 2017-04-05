package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


import org.hibernate.Criteria;
//import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
//import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.*;

import edu.usd.portlet.cmscontent.dao.CMSPageInfo;
import edu.usd.portlet.cmscontent.dao.InternalPageStore;

public class InternalDaoImpl implements CMSDataDao, DisposableBean
{

	public ArrayList<CMSPageContent> getContent(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		String[] pageUriArray = preferences.getValues("pageUri",null);
		String content = "", title = "";

		CMSPageContent page;
		ArrayList<CMSPageContent> ret = new ArrayList<CMSPageContent>();

		for(String uri:pageUriArray)
		{
			try
			{
				
			}
			catch(Exception e)
			{

			}
			finally
			{

			}
		}
		return ret;
	}

	public ArrayList<CMSPageInfo> getAvailablePages()
	{
		ArrayList<CMSPageInfo> pages;// = new ArrayList<CMSPageInfo>();
		InternalPageStore hb = new InternalPageStore();
		pages = hb.getPages();
		try
		{
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
