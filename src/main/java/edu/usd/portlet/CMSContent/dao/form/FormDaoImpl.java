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
		try
		{
			String form = "[{\"options\":\"\",\"label\":\"Insert info\",\"type\":\"text\"},{\"options\":\"a,b,c\",\"label\":\"Drop down options\",\"type\":\"select\"},{\"options\":\"\",\"label\":\"true?\",\"type\":\"bool\"},{\"options\":\"1,2,3,17\",\"label\":\"checka boxes!\",\"type\":\"checkbox\"},{\"options\":\"fast,cheap,good\",\"label\":\"only one!\",\"type\":\"radiobutton\"},{\"options\":\"\",\"label\":\"\",\"type\":\"hr\"},{\"options\":\"\",\"label\":\"Label example\",\"type\":\"label\"},{\"options\":\"toben.archer@usd.edu;toben.archer@usd.edu\",\"label\":\"Email\",\"type\":\"respType\"}]";

			FormDoc val =  new FormDoc(new CMSDocument("title","Id","Internal Forms","html",form));
			val.setJspRenderer(jspRenderer);
			return val;
		}
		catch(Exception e)
		{
			logger.debug(e);
			CMSDocument val = new CMSDocument("title","Id","Internal Forms","html","content");
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
