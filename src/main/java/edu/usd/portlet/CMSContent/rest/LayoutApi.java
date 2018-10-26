package edu.usd.portlet.cmscontent.rest;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Thread;
import java.lang.Runnable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import edu.usd.portlet.cmscontent.dao.FormDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSLayout;
/**
 * This class provides a REST API for the CMS Forms. There are two functions
 * that are exposed to allow the CMS Forms to function correctly:
 *   response: Responds to a form submission.
 *   listResponders: Returns the available responders. For editor usage.
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */
@RestController
@RequestMapping("/v2/layout")
public final class LayoutApi {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	List<CMSDocumentDao> dataSources;
	
	@Autowired
	List<CMSLayout> layouts;

	public CMSDocumentDao getDbo(String name)
	{
		CMSDocumentDao dbo = dataSources.get(0);
		for(CMSDocumentDao ds:dataSources)
			if (ds.getDaoName().equals(name))
				dbo = ds;
		return dbo;
	}

	@RequestMapping("list")
	public String list()
	{
		String ret = "{";
		for(CMSLayout l:layouts)
			ret += "\""+l.getView()+"\":\""+l.getName()+"\",";
		
		//remove the trailing comma and return with the closing bracket.
		return ret.substring(0,ret.length()-1) + "}";
	}

}

