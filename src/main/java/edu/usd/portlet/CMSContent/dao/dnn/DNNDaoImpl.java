package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.DisposableBean;

import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;

import java.util.*;
import java.sql.*;

import edu.usd.portlet.cmscontent.dao.UsdSql;
import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

public class DNNDaoImpl implements CMSDataDao, DisposableBean
{

	public ArrayList<CMSPageContent> getContent(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		String content = "", title = "";
		CMSPageContent page;
		ArrayList<CMSPageContent> ret = new ArrayList<CMSPageContent>();

		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

//		String pageUri = "/Channels/myUSDhelp";
		String pageUri = preferences.getValue("pageUri","/404ErrorPage");

		try
		{
			connection = UsdSql.getPoolConnection();
			selectStatement = connection.prepareStatement("exec dbo.selectDNNContent ?");
			selectStatement.setString(1, pageUri);

			resultSet = selectStatement.executeQuery();
			while(resultSet.next())
			{
				content += (String) resultSet.getString("content");
				content = content.replace("&lt;","<");
				content = content.replace("&gt;",">");
				content = content.replace("&quot;","\"");
				content = content.replace("&amp;nbsp;","\n");
				content = content.replace("&amp;#39;","'");
				title = (String) resultSet.getString("title");
				page = new CMSPageContent(content,title);
				ret.add(page);
				title = "";
				content = "";
			}
			
		}
		catch(Exception e)
		{
//			content = "There was a problem retrieving the requested content. " + e.getMessage();
		}
		finally
		{
			UsdSql.closeResultSet(resultSet);
			UsdSql.closePreparedStatement(selectStatement);
			UsdSql.closePoolConnection(connection);
		}
		return ret;
	}

	public CMSPageContent getPageContent(String pageUri)
	{
		String content = "", title = "";
		CMSPageContent page;

		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

//		String pageUri = "/Channels/myUSDhelp";
//		String pageUri = preferences.getValue("pageUri","/404ErrorPage");

		try
		{
			connection = UsdSql.getPoolConnection();
			selectStatement = connection.prepareStatement("exec dbo.selectDNNContent ?");
			selectStatement.setString(1, pageUri);

			resultSet = selectStatement.executeQuery();
			resultSet.next();
			content += (String) resultSet.getString("content");
			content = content.replace("&lt;","<");
			content = content.replace("&gt;",">");
			content = content.replace("&quot;","\"");
			content = content.replace("&amp;nbsp;","\n");
			content = content.replace("&amp;#39;","'");
			title = (String) resultSet.getString("title");
			page = new CMSPageContent(content,title);
			
		}
		catch(Exception e)
		{
//			content = "There was a problem retrieving the requested content. " + e.getMessage();
			page = new CMSPageContent("error","error");
		}
		finally
		{
			UsdSql.closeResultSet(resultSet);
			UsdSql.closePreparedStatement(selectStatement);
			UsdSql.closePoolConnection(connection);
		}
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
			selectStatement = connection.prepareStatement("exec dbo.selectDNNAvailablePages");

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
