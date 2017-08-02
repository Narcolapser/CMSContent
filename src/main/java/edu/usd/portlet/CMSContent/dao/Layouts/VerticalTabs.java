package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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

	public CMSLayout copy(CMSLayout val)
	{
		CMSLayout l = new CMSLayout(val);
		l.view = "view_vertical_tabs";
		l.name = "Vertical Tabs";
		logger.info("mid-copy: layout: " + l.getName() + ";" + l.getView());
		return l;
	}
}
