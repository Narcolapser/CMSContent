package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import edu.usd.portlet.cmscontent.dao.CMSSubscription;
import edu.usd.portlet.cmscontent.dao.CMSLayout;
import org.springframework.web.portlet.ModelAndView;

@Component
public class VerticalTabs extends CMSLayout
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public VerticalTabs(){}
	public String getName(){return "Vertical Tabs";}
	public String getView(){return "view_vertical_tabs";}
	public String getDescription()
	{
		return "Tabs, but vertically along the left side. Also included on the tabs is a button to copy a direct link to that portlet fullscreened with that tab open. \n\nThere is one property: Tab Width. This specifies how wide the column of tabs should be. Useful for documents with really long titles.";
	}

	public CMSLayout copy(CMSLayout val)
	{
		CMSLayout l = new CMSLayout();
		l.view = "view_vertical_tabs";
		l.name = "Vertical Tabs";
		l.properties = getDefaultProperties();
		return l;
	}
	
	public Map<String,String> getDefaultProperties()
	{
		Map<String,String> ret = new HashMap<String,String>();
		ret.put("Link buttons (True/False)","False");
		ret.put("Tab column width (in pixels)","400");
		return ret;
	}
	
	public Map<String,String> getProperties()
	{
		//logger.debug("Vertical tabs properties!");
		Map<String,String> ret = this.getDefaultProperties();
		for(String val:ret.keySet())
		{
			//logger.debug("Key: " + val + " Value: " + ret.get(val));
			if(!this.properties.containsKey(val))
				this.properties.put(val,ret.get(val));
		}
		return this.properties;
	}
	
	public void setProperties(Map<String,String> val)
	{
		this.properties = val;
	}
}
