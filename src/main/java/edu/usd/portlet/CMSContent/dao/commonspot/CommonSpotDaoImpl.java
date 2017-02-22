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

		String pageUri = preferences.getValue("pageUri","");
		String content = "", title = "";
		CMSPageContent page;
		ArrayList<CMSPageContent> ret = new ArrayList<CMSPageContent>();

		Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
		String username = (String)userInfo.get("username");

		try
		{
			logger.info("Preparing to get information for user: " + username + " requesting page: " + pageUri);
			connection = UsdSql.getPoolConnection();
			logger.info("Check 1");
			selectStatement = connection.prepareStatement("exec dbo.selectChannelContentForUser ?, ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			selectStatement.setString(1, username);
			logger.info("Check 2");
			selectStatement.setString(2, pageUri);
			logger.info("Check 3");

			resultSet = selectStatement.executeQuery();
			logger.info("Check 4");
			resultSet.next();
			logger.info("Check 5");
			content = (String) resultSet.getString("cachedContent");
			logger.info("Information returned in first query: " + content.substring(10));
			if (!resultSet.isLast())
			{
				logger.info("Multi Page");
				String[] sectionData;
				String label, showOnLoad, url, sectionClass, bodyStyle, sectionId, sectionContent;
				String delimiter = ";";
				sectionData = ((String) resultSet.getString("cachedContent")).split(delimiter);
				url = sectionData[2];
				title = sectionData[0];

				logger.info("First URL:" + url);

				selectStatement2 = connection.prepareStatement("exec dbo.selectChannelContentForUser ?, ?");
				selectStatement2.setString(1, username);
				selectStatement2.setString(2, url);
				logger.info("Next URL: " + url);
				resultSet2 = selectStatement2.executeQuery();
				if(resultSet2.next())
				{
					sectionContent = resultSet2.getString("cachedContent");
					title = resultSet2.getString("Title");
				}
				else
				{
					sectionContent = "No Content Available";
				}

				page = new CMSPageContent(sectionContent,title);
				ret.add(page);

				while(resultSet.next())
				{
					sectionData = ((String) resultSet.getString("cachedContent")).split(delimiter);
					url = sectionData[2];
					title = sectionData[0];
					selectStatement2 = connection.prepareStatement("exec dbo.selectChannelContentForUser ?, ?");
					selectStatement2.setString(1, username);
					selectStatement2.setString(2, url);
					resultSet2 = selectStatement2.executeQuery();
					if(resultSet2.next()){
						sectionContent = resultSet2.getString("cachedContent");
						title = resultSet2.getString("Title");
					}else{
						sectionContent = "No Content Available";
					}
					page = new CMSPageContent(sectionContent,title);
					ret.add(page);
				}
			}
			else
			{
				logger.info("Single page. printing content");
				title = resultSet2.getString("Title");
				page = new CMSPageContent(content,title);
				ret.add(page);
			}
		}
		catch(Exception e)
		{
			logger.info("Error fetching content: " + e.getMessage());
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

	public ArrayList<CMSPageInfo> getAvailablePages()
	{
		ArrayList<CMSPageInfo> pages = new ArrayList<CMSPageInfo>();

		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

		try
		{
			connection = UsdSql.getPoolConnection();
			selectStatement = connection.prepareStatement("SELECT Title, url as PagePath FROM [commonspot-site-portal].synSitePages as csp JOIN [uPortalUSD].[search].[vwCmsChannelContentUrls] as ccu on ccu.pageId = csp.ID");

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
