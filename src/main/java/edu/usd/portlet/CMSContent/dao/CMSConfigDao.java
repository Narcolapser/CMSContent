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

	public List<CMSDocument> getPageUrisSecure(PortletRequest request);
	// Return a list of page Uri's and the data source they came from. This
	// method only returns the page uris the current user has access to see.
	// ret.get(page uri) -> data source.

	public List<CMSDocument> getPageUris(PortletRequest request);
	// Return a list of page Uri's and the data source they came from. This is 
	// meant to be used in the editor rather than for displaying.
	// ret.get(page uri) -> data source.

	public void addPage(PortletRequest request);
	// Creates a new place holder page which will later be updated.

	public void updatePage(PortletRequest request, int index, String uri, String source);
	// Update an index in the Uri list. 4 arguments are needed for this. First
	// of course is the PortletRequest. Next an int, which is the index of the
	// page to be updated. Finally, two strings, the Uri, and which source it 
	// came from.

	public void removePage(PortletRequest request, int index);
	// Remove the page of the specified index.

	public String getDisplayType(PortletRequest request);
	// return the display type that is selected for this portlet.

	public void setDisplayType(PortletRequest request, String disp_type);
	// set the display type that is selected for this portlet.

	public Map<String,String> getDisplayAttributes(PortletRequest request);
	// return the display information for this portlet. This allows for display
	// types that require extra information like pages that need to be placed
	// in a special location. or attributes like tab width.


	//THE MAX FUNCTIONS: Same as above, just for maximized view.
	public List<CMSDocument> getMaxPageUrisSecure(PortletRequest request);
	// Return a list of page Uri's and the data source they came from. This
	// method only returns the page uris the current user has access to see.
	// ret.get(page uri) -> data source.

	public List<CMSDocument> getMaxPageUris(PortletRequest request);
	// Return a list of page Uri's and the data source they came from. This is 
	// meant to be used in the editor rather than for displaying.
	// ret.get(page uri) -> data source.

	public void addMaxPage(PortletRequest request);
	// Creates a new place holder page which will later be updated.

	public void updateMaxPage(PortletRequest request, int index, String uri, String source);
	// Update an index in the Uri list. 4 arguments are needed for this. First
	// of course is the PortletRequest. Next an int, which is the index of the
	// page to be updated. Finally, two strings, the Uri, and which source it 
	// came from.

	public void removeMaxPage(PortletRequest request, int index);
	// Remove the page of the specified index.

	public String getMaximizedDisplayType(PortletRequest request);
	// return the display that that is selected for this portlet when maximized.
	
	public void setMaximizedDisplayType(PortletRequest request, String disp_type);
	// set the display type that is selected for this portlet when maximized.

	public Map<String,String> getMaxDisplayAttributes(PortletRequest request);
	// return the display information for this portlet. This allows for display
	// types that require extra information like pages that need to be placed
	// in a special location. or attributes like tab width.

}
