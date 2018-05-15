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
public class Single extends CMSLayout
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public Single(){}
	public String getName(){return "Single";}
	public String getView(){return "layouts/single";}
	public String getDescription()
	{
		return "The simplest of layouts. Only the top document listed will be displayed.";
	}

	public CMSLayout copy(CMSLayout val)
	{
		logger.debug("Single is copying");
		CMSLayout l = new CMSLayout(val);
		l.view = "layouts/single";
		l.name = "Single";
		return l;
	}
}
