package edu.usd.portlet.cmscontent.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

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
 * data from our old CMS, CSPortalPage. It is realatively straight forward as all
 * the heavy lifting is done in the database. 
 * 
 * @author Toben Archer
 * @version $Id$
 */

@Component
public class CSPortalPage implements CMSDocumentDao, DisposableBean
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public CMSDocument getDocument(String Id)
	{
		String pageUri = Id;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;

		String content = "", title = "", type = "normal";
		CMSDocument page;

		try
		{
			connection = UsdSql.getPoolConnection();
			content = "";
			logger.debug("fetching uri: " + pageUri);
			selectStatement = connection.prepareStatement("SELECT [Title],[TextBlock] FROM [uPortalUSD].[search].[vwPortalpageTextblocks] WHERE SubSiteURL + FileName = ?");
			selectStatement.setString(1, pageUri);

			resultSet = selectStatement.executeQuery();
			while (resultSet.next())
			{
				content += (String) resultSet.getString("TextBlock");
				title = resultSet.getString("Title");
			}
			page = new CMSDocument(title,Id,"CSPortalPage","html",content);
		}
		catch(Exception e)
		{
			logger.info(e);
			page = new CMSDocument("error",Id,"CSPortalPage","html","There was a problem retrieving the requested content: " + e.getMessage());
		}
		finally
		{
			UsdSql.closeResultSet(resultSet);
			UsdSql.closePreparedStatement(selectStatement);
			UsdSql.closePoolConnection(connection);
		}
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
			selectStatement = connection.prepareStatement("SELECT Title,[SubSiteURL]+[FileName] as PagePath,* FROM [uPortalUSD].[search].[vwPortalpageTextblocks] as docs Inner Join (SELECT ID, max(DateApproved) as MaxDate FROM [uPortalUSD].[search].[vwPortalpageTextblocks] group by ID) udocs ON udocs.ID = docs.ID AND udocs.MaxDate = docs.DateApproved");

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
			selectStatement = connection.prepareStatement("SELECT Title,[SubSiteURL]+[FileName] as PagePath,* FROM [uPortalUSD].[search].[vwPortalpageTextblocks] as docs Inner Join (SELECT ID, max(DateApproved) as MaxDate FROM [uPortalUSD].[search].[vwPortalpageTextblocks] group by ID) udocs ON udocs.ID = docs.ID AND udocs.MaxDate = docs.DateApproved");

			resultSet = selectStatement.executeQuery();
			String title;
			String url;
			while(resultSet.next())
			{
				CMSDocument page = new CMSDocument();
				page.setTitle((String)(resultSet.getString("Title")));
				page.setId((String)(resultSet.getString("PagePath")));
				page.setDocType("html");
				page.setSource("CSPortalPage");
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
	
	public void saveDocument(CMSDocument val){}
	
	public String getDaoName()
	{
		return "CSPortalPage";
	}

	public void destroy() throws Exception {
	}

	public boolean deleteDocument(String Id){return false;}

	public boolean saveEnabled(){return false;}
	
	public boolean deleteEnabled(){return false;}
	
	public String getSourceType(){return "html";}
}
