package edu.usd.portlet.cmscontent.dao;

import java.util.Collection;
import java.util.ArrayList;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;
import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

/**
 * This is the interface definition for external CMS content. Any CMS content
 * providers must provide a method that retrieves content, lists available
 * pages, lists available groups, and whether or not security is internally 
 * managed or externally managed. 
 * @author Toben Archer
 * @version $Id$
 */

public interface CMSDataDao
{

	public ArrayList<String> getContent(PortletRequest request);
	// This method's purpose is to take the portlet page request and return the
	// HTML code that is to be displayed. If security is managed internally you
	// must handle the security in this method. After you return the string for
	// this request's content, no more security checks will occur.

	public ArrayList<CMSPageInfo> getAvailablePages();
	// List the available pages. This is primarily used for the configuring of
	// the portlets. When the person responsible chooses what content is to be
	// displayed, a list populated by the content returned by this method will 
	// display to them the available options.

	public Collection<String> getAvailableGroups();
	// Like the available pages method, this method gets the list of groups that
	// are available. This is only used when security is managed internally. 
	// When security is managed internally, during the configuration for a
	// portlet, this method will provide the groups that the configurer will
	// mark as being able to see or not.

}
