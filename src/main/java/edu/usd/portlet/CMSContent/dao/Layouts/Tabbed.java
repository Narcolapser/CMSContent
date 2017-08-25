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
public class Tabbed extends CMSLayout
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public Tabbed(){}
	public String getName(){return "Tabbed";}
	public String getView(){return "view_tabbed";}
	public String getDescription()
	{
		return "A simple tabbed layout.";
	}

	public CMSLayout copy(CMSLayout val)
	{
		logger.info("Tabbed is copying");
		CMSLayout l = new CMSLayout(val);
		l.view = "view_tabbed";
		l.name = "Tabbed";
		//logger.info("mid-copy: layout: " + l.getName() + ";" + l.getView());
		return l;
	}
	
	public Map<String,String> getDefaultProperties()
	{
		Map<String,String> ret = new HashMap<String,String>();
		ret.put("Link buttons (True/False)","False");
		return ret;
	}
	
	public Map<String,String> getProperties()
	{
		//logger.debug("Tabs properties!");
		Map<String,String> ret = this.getDefaultProperties();
		for(String val:ret.keySet())
		{
			//logger.debug("Key: " + val);
			if(!this.properties.containsKey(val))
				this.properties.put(val,ret.get(val));
		}
		return this.properties;
	}
}
