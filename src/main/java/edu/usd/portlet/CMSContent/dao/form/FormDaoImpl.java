package edu.usd.portlet.cmscontent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import edu.usd.portlet.cmscontent.dao.UsdSql;
import edu.usd.portlet.cmscontent.components.SwallowingJspRenderer;

/**
 * This is an implementation of the CMSDocumentDao. It is responsible for pulling in
 * data from our old CMS, CSPortalPage. It is realatively straight forward as all
 * the heavy lifting is done in the database. 
 * 
 * @author Toben Archer
 * @version $Id$
 */

@Component
public class FormDaoImpl implements CMSDocumentDao, DisposableBean
{
	@Autowired
	private SwallowingJspRenderer jspRenderer;

	protected final Log logger = LogFactory.getLog(this.getClass());

	public CMSDocument getDocument(String Id)
	{
		logger.debug("Getting document: 1");
		try
		{
			logger.debug("Getting document: 2");
			Map<String,Object> model = new HashMap<String,Object>();logger.debug("Getting document: 3");
			model.put("content","and this is content!");
			String content = jspRenderer.render("test",model);logger.debug("Getting document: 4");
			CMSDocument val = new CMSDocument("title","Id","Internal Forms","html",content);logger.debug("Getting document: 5");
			return val;
		}
		catch(Exception e)
		{
			logger.debug(e);
			logger.debug("Getting document: 6");
			CMSDocument val = new CMSDocument("title","Id","Internal Forms","html","content");logger.debug("Getting document: 7");
			return val;
		}
	}

	public List<String> getAvailableDocuments()
	{
		ArrayList<String> pages = new ArrayList<String>();
		pages.add("title");
		return pages;
	}

	public List<CMSDocument> getAllDocumentsContentless()
	{
		ArrayList<CMSDocument> pages = new ArrayList<CMSDocument>();
		CMSDocument val = new CMSDocument("title","Id","Internal Forms","html","content");
		pages.add(val);
		return pages;
	}
	
	public void saveDocument(CMSDocument val){}
	
	public String getDaoName()
	{
		return "Internal Forms";
	}

	public void destroy() throws Exception {
	}

	public boolean deleteDocument(String Id){return false;}

	public boolean saveEnabled(){return false;}
	
	public boolean deleteEnabled(){return false;}
}
