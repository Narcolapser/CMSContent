package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import edu.usd.portlet.cmscontent.dao.CMSConfigDao;

/**
 * @author Toben Archer
 * @version $Id$
 */

public class PropertiesDaoImpl implements CMSConfigDao, DisposableBean
{
	public Map<String,String> getPageUrisSecure(PortletRequest request)
	{
		return new HashMap<String,String>();
	}

	// Return a list of page Uri's and the data source they came from. This is 
	// meant to be used in the editor rather than for displaying.
	// ret.get(page uri) -> data source.
	public Map<String,String> getPageUris(PortletRequest request)
	{
		PortletPreferences prefs = request.getPreferences();
		String[] pageUriArray = prefs.getValues("pageUri",null);
		String source;
		Map<String,String> ret = new HashMap<String,String>();
		for(String val:pageUriArray)
		{
			source = prefs.getValue(val,null);
			ret.put(val,source);
		}
		return ret;
	}
	public String getDisplayType(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		String displayType = preferences.getValue("displayType","Single");
		return displayType;
	}
	public Map<String,String> getDisplayAttributes(PortletRequest request)
	{
		PortletPreferences prefs = request.getPreferences();
		String[] atts = prefs.getValues("displayAttributes",null);
		String[] parts;
		Map<String,String> ret = new HashMap<String,String>();
		for(String val:atts)
		{
			parts = val.split(":");
			ret.put(parts[0],parts[1]);
		}
		return ret;
	}

	public void destroy() throws Exception {
	}
}
