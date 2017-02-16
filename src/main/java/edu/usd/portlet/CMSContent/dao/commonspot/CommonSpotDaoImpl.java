package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.PreparedStatement;
import javax.sql.DataSource;

import edu.usd.portlet.cmscontent.dao.UsdSql;

/**
 * This is an implementation of the CMSDataDao. It is responsible for pulling in
 * data from our old CMS, CommonSpot. It is realatively straight forward as all
 * the heavy lifting is done in the database. 
 * 
 * @author Toben Archer
 * @version $Id$
 */

public class CommonSpotDaoImpl implements CMSDataDao, DisposableBean
{

	public String getContent(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

		String pageUri = preferences.getValue("pageUri","");
		String content = "";

		Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
		String username = (String)userInfo.get("username");

		try
		{
				connection = UsdSql.getPoolConnection();
				selectStatement = connection.prepareStatement("exec dbo.selectChannelContentForUser ?, ?");
				selectStatement.setString(1, username);
				selectStatement.setString(2, pageUri);

				resultSet = selectStatement.executeQuery();
				while(resultSet.next())
				{
						content += (String) resultSet.getString("cachedContent");
				}
		}
		catch(Exception e)
		{
				content = "There was a problem retrieving the requested content.";
		}
		finally
		{
				UsdSql.closeResultSet(resultSet);
				UsdSql.closePreparedStatement(selectStatement);
				UsdSql.closePoolConnection(connection);
		}

		return content;
	}

	public ArrayList<CMSPageInfo> getAvailablePages()
	{
		return new ArrayList<CMSPageInfo>();
	}

	public Collection<String> getAvailableGroups()
	{
		return new ArrayList<String>();
	}

	public void destroy() throws Exception {
	}

}
