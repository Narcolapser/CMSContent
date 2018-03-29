package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.portlet.ModelAndView;

@Component
public class Database implements CMSResponder
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	//get the name of the responder.
	public String getName(){return "Database";}
	
	public String getOptionInfo(){return "Connection String:";}
	
	//respond to the form returned to the system.
	public boolean respond(String json, String options)
	{
		logger.debug("Processing response: " + json);
		return true;
	}
}
