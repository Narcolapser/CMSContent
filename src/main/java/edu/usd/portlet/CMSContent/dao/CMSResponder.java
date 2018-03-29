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
}
