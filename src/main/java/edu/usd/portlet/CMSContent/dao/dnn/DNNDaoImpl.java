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

public class DNNDaoImpl implements CMSDataDao, DisposableBean
{

	public String getContent(PortletRequest request)
	{
		final PortletPreferences preferences = request.getPreferences();
		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

//		String pageUri = "/Channels/myUSDhelp";
		String pageUri = preferences.getValue("pageUri","/404ErrorPage");
		String content = "";

		try
		{
				connection = UsdSql.getPoolConnection();
				selectStatement = connection.prepareStatement("exec dbo.selectEvoqContent ?");
				selectStatement.setString(1, pageUri);

				resultSet = selectStatement.executeQuery();
				while(resultSet.next())
				{
						content += (String) resultSet.getString("content");
				}
				content = content.replace("&lt;","<");
				content = content.replace("&gt;",">");
				content = content.replace("&quot;","\"");
				content = content.replace("&amp;nbsp;","\n");
				content = content.replace("&amp;#39;","'");
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

	public Collection<String> getAvailablePages()
	{
		return new ArrayList<String>();
	}

	public Collection<String> getAvailableGroups()
	{
		return new ArrayList<String>();
	}

	public void destroy() throws Exception {
	}
}
