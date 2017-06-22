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
 * This is an implementation of the CMSDocumentDao. It is responsible for pulling in
 * data from our old CMS, CommonSpot. It is realatively straight forward as all
 * the heavy lifting is done in the database. 
 * 
 * @author Toben Archer
 * @version $Id$
 */

public class CommonSpotDaoImpl implements CMSDocumentDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public CMSDocument getDocument(String Id)
	{
		String pageUri = Id;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		PreparedStatement selectStatement2 = null;
		ResultSet resultSet2 = null;
		Connection connection = null;

		String content = "", title = "", type = "normal";
		CMSDocument page;

		try
		{
			connection = UsdSql.getPoolConnection();
			content = "";
			logger.info("fetching uri: " + pageUri);
			selectStatement = connection.prepareStatement("SELECT Title, url, cachedContent FROM [uPortalUSD].[dbo].[vwCommonSpotExtraInfo] WHERE url=?");
			selectStatement.setString(1, pageUri);

			resultSet = selectStatement.executeQuery();
			while (resultSet.next())
			{
				content += (String) resultSet.getString("cachedContent");
				title = resultSet.getString("Title");
			}
			page = new CMSDocument(title,Id,"CommonSpot",content);
		}
		catch(Exception e)
		{
			logger.info(e);
			page = new CMSDocument("There was a problem retrieving the requested content: " + e.getMessage(),"error");
		}
		finally
		{
			UsdSql.closeResultSet(resultSet);
			UsdSql.closePreparedStatement(selectStatement);
			UsdSql.closeResultSet(resultSet2);
			UsdSql.closePreparedStatement(selectStatement2);
			UsdSql.closePoolConnection(connection);
		}
		logger.debug("returning page: " + page);
		return page;
	}

	public List<String> getAvailableDocuments()
	{
		ArrayList<String> pages = new ArrayList<String>();
		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

		try
		{
			connection = UsdSql.getPoolConnection();
			selectStatement = connection.prepareStatement("SELECT [url] as PagePath,[Title] FROM [uPortalUSD].[dbo].[vwCommonSpotExtraInfo]");

			resultSet = selectStatement.executeQuery();
			while(resultSet.next())
			{
				pages.add((String)(resultSet.getString("Title")));
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

	public List<CMSDocument> getAllDocumentsContentless()
	{
		ArrayList<CMSDocument> pages = new ArrayList<CMSDocument>();
		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

		try
		{
			connection = UsdSql.getPoolConnection();
			selectStatement = connection.prepareStatement("SELECT [url],[Title] FROM [uPortalUSD].[dbo].[vwCommonSpotExtraInfo]");

			resultSet = selectStatement.executeQuery();
			String title;
			String url;
			while(resultSet.next())
			{
				CMSDocument page = new CMSDocument();
				page.setTitle((String)(resultSet.getString("Title")));
				page.setId((String)(resultSet.getString("url")));
				page.setSource("CommonSpot");
				pages.add(page);
			}
		}
		catch(Exception e)
		{
			logger.debug("woopsie: " + e);
		}
		finally
		{
			UsdSql.closeResultSet(resultSet);
			UsdSql.closePreparedStatement(selectStatement);
			UsdSql.closePoolConnection(connection);
		}
		return pages;
	}

	public void destroy() throws Exception {
	}

}
