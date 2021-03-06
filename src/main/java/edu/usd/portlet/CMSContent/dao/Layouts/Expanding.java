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
public class Expanding extends CMSLayout
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public Expanding(){}
	public String getName(){return "Expanding";}
	public String getView(){return "layouts/expanding";}
	public String getDescription()
	{
		return "An acordian style layout. Features headers that the user can click on to expand and show the content within. ";
	}
	
	public CMSLayout copy(CMSLayout val)
	{
		logger.info("Expanding is copying");
		CMSLayout l = new CMSLayout(val);
		l.view = "layouts/expanding";
		l.name = "Expanding";
		return l;
	}
}
