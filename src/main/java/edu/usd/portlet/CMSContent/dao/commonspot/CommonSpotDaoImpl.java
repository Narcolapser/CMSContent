package edu.usd.portlet.cmscontent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	protected final Log logger = LogFactory.getLog(this.getClass());

	public ArrayList<CMSPageContent> getContent(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		PreparedStatement selectStatement2 = null;
		ResultSet resultSet2 = null;
		Connection connection = null;

		PortletPreferences prefs = request.getPreferences();
		String[] pageUriArray = prefs.getValues("pageUri",null);
		String pageUri = pageUriArray[0];
		String content = "", title = "", type = "normal";
		CMSPageContent page;
		ArrayList<CMSPageContent> ret = new ArrayList<CMSPageContent>();

		Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
		String username = (String)userInfo.get("username");

		try
		{
			connection = UsdSql.getPoolConnection();
			logger.info("Preparing to get information for user: " + username + " requesting page: " + pageUri);
			selectStatement = connection.prepareStatement("exec dbo.selectChannelContentForUser ?, ?");
			selectStatement.setString(1, username);
			selectStatement.setString(2, pageUri);

			resultSet = selectStatement.executeQuery();
			resultSet.next();

			type = (String) resultSet.getString("contentType");
			logger.debug("Got infomration from the query.");
			if (!type.equals("normal"))
			{
				logger.debug("Multi Page");
				page = getSubPageContent(resultSet,connection,username);
				ret.add(page);
				while(resultSet.next())
				{
					page = getSubPageContent(resultSet,connection,username);
					ret.add(page);
				}
			}
			else
			{
				title = resultSet.getString("Title");
				logger.debug("Single page. printing content");
				for(String uri:pageUriArray)
				{
					logger.info("fetching uri: " + uri);
					if (uri == null || uri.equals("blank"))
						continue;
					selectStatement = connection.prepareStatement("exec dbo.selectChannelContentForUser ?, ?");
					selectStatement.setString(1, username);
					selectStatement.setString(2, uri);

					resultSet = selectStatement.executeQuery();
					while (resultSet.next())
					{
						content += (String) resultSet.getString("cachedContent");
						title = resultSet.getString("Title");
					}
					page = new CMSPageContent(content,title);
					ret.add(page);
				}
			}
		}
		catch(Exception e)
		{
			logger.info(e);
			page = new CMSPageContent("There was a problem retrieving the requested content: " + e.getMessage(),"error");
			ret.add(page);
		}
		finally
		{
			UsdSql.closeResultSet(resultSet);
			UsdSql.closePreparedStatement(selectStatement);
			UsdSql.closeResultSet(resultSet2);
			UsdSql.closePreparedStatement(selectStatement2);
			UsdSql.closePoolConnection(connection);
		}
		return ret;
	}

	private CMSPageContent getSubPageContent(ResultSet resultSet, Connection con, String username) throws SQLException
	{
		PreparedStatement selectStatement = null;
		ResultSet resultSet2 = null;

		String title, sectionContent, url;
		String[] sectionData;
		sectionData = ((String) resultSet.getString("cachedContent")).split(";");
		title = sectionData[0];
		url = sectionData[2];
		selectStatement = con.prepareStatement("exec dbo.selectChannelContentForUser ?, ?");
		selectStatement.setString(1, username);
		selectStatement.setString(2, url);
		resultSet2 = selectStatement.executeQuery();
		if(resultSet2.next()){
			sectionContent = resultSet2.getString("cachedContent");
		}else{
			sectionContent = "No Content Available";
		}
		CMSPageContent page = new CMSPageContent(sectionContent,title);
		return page;
	}

	public ArrayList<CMSPageInfo> getAvailablePages()
	{
		ArrayList<CMSPageInfo> pages = new ArrayList<CMSPageInfo>();

		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

		try
		{
			connection = UsdSql.getPoolConnection();
			selectStatement = connection.prepareStatement("SELECT [url] as PagePath,[Title] FROM [uPortalUSD].[dbo].[vwCommonSpotExtraInfo]");

			resultSet = selectStatement.executeQuery();
			String title;
			String path;
			while(resultSet.next())
			{
				title = (String)(resultSet.getString("Title"));
				path  = (String)(resultSet.getString("PagePath"));
				CMSPageInfo pageInfo = new CMSPageInfo(title,path);
				pages.add(pageInfo);
			}
		}
		catch(Exception e)
		{
			
		}
		finally
		{
			UsdSql.closeResultSet(resultSet);
			UsdSql.closePreparedStatement(selectStatement);
			UsdSql.closePoolConnection(connection);
		}

		return pages;
	}

	public Collection<String> getAvailableGroups()
	{
		return new ArrayList<String>();
	}

	public void destroy() throws Exception {
	}

}
