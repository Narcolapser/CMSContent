package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSLayout;

/**
 * This is the interface definition for portlet configuration. This will contain
 * the information such as what type of display the portlet uses, what the pages
 * are in the display, from what data source the page comes from,  etc. It will 
 * also store information unique to the display, such as tab width. 
 * @author Toben Archer
 * @version $Id$
 */

public interface CMSConfigDao
{

	public CMSLayout getLayout(PortletRequest request);
	//requests the default layout for the current portlet mode.
	
	public CMSLayout getLayout(PortletRequest request, String mode);
	//requests the layout specified in the argument mode.
	
	public void setLayout(PortletRequest request, String mode, CMSLayout layout);
	//sets the layout for the requested type.

}
