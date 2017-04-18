package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.usd.portlet.cmscontent.dao.CMSConfigDao;
import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

/**
 * @author Toben Archer
 * @version $Id$
 */

public class PropertiesDaoImpl implements CMSConfigDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public List<CMSPageInfo> getPageUrisSecure(PortletRequest request)
	{
		return new ArrayList<CMSPageInfo>();
	}

	// Return a list of page Uri's and the data source they came from. This is 
	// meant to be used in the editor rather than for displaying.
	// ret.get(page uri) -> data source.
	public List<CMSPageInfo> getPageUris(PortletRequest request)
	{
//		logger.debug("Getting them uris!");
		PortletPreferences prefs = request.getPreferences();
//		logger.debug("Prefs got, getting values");
		String[] pageUriArray = prefs.getValues("pageUri",null);
//		logger.debug("Vals got");
		String source;
		ArrayList<CMSPageInfo> ret = new ArrayList<CMSPageInfo>();
		try
		{
			for(String val:pageUriArray)
			{
				if(val == null)
				{
					//ret.put("blank","blank");
					ret.add(new CMSPageInfo("","blank","blank"));
					continue;
				}
//				logger.debug("The value is: '" + val + "'");
				source = prefs.getValue(val,null);
				ret.add(new CMSPageInfo("",val,source));
			}
		}
		catch(Exception e)
		{
			logger.info("There were no values set");
			ret.add(new CMSPageInfo("","blank","blank"));
		}
//		logger.debug("Ready to return");
		return ret;
	}

	public void addPage(PortletRequest request)
	{
		logger.info("attempting to add a page");
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		//get the current list of channel paths.
		String[] pageUriArray = prefs.getValues("pageUri",null);

		//create an array list to remove nulls and insert new items
		ArrayList<String> pageUris = new ArrayList<String>();
		logger.info("pageUriArray contents:");
		for(String val:pageUriArray)
		{
			//logger.info(val);
			if (val == null)
				logger.debug("Skipping null value");
			else
			{
				logger.debug("Adding value: " + val);
				pageUris.add(val);
			}
		}
		//add an item to that list.
		pageUris.add("blank");

		//convert back and save it as the new list of channels. Putting it in an
		//array that is no bigger than necessary to avoid excess null values.
		String[] insertArray = new String[pageUris.size()];
		try
		{
			//clear the previous values
			prefs.reset("pageUri");
			prefs.store();
			//add the new list back in.
			prefs.setValues("pageUri",pageUris.toArray(insertArray));
			//Save.
			prefs.store();
		}
		catch(ReadOnlyException e)
		{
			logger.debug("Error trying to set values, Javax Read Only Exception: " + e);
		}
		catch(IOException e)
		{
			logger.debug("Error trying to save new values. Java IOException: " + e);
		}
		catch(ValidatorException e)
		{
			logger.debug("Error trying to save new values. Javax Validator Exception: " + e);
		}
	}
	// Creates a new place holder page which will later be updated.

	public void updatePage(PortletRequest request, int index, String uri, String source)
	{

	}
	// Update an index in the Uri list. 4 arguments are needed for this. First
	// of course is the PortletRequest. Next an int, which is the index of the
	// page to be updated. Finally, two strings, the Uri, and which source it 
	// came from.

	public void removePage(PortletRequest request, int index)
	// Remove the page of the specified index.
	{

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
