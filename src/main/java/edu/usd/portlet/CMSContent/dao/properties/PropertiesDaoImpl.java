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

/**
 * @author Toben Archer
 * @version $Id$
 */

public class PropertiesDaoImpl implements CMSConfigDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	List<CMSLayout> layouts;
	
	public CMSLayout getLayout(PortletRequest request)
	{
		WindowState state = request.getWindowState();
		if (WindowState.MAXIMIZED.equals(state))
		{
			CMSLayout max = getLayout(request,"maximized");
			if (max.getSubscriptionsAsDocs().size()!=0)
				return max;
		}
		return getLayout(request,"normal");
	}
	
	public CMSLayout getLayout(PortletRequest request, String mode)
	{
	
		PortletPreferences prefs = request.getPreferences();
		if ((prefs.getValue("pageUri",null) != null))
			return getLayoutLegacy(request);
		
		CMSLayout ret = new CMSLayout();
		
		String view = prefs.getValue(mode,"layouts/single");
		for(CMSLayout layout:layouts)
		{
			logger.debug("Comparing " + layout.getView() + " to " + view);
			if (layout.getView().equals(view))
			{
				ret = layout.copy(layout);
			}
		}
		
		String[] subs = prefs.getValues(mode+".subscriptions",new String[0]);
		logger.debug("found subs: " + subs.length + " for " + mode+".subscriptions");
		List<CMSSubscription> subscriptions = new ArrayList<CMSSubscription>();
		for(String sub:subs)
		{
			int delimiter = sub.indexOf(";");
			String source = sub.substring(0,delimiter);
			String id = sub.substring(delimiter+1);
			CMSSubscription csub = new CMSSubscription();
			csub.setDocId(id);
			csub.setDocSource(source);
			
			String[] groups = prefs.getValues(mode+".subscriptions."+sub,new String[0]);
			csub.setSecurityGroups(Arrays.asList(groups));
			
			subscriptions.add(csub);
		}
		logger.debug("Total subscriptions: " + subscriptions.size());
		
		String[] props = prefs.getValues(mode+".properties",new String[0]);
		//logger.debug("found props: " + props.length + " for " + mode + ".properties");
		for(String prop:props)
		{
			try
			{
				int delimiter = prop.indexOf(";");
				String property = prop.substring(0,delimiter);
				String value = prop.substring(delimiter+1);
				//logger.debug("Property found, Key: " + property + " Value: " + value);
				ret.setProperty(property,value);
			}
			catch (Exception e)
			{
				//logger.info("Error while trying to load property: " + prop + " Error: " + e);
			}
		}
		
		ret.setView(view);
		ret.setSubscriptions(subscriptions);
		
		return ret;
	}
	
	public void setLayout(PortletRequest request, String mode, CMSLayout layout)
	{
		PortletPreferences prefs = request.getPreferences();
		logger.debug("setting layout. to: " + layout.getView());
		try
		{
			//Legacy:
			prefs.reset("pageUri");
			prefs.store();

			Enumeration<String> pks = prefs.getNames();
			List<String> prefkeys = new ArrayList<String>();
			while(pks.hasMoreElements())
			{
				String pref = pks.nextElement();
				if (pref.contains(mode))
					prefkeys.add(pref);
			}
			for(String pk : prefkeys)
				prefs.reset(pk);
			logger.debug("Setting " + mode + " to " + layout.getView());
			prefs.setValue(mode,layout.getView());
			
			ArrayList<String> subs = new ArrayList<String>();
			for(CMSSubscription cmssub:layout.getSubscriptions())
			{
				
				String sub = cmssub.getDocSource();
				sub += ";";
				sub += cmssub.getDocId();
				subs.add(sub);
				if(cmssub.getSecurityGroups() != null)
				{
					String[] insertArray = new String[cmssub.getSecurityGroups().size()];
					prefs.setValues(mode+".subscriptions."+sub, cmssub.getSecurityGroups().toArray(insertArray));
				}
			}
			String[] insertArray = new String[subs.size()];
			prefs.setValues(mode+".subscriptions",subs.toArray(insertArray));
			
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
				
				}
			}
			String[] propArray = new String[subs.size()];
			prefs.setValues(mode+".properties",props.toArray(propArray));
			prefs.store();
			logger.debug("layout reset.");
		}
		catch(Exception e)
		{
			logger.debug("Failure!" + e);
		}
	}

	private CMSLayout getLayoutLegacy(PortletRequest request)
	{
		logger.debug("Legacy portlet");
		PortletPreferences prefs = request.getPreferences();
		String[] pageUriArray = prefs.getValues("pageUri",null);
		String source;
		List<CMSSubscription> ret = new ArrayList<CMSSubscription>();
		try
		{
			for(String val:pageUriArray)
			{
				if(val == null)
				{
					CMSSubscription csub = new CMSSubscription();
					csub.setDocId("blank");
					csub.setDocSource("blank");
					ret.add(csub);
					continue;
				}
				//default is commonspot for backwards compatibility reasons.
				source = prefs.getValue(val,"CommonSpot");
				CMSSubscription csub = new CMSSubscription();
				csub.setDocId(val);
				csub.setDocSource(source);
				ret.add(csub);
			}
		}
		catch(Exception e)
		{
			logger.debug("There were no values set");
			CMSSubscription csub = new CMSSubscription();
			csub.setDocId("blank");
			csub.setDocSource("blank");
			ret.add(csub);
		}
		
		CMSLayout layout = new CMSLayout();
		
		layout.setView("layouts/single");
		layout.setSubscriptions(ret);
		
		return layout;
	}


	public void destroy() throws Exception {
	}
}
