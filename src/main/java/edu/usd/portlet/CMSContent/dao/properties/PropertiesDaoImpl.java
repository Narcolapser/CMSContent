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
			return getLayout(request,"maximized");
		return getLayout(request,"normal");
	}
	
	public CMSLayout getLayout(PortletRequest request, String mode)
	{
	
		PortletPreferences prefs = request.getPreferences();
		if ((prefs.getValue("pageUri",null) != null))
//			if (mode.equals("maximized"))
//				return new CMSLayout();
//			else
				return getLayoutLegacy(request);
		
		logger.debug("Not a legacy portlet!");
		
		CMSLayout ret = new CMSLayout();
		
		String view = prefs.getValue(mode,"view_single");
		
		for(CMSLayout layout:layouts)
		{
			logger.debug("Layout option: " + layout.getView() + " looking for: " + view);
			if (layout.getView().equals(view))
			{
				ret = layout.copy(layout);
				logger.debug("Match!: " + ret.getView());
			}
		}
		
		logger.debug("Found view: " + view);
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
			subscriptions.add(csub);
		}
		logger.info("Total subscriptions: " + subscriptions.size());
		
		ret.setView(view);
		ret.setSubscriptions(subscriptions);
		
		return ret;
	}
	
	public void setLayout(PortletRequest request, String mode, CMSLayout layout)
	{
		PortletPreferences prefs = request.getPreferences();
		logger.debug("resetting layout.");
		try
		{
			//Legacy:
			prefs.reset("pageUri");
			prefs.store();
			prefs.reset(mode);
			prefs.reset(mode+".subscriptions");

			prefs.setValue(mode,layout.getView());
			ArrayList<String> subs = new ArrayList<String>();
			for(CMSSubscription cmssub:layout.getSubscriptions())
			{
				String sub = cmssub.getDocSource();
				sub += ";";
				sub += cmssub.getDocId();
				subs.add(sub);
			}

			String[] insertArray = new String[subs.size()];
			prefs.setValues(mode+".subscriptions",subs.toArray(insertArray));

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
		
		layout.setView("view_single");
		layout.setSubscriptions(ret);
		
		return layout;
	}


	public void destroy() throws Exception {
	}
}
