/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.usd.portlet.cmscontent.portlet;

import java.util.Map;
import java.util.HashMap;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Savepoint;
//import java.sql.Statement; //optional
import java.sql.PreparedStatement;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.ModelAndView;

import edu.usd.portlet.cmscontent.dao.UsdSql;

/**
 * This class handles retrieves information and displays the view page.
 * 
 * @author Toben Archer
 * @version $Id$
 */
@Controller
@RequestMapping("VIEW")
public class CMSContentViewController {
	protected final Log logger = LogFactory.getLog(this.getClass());

	@RequestMapping
	public ModelAndView viewContent(RenderRequest request, RenderResponse response) {
		final PortletPreferences preferences = request.getPreferences();
		final Map<String, Object> refData = new HashMap<String, Object>();


		Connection connection = null;
		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;

//		String pageUri = "/Channels/myUSDhelp";
		String pageUri = preferences.getValue("pageUri","/Channels/myUSDhelp");
		String content = "";
		refData.put("pageUri",pageUri);

		Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
		String username = (String)userInfo.get("username");
		refData.put("username",username);

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
//				content = content.replace("&lt;","<");
//				content = content.replace("&gt;",">");
//				content = content.replace("&quot;","\"");
//				content = content.replace("&amp;nbsp;","\n");
//				content = content.replace("&amp;#39;","'");
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
		refData.put("content",content);

		return new ModelAndView("view",refData);
	}
}
