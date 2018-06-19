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
public class Multiple extends CMSLayout
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public Multiple(){}
	public String getName(){return "Multiple";}
	public String getView(){return "layouts/multiple";}
	public String getDescription()
	{
		return "For displaying multiple documents one after another. Like the \"single\" layout, this does no extra formating.";
	}

	public CMSLayout copy(CMSLayout val)
	{
		logger.debug("Multiple is copying");
		CMSLayout l = new CMSLayout(val);
		l.view = "layouts/multiple";
		l.name = "Multiple";
		return l;
	}
}
