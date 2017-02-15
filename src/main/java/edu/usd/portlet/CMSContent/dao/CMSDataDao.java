package edu.usd.portlet.cmscontent.dao;

import java.util.Collection;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

public interface CMSDataDao
{

	public String getContent(PortletRequest request);
	public Collection<String> getAvailablePages();
	public Collection<String> getAvailableGroups();

}
