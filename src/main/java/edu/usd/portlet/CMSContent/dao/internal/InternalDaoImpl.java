package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.*;

import edu.usd.portlet.cmscontent.dao.CMSPageInfo;
import edu.usd.portlet.cmscontent.dao.InternalPageStore;
import edu.usd.portlet.cmscontent.dao.InternalPageInfoRepository;

public class InternalDaoImpl implements CMSDataDao, DisposableBean
{

	@Autowired
	private InternalPageInfoRepository pageInfoRepo;
//	private InternalPageInfoRepository pageInfoRepo = new InternalPageInfoRepository();

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
		ArrayList<CMSPageInfo> pages = new ArrayList<CMSPageInfo>();
//		InternalPageStore hb = new InternalPageStore();
//		pages = hb.getPages();
		pages.add(new CMSPageInfo("Test page","/path/to/test.html"));
		pages.add(new CMSPageInfo("tp2","/path/to/test2.html"));
		
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
