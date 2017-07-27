package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;

import java.io.IOException;
import java.lang.NullPointerException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Collections;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.usd.portlet.cmscontent.dao.CMSConfigDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSLayout;
import edu.usd.portlet.cmscontent.dao.CMSSubscription;

/**
 * @author Toben Archer
 * @version $Id$
 */

public class PropertiesDaoImpl implements CMSConfigDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	public CMSLayout getLayout(PortletRequest request)
	{
		WindowState state = request.getWindowState();
//		logger.debug("Window state: " + state.toString() + " max test: " + (WindowState.MAXIMIZED.equals(state) && !maxDisplayType.equals("None")));
		if (WindowState.MAXIMIZED.equals(state))
			return getLayout(request,"maximized");
		return getLayout(request,"normal");
	}
	
	public CMSLayout getLayout(PortletRequest request, String mode)
	{
	
		PortletPreferences prefs = request.getPreferences();
		if ((prefs.getValue("pageUri",null) != null))
			return getLayoutLegacy(request);
		
		CMSLayout ret = new CMSLayout();
		
		String view = prefs.getValue(mode,"view_single");
		String[] subs = prefs.getValues(mode+".subscriptions",null);
		List<CMSSubscription> subscriptions = new ArrayList<CMSSubscription>();
		for(String sub:subs)
		{
			int delimiter = sub.indexOf(";");
			String source = sub.substring(0,delimiter);
			String id = sub.substring(delimiter+1);
			CMSSubscription csub = new CMSSubscription();
			csub.setDocId(id);
			csub.setDocSource(source);
			subscriptions.add(csub);
		}
		
		ret.setView(view);
		ret.setSubscriptions(subscriptions);
		
		return ret;
	}

	private CMSLayout getLayoutLegacy(PortletRequest request)
	{
		PortletPreferences prefs = request.getPreferences();
		String[] pageUriArray = prefs.getValues("pageUri",null);
		String source;
		//ArrayList<CMSDocument> ret = new ArrayList<CMSDocument>();
		List<CMSSubscription> ret = new ArrayList<CMSSubscription>();
		try
		{
			for(String val:pageUriArray)
			{
				if(val == null)
				{
					//ret.add(new CMSDocument("","blank","blank","blank"));
					CMSSubscription csub = new CMSSubscription();
					csub.setDocId("blank");
					csub.setDocSource("blank");
					ret.add(csub);
					continue;
				}
				//default is commonspot for backwards compatibility reasons.
				source = prefs.getValue(val,"CommonSpot");
				//ret.add(new CMSDocument("",val,source,""));
				CMSSubscription csub = new CMSSubscription();
				csub.setDocId(val);
				csub.setDocSource(source);
				ret.add(csub);
			}
		}
		catch(Exception e)
		{
			logger.info("There were no values set");
			//ret.add(new CMSDocument("","blank","blank","blank"));
			CMSSubscription csub = new CMSSubscription();
			csub.setDocId("blank");
			csub.setDocSource("blank");
			ret.add(csub);
		}
		
		CMSLayout layout = new CMSLayout();
		
		layout.setView("view_single");
		layout.setSubscriptions(ret);
		
		return layout;
	}

	public void setLayout(PortletRequest request, String mode)
	{
	
	}

	public List<CMSDocument> getPageUrisSecure(PortletRequest request)
	{
		return new ArrayList<CMSDocument>();
	}

	// Return a list of page Uri's and the data source they came from. This is 
	// meant to be used in the editor rather than for displaying.
	// ret.get(page uri) -> data source.
	public List<CMSDocument> getPageUris(PortletRequest request)
	{
		PortletPreferences prefs = request.getPreferences();
		String[] pageUriArray = prefs.getValues("pageUri",null);
		String source;
		ArrayList<CMSDocument> ret = new ArrayList<CMSDocument>();
		try
		{
			for(String val:pageUriArray)
			{
				if(val == null)
				{
					ret.add(new CMSDocument("","blank","blank","blank"));
					continue;
				}
				//default is commonspot for backwards compatibility reasons.
				source = prefs.getValue(val,"CommonSpot");
				ret.add(new CMSDocument("",val,source,""));
			}
		}
		catch(Exception e)
		{
			logger.info("There were no values set");
			ret.add(new CMSDocument("","blank","blank","blank"));
		}
		return ret;
	}

	public void addPage(PortletRequest request)
	{
		logger.info("attempting to add a page");
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		//get the current list of channel paths.
		String[] pageUriArray = prefs.getValues("pageUri",new String[0]);

		//in some instances there can arrive a particularly trouble some point. 
		//the portlet might have errored or something and this will result in
		//pageUri being undefined and returning null. Also can happen if this is
		//a new portlet I believe. In this case, we need to add a page, and then
		//continue on. 

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

	public void updatePage(PortletRequest request, int index, String uri, String source)
	{
		PortletPreferences prefs = request.getPreferences();

		//get the current list of uri paths.
		String[] pageUriArray = prefs.getValues("pageUri",null);

		//in some instances there can arrive a particularly trouble some point. 
		//the portlet might have errored or something and this will result in
		//pageUri being undefined and returning null. Also can happen if this is
		//a new portlet I believe. In this case, we need to add a page, and then
		//continue on. 
		ArrayList<String> pageUris = getPropList(request);
		if (pageUriArray == null)
		{
			addPage(request);
			pageUriArray = prefs.getValues("pageUri",null);
			pageUris = getPropList(request);
		}

//		//create an array list to remove nulls and insert new items
//		ArrayList<String> pageUris = new ArrayList<String>();
//		logger.info("pageUriArray contents:");
//		for(String val:pageUriArray)
//		{
//			//logger.info(val);
//			if (val == null)
//				logger.debug("Skipping null value");
//			else
//			{
//				logger.debug("Adding value: " + val);
//				pageUris.add(val);
//			}
//		}

		//update the pageUri property.
		try
		{
			prefs.reset("pageUri");
			prefs.store();
			//beware the off by 1 errors!
			pageUris.set(index - 1,uri);
			prefs.setValues("pageUri",pageUris.toArray(pageUriArray));
			prefs.store();
		}
		catch(ReadOnlyException e)
		{
			logger.debug("Error trying to clear values, Javax Read Only Exception: " + e);
		}
		catch(IOException e)
		{
			logger.debug("Error trying to clear values. Java IOException: " + e);
		}
		catch(ValidatorException e)
		{
			logger.debug("Error trying to clear values. Javax Validator Exception: " + e);
		}

		logger.debug("pageUri has been updated");


		clearExcessPages(request);
		try
		{
			prefs.reset(uri);
			prefs.setValue(uri,source);
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
		
		logger.error("Done setting value.");
	}

	public void removePage(PortletRequest request, int index)
	// Remove the page of the specified index.
	{
		logger.info("attempting to remove page uri #" + index);
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		ArrayList<String> pageUris = getPropList(request);
		String[] insertArray = new String[pageUris.size()];

		try
		{
			prefs.reset("pageUri");
			prefs.store();
			//beware the off by 1 errors!
			pageUris.remove(index - 1);
			prefs.setValues("pageUri",pageUris.toArray(insertArray));
			prefs.store();
		}
		catch(ReadOnlyException e)
		{
			logger.debug("Error trying to clear values, Javax Read Only Exception: " + e);
		}
		catch(IOException e)
		{
			logger.debug("Error trying to clear values. Java IOException: " + e);
		}
		catch(ValidatorException e)
		{
			logger.debug("Error trying to clear values. Javax Validator Exception: " + e);
		}

		logger.debug("pageUri has been removed, remove it's unique data.");

		clearExcessPages(request);

		logger.debug("Done removing page.");
	}

	private void clearExcessPages(PortletRequest request)
	{
		PortletPreferences prefs = request.getPreferences();
		ArrayList<String> pageUris = getPropList(request);
		if (pageUris == null)
			pageUris = new ArrayList<String>();
		ArrayList<String> pageMaxUris = getMaxPropList(request);
		if (pageMaxUris != null)
			pageUris.addAll(pageMaxUris);
		try
		{
			//remove the excess page references
			Enumeration<String> en_props = prefs.getNames();
			ArrayList<String> props = Collections.list(en_props);
			//while(props.hasMoreElements())
			for(String name:props)
			{
				//String name = props.nextElement();
				if (name.equals("pageUri") || name.equals("displayType") || name.equals("pageUriMaximized") || name.equals("maximizedDisplayType"))
				{
					logger.debug("Skipping " + name);
					continue;
				}
				if (pageUris.indexOf(name) == -1)
				{
					logger.debug("removing " + name);
					prefs.reset(name);
					prefs.store();
				}
			}
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

	private ArrayList<String> getPropList(PortletRequest request)
	{
		PortletPreferences prefs = request.getPreferences();
		//get the current list of channel paths.
		String[] pageUriArray = prefs.getValues("pageUri",null);

		//convert to an array list.
		ArrayList<String> pageUris = new ArrayList<String>();
		logger.info("pageUriArray contents:");
		try
		{
			for(String val:pageUriArray)
			{
				if (val == null)
					logger.debug("Skipping null value");
				else
				{
					logger.debug("Adding value: " + val);
					pageUris.add(val);
				}
			}
		}
		catch (NullPointerException e)
		{
			return null;
		}
		return pageUris;
	}

	public String getDisplayType(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		String displayType = preferences.getValue("displayType","Single");
		return displayType;
	}

	public void setDisplayType(PortletRequest request, String disp_type)
	{
		logger.info("attempting to update diplay type to: " + disp_type);
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		try
		{
			prefs.setValue("displayType",disp_type);
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

		logger.debug("Done updating display type.");
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
	






	public List<CMSDocument> getMaxPageUrisSecure(PortletRequest request)
	{
		return new ArrayList<CMSDocument>();
	}

	public List<CMSDocument> getMaxPageUris(PortletRequest request)
	{
		PortletPreferences prefs = request.getPreferences();
		String[] pageUriArray = prefs.getValues("pageUriMaximized",null);
		String source;
		ArrayList<CMSDocument> ret = new ArrayList<CMSDocument>();
		try
		{
			for(String val:pageUriArray)
			{
				if(val == null)
				{
					ret.add(new CMSDocument("","blank","blank","blank"));
					continue;
				}
				//default is commonspot for backwards compatibility reasons.
				source = prefs.getValue(val,"CommonSpot");
				ret.add(new CMSDocument("",val,source,""));
			}
		}
		catch(Exception e)
		{
			logger.info("There were no values set");
			ret.add(new CMSDocument("","blank","blank","blank"));
		}
		return ret;
	}


	public void addMaxPage(PortletRequest request)
	{
		logger.info("attempting to add a page");
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		//get the current list of channel paths.
		String[] pageUriArray = prefs.getValues("pageUriMaximized",new String[0]);

		//in some instances there can arrive a particularly trouble some point. 
		//the portlet might have errored or something and this will result in
		//pageUri being undefined and returning null. Also can happen if this is
		//a new portlet I believe. In this case, we need to add a page, and then
		//continue on. 

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
			prefs.reset("pageUriMaximized");
			prefs.store();
			//add the new list back in.
			prefs.setValues("pageUriMaximized",pageUris.toArray(insertArray));
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

	public void updateMaxPage(PortletRequest request, int index, String uri, String source)
	{
		PortletPreferences prefs = request.getPreferences();

		//get the current list of uri paths.
		String[] pageUriArray = prefs.getValues("pageUriMaximized",null);

		//in some instances there can arrive a particularly trouble some point. 
		//the portlet might have errored or something and this will result in
		//pageUri being undefined and returning null. Also can happen if this is
		//a new portlet I believe. In this case, we need to add a page, and then
		//continue on. 
		ArrayList<String> pageUris = getMaxPropList(request);
		if (pageUriArray == null)
		{
			addMaxPage(request);
			pageUriArray = prefs.getValues("pageUriMaximized",null);
			pageUris = getMaxPropList(request);
		}

		//update the pageUri property.
		try
		{
			prefs.reset("pageUriMaximized");
			prefs.store();
			//beware the off by 1 errors!
			pageUris.set(index - 1,uri);
			prefs.setValues("pageUriMaximized",pageUris.toArray(pageUriArray));
			prefs.store();
			logger.info("Updated pageUriMaximized with: " + uri);
		}
		catch(ReadOnlyException e)
		{
			logger.debug("Error trying to clear values, Javax Read Only Exception: " + e);
		}
		catch(IOException e)
		{
			logger.debug("Error trying to clear values. Java IOException: " + e);
		}
		catch(ValidatorException e)
		{
			logger.debug("Error trying to clear values. Javax Validator Exception: " + e);
		}

		logger.debug("pageUriMaximized has been updated");


		clearExcessPages(request);
		try
		{
			prefs.reset(uri);
			prefs.setValue(uri,source);
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
		
		logger.error("Done setting value.");
	}

	public void removeMaxPage(PortletRequest request, int index)
	// Remove the page of the specified index from the maximized view
	{
		logger.info("attempting to remove page uri #" + index);
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		ArrayList<String> pageUris = getMaxPropList(request);
		String[] insertArray = new String[pageUris.size()];

		try
		{
			prefs.reset("pageUriMaximized");
			prefs.store();
			//beware the off by 1 errors!
			pageUris.remove(index - 1);
			prefs.setValues("pageUriMaximized",pageUris.toArray(insertArray));
			prefs.store();
		}
		catch(ReadOnlyException e)
		{
			logger.debug("Error trying to clear values, Javax Read Only Exception: " + e);
		}
		catch(IOException e)
		{
			logger.debug("Error trying to clear values. Java IOException: " + e);
		}
		catch(ValidatorException e)
		{
			logger.debug("Error trying to clear values. Javax Validator Exception: " + e);
		}

		logger.debug("pageUri has been removed, remove it's unique data.");

		clearExcessPages(request);

		logger.debug("Done removing page.");
	}

	public String getMaximizedDisplayType(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		String displayType = preferences.getValue("maximizedDisplayType","None");
		return displayType;
	}
	
	public void setMaximizedDisplayType(PortletRequest request, String disp_type)
	{
		logger.info("attempting to update maximized diplay type to: " + disp_type);
		//get the portlets preferences.
		PortletPreferences prefs = request.getPreferences();

		try
		{
			prefs.setValue("maximizedDisplayType",disp_type);
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

		logger.debug("Done updating maximized display type.");
	}

	public Map<String,String> getMaxDisplayAttributes(PortletRequest request)
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
	
	private ArrayList<String> getMaxPropList(PortletRequest request)
	{
		PortletPreferences prefs = request.getPreferences();
		//get the current list of channel paths.
		String[] pageUriArray = prefs.getValues("pageUriMaximized",null);

		//convert to an array list.
		ArrayList<String> pageUris = new ArrayList<String>();
		logger.info("pageUriArray contents:");
		try
		{
			for(String val:pageUriArray)
			{
				if (val == null)
					logger.debug("Skipping null value");
				else
				{
					logger.debug("Adding value: " + val);
					pageUris.add(val);
				}
			}
		}
		catch (NullPointerException e)
		{
			return null;
		}
		return pageUris;
	}

	public void destroy() throws Exception {
	}
}
