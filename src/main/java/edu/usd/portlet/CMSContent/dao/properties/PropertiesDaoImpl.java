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
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;
import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.usd.portlet.cmscontent.dao.CMSConfigDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSLayout;
import edu.usd.portlet.cmscontent.dao.CMSSubscription;
import edu.usd.portlet.cmscontent.dao.CMSId_map;

/**
 * This is an implementation of the CMSConfigDao which leverages the portal's
 * built in properties system. You could theoretically make one that does it
 * entirely in hibernate or some such way, but lacking anyway to determing what
 * the portlet is uniquely it would be very hard.
 * 
 * @author Toben Archer
 * @version $Id$
 */

public class PropertiesDaoImpl implements CMSConfigDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	List<CMSLayout> layouts;
	
	//the default getLayout method. It will attempt to return, context aware, the current layout.
	public CMSLayout getLayout(PortletRequest request)
	{
		return getLayout(request,"normal");
	}
	
	public CMSLayout getLayout(PortletRequest request, String mode)
	{
		PortletPreferences prefs = request.getPreferences();
		//create a new CMSLayout object which will be returned.
		CMSLayout ret = new CMSLayout();
		
		//Get the view for the current mode, if there isn't one, assume single.
		String view = prefs.getValue(mode,"layouts/single");
		ret.setView(view);
		
		//Iterate over the available layouts and grab the approparite class.
		for(CMSLayout layout:layouts)
		{
			//logger.debug("Comparing " + layout.getView() + " to " + view);
			if (layout.getView().equals(view))
			{
				//make a copy of that class so that we can get the benefits of polymorphism.
				ret = layout.copy(layout);
			}
		}
		
		//Get this layout's subscriptions.
		String[] subs = prefs.getValues(mode+".subscriptions",new String[0]);
		logger.debug("found subs: " + subs.length + " for " + mode+".subscriptions");

		//Iterate over the array of strings and form them neatly into CMSSubscription objects.
		List<CMSSubscription> subscriptions = new ArrayList<CMSSubscription>();
		for(String sub:subs)
		{
			int delimiter = sub.indexOf(";");
			String source = sub.substring(0,delimiter);
			String id = sub.substring(delimiter+1);
			//This will be enough to allow me to do the transition. This will load the documents
			//with the new id and the portlets will gradually be saved over to the new id.
			if (CMSId_map.id_map_old.containsKey(id))
				id = CMSId_map.id_map_old.get(id);
			CMSSubscription csub = new CMSSubscription();
			csub.setDocId(id);
			csub.setDocSource(source);
			
			//Get the security groups for this subscription. 
			String[] groups = prefs.getValues(mode+".subscriptions."+sub,new String[0]);
			csub.setSecurityGroups(Arrays.asList(groups));
			
			subscriptions.add(csub);
			logger.info(csub);
		}
		logger.debug("Total subscriptions: " + subscriptions.size());
		ret.setSubscriptions(subscriptions);
		
		//Get any properties set for this layout.
		String[] props = prefs.getValues(mode+".properties",new String[0]);
		for(String prop:props)
		{
			try
			{
				int delimiter = prop.indexOf(";");
				String property = prop.substring(0,delimiter);
				String value = prop.substring(delimiter+1);
				ret.setProperty(property,value);
			}
			catch (Exception e)
			{
				//logger.debug("Error while trying to load property: " + prop + " Error: " + e);
			}
		}

		return ret;
	}
	
	public void setLayout(PortletRequest request, String mode, CMSLayout layout)
	{
		PortletPreferences prefs = request.getPreferences();
		logger.debug("setting layout. to: " + layout.getView());
		try
		{
			//Get the list of preference names.
			Enumeration<String> pks = prefs.getNames();
			List<String> prefkeys = new ArrayList<String>();
			
			//Iterate over that list to find the ones that are related this mode's view.
			while(pks.hasMoreElements())
			{
				String pref = pks.nextElement();
				if (pref.contains(mode))
					prefkeys.add(pref);
			}
			
			//Reset, e.g. delete, those preferences so we don't have any loose ends.
			for(String pk : prefkeys)
				prefs.reset(pk);

			//Set the mode's view to the layout's view. 
			logger.debug("Setting " + mode + " to " + layout.getView());
			prefs.setValue(mode,layout.getView());
			
			//Iterate through the layout's subscriptions and add each one as a preference.
			ArrayList<String> subs = new ArrayList<String>();
			for(CMSSubscription cmssub:layout.getSubscriptions())
			{
				//Delimit between document source and document id with a semicolon.
				String sub = cmssub.getDocSource();
				sub += ";";
				sub += cmssub.getDocId();
				subs.add(sub);
				
				//If the subscription has security groups, add them in.
				if(cmssub.getSecurityGroups() != null)
				{
					String[] insertArray = new String[cmssub.getSecurityGroups().size()];
					prefs.setValues(mode+".subscriptions."+sub, cmssub.getSecurityGroups().toArray(insertArray));
				}
			}
			//Add all the subscriptions into the mode's subscriptions preference.
			String[] insertArray = new String[subs.size()];
			prefs.setValues(mode+".subscriptions",subs.toArray(insertArray));
			
			//If the view has properties, save those as well. 
			ArrayList<String> props = new ArrayList<String>();
			Set<String> keys = layout.getProperties().keySet();
			for(String key:keys)
			{
				try
				{
					String prop = key+";";
					prop += layout.getProperty(key);
					props.add(prop);
				}
				catch (Exception e)
				{
					logger.error("Error saving layout properties: " + e);
				}
			}
			String[] propArray = new String[subs.size()];
			prefs.setValues(mode+".properties",props.toArray(propArray));
			
			//save changes.
			prefs.store();
			logger.debug("layout reset.");
		}
		catch(Exception e)
		{
			logger.error("Failure in setting layout: " + e);
		}
	}

	public void destroy() throws Exception {
	}
	
}
