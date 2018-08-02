package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSLayout;

/**
 * This interface is for the reponders to forms.
 * @author Toben Archer
 * @version $Id$
 */

public interface CMSResponder
{
	//get the name of the responder.
	public String getName();
	
	//if the responder uses options, describe what they are briefly.
	public String getOptionInfo();
	
	//respond to the form returned to the system.
	public boolean respond(String json, String options);
	
	//auto responding responders get run regardless of whether they are in the
	//forms configuration or not. These responders run and finish before the
	//user gets confirmation. So only make auto responders that respond fast.
	//And there is no way to provide per-form configuration, so only use an auto
	//responder if you don't need per-form options. Really, you should just not
	//use this and let the database responder be the only one.
	public boolean autoRespond();
}
