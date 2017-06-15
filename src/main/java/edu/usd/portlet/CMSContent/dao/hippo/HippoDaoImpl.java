package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.*;

import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

//public class HippoDaoImpl implements CMSDataDao, DisposableBean
//{

//	public ArrayList<CMSPageContent> getContent(PortletRequest request)
//	{
//		final PortletPreferences preferences = request.getPreferences();
//		String content = "", title = "";
//		CMSPageContent page;
//		ArrayList<CMSPageContent> ret = new ArrayList<CMSPageContent>();

//		String pageUri = preferences.getValue("pageUri","/404ErrorPage");

//		try
//		{

//		}
//		catch(Exception e)
//		{
////			content = "There was a problem retrieving the requested content. " + e.getMessage();
//		}
//		finally
//		{
//		}
//		return ret;
//	}

//	public ArrayList<CMSPageInfo> getAvailablePages()
//	{
//		ArrayList<CMSPageInfo> pages = new ArrayList<CMSPageInfo>();


//		try
//		{
//		}
//		catch(Exception e)
//		{
//			
//		}
//		finally
//		{

//		}

//		return pages;
//	}

//	public Collection<String> getAvailableGroups()
//	{
//		return new ArrayList<String>();
//	}

//	public void destroy() throws Exception {
//	}
//}
