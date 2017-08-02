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
	public String getView(){return "view_expanding";}

	public CMSLayout copy(CMSLayout val)
	{
		logger.info("Expanding is copying");
		CMSLayout l = new CMSLayout(val);
		l.view = "view_expanding";
		l.name = "Expanding";
		logger.info("mid-copy: layout: " + l.getName() + ";" + l.getView());
		return l;
	}
}
