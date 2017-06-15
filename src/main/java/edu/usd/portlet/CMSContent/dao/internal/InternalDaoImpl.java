package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.collections.IteratorUtils;

import edu.usd.portlet.cmscontent.dao.CMSPageInfo;
import edu.usd.portlet.cmscontent.dao.InternalPageStore;
import edu.usd.portlet.cmscontent.dao.InternalPageInfoRepository;

public class InternalDaoImpl implements CMSDataDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());

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
		logger.debug("Getting all pages");
		List<CMSPageInfo> pages = new ArrayList<CMSPageInfo>();
//		InternalPageStore hb = new InternalPageStore();
//		pages = hb.getPages();
//		CMSPageInfo page = new CMSPageInfo("Test page","/path/to/test.html");
//		pages.add(page);
//		pageInfoRepo.save(page);
//		page = new CMSPageInfo("tp2","/path/to/test2.html");
//		pages.add(page);
//		pageInfoRepo.save(page);
		try
		{
			Iterable<?> pagesIter = pageInfoRepo.findAll();
			logger.debug("pages fetched.");
			pages = IteratorUtils.toList(pagesIter.iterator());
			logger.debug("Number of pages: " + pages.size());
		}
		catch(Exception e)
		{
			logger.debug("An error was encountered!");
			logger.debug(e);
		}
		finally
		{

		}

		
//		return new ArrayList<CMSPageInfo>(pages);
		return (ArrayList<CMSPageInfo>)pages;
	}
	
	public CMSPageContent getPageContent(String pageUri)
	{
		CMSPageContent page;
		return new CMSPageContent();
	}

	public Collection<String> getAvailableGroups()
	{
		return new ArrayList<String>();
	}

	public void destroy() throws Exception {
	}
}
