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
public class Email implements CMSResponder
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	//get the name of the responder.
	public String getName(){return "Email";}
	
	//respond to the form returned to the system.
	public boolean respond(String json)
	{
		logger.debug("Processing response: " + json);
		return true;
	}
}
