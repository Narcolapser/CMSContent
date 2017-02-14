<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>

<%@ page contentType="text/html"
        import="edu.usd.its.UsdSql,
        java.sql.*"
        %>
<%
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet resultSet = null;

        String pageUri = "/Channels/myUSDhelp";
        String content = "";

        try
        {
                connection = UsdSql.getPoolConnection();
                selectStatement = connection.prepareStatement("exec dbo.selectEvoqContent ?");
                selectStatement.setString(1, pageUri);

                resultSet = selectStatement.executeQuery();
                while(resultSet.next())
                {
                        content += (String) resultSet.getString("Content");
                }
                content = content.replace("&lt;","<");
                content = content.replace("&gt;",">");
                content = content.replace("&quot;","\"");
                content = content.replace("&amp;nbsp;","\n");
                content = content.replace("&amp;#39;","'");
        }
        catch(Exception e)
        {
                content = "There was a problem retrieving the requested content. test 1";
                out.print(e.getMessage());
        }
        finally
        {
                UsdSql.closeResultSet(resultSet);
                UsdSql.closePreparedStatement(selectStatement);
                UsdSql.closePoolConnection(connection);
        }

        out.print("<div class=\"usdChannel\">"+content+"</div>");

%>

