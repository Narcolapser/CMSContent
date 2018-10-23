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
import java.util.Comparator;

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
 * This is an implementation of the CMSConfigDao which leverages the portal's
 * built in properties system. You could theoretically make one that does it
 * entirely in hibernate or some such way, but lacking anyway to determing what
 * the portlet is uniquely it would be very hard.
 * 
 * @author Toben Archer
 * @version $Id$
 */

public class DBConfigImpl implements CMSConfigDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	List<CMSLayout> layouts;
	
	@Autowired
	PropertiesDaoImpl proDao;
	
	@Autowired
	DBConfigRepo repo;
	
	//the default getLayout method. It will attempt to return, context aware, the current layout.
	public CMSLayout getLayout(PortletRequest request)
	{
	//create a new CMSLayout object which will be returned.
		CMSLayout ret = new CMSLayout();
		
		logger.debug("Attempting to getting layout from database");
		PortletPreferences prefs = request.getPreferences();
		String fname = prefs.getValue("fname","");
		if (!fname.equals(""))
		{
			logger.debug("Portlet has fname was set, getting fname: " + fname);
			ret = repo.getCMSLayoutFname(fname);
			logger.debug(ret);
			if (ret == null)
			{
				logger.debug("No instance of this fname found. Defaulting to properties.");
				ret = proDao.getLayout(request);
				ret.fname = fname;
				logger.debug(ret.getSubscriptions());
				repo.insertLayout(ret);
				logger.debug("Layout inserted");
			}
			else
			{
				logger.debug("Layout found for this fname:");
				logger.debug(ret);
			}
		}
		else
		{
			logger.debug("No database entry for this layout exists, defaulting to properties.");
			ret = proDao.getLayout(request);
		}
		List<CMSSubscription> subs = ret.getSubscriptions();
		Collections.sort(subs, new SortbyOrder());
		ret.setSubscriptions(subs);
		return ret;
	}
	
	public CMSLayout getLayout(PortletRequest request, String mode)
	{
		//Window state as an option is being depricated. Use different portlets for different views.
		return this.getLayout(request);
	}
	
	public void setLayout(PortletRequest request, String mode, CMSLayout layout)
	{
		PortletPreferences prefs = request.getPreferences();
		logger.debug("setting layout. to: " + layout.getView());
		repo.insertLayout(layout);
	}

	public void destroy() throws Exception {
	}
	
	public class SortbyOrder implements Comparator<CMSSubscription>
	{
		public int compare(CMSSubscription left, CMSSubscription right)
		{
			return left.order-right.order;
		}
	}
}
