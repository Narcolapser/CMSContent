package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

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

	public Map<String,String> getPageUrisSecure(PortletRequest request);
	// Return a list of page Uri's and the data source they came from. This
	// method only returns the page uris the current user has access to see.
	// ret.get(page uri) -> data source.

	public Map<String,String> getPageUris(PortletRequest request);
	// Return a list of page Uri's and the data source they came from. This is 
	// meant to be used in the editor rather than for displaying.
	// ret.get(page uri) -> data source.

	public String getDisplayType(PortletRequest request);
	// return the display type that is selected for this portlet.

	public Map<String,String> getDisplayAttributes(PortletRequest request);
	// return the display information for this portlet. This allows for display
	// types that require extra information like pages that need to be placed
	// in a special location. or attributes like tab width.

}
