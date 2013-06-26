package usd.its.code;

import java.io.IOException;
import java.io.PrintWriter;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletContext;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.PortletURL;
import javax.portlet.PortletMode;
import java.util.Map;
import javax.portlet.PortletRequest;
import javax.portlet.PortletPreferences;
//import javax.portlet.PortletRequestDispatcher;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Savepoint;
import java.sql.Statement; //optional
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import java.io.*;
import java.util.Random;
import java.util.Date;
import java.lang.Math;
import java.util.*;
import java.net.*;

//--database
import edu.usd.its.UsdSql;
/**
 * 
 *
 */
public class Code extends GenericPortlet {
//public int appID = 1;

    /**
     * This method is called when the portlet is rendered in the VIEW portlet mode state.
     *
     * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest, javax.portlet.RenderResponse)
     */
    public void doView(RenderRequest request, RenderResponse response)
    	throws PortletException, IOException
	{
    	response.setContentType("text/html");
    	PrintWriter out = response.getWriter();
    	/*Get the publish params from the portlet */
    	Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
    	PortletPreferences prefs = request.getPreferences();
    	String contentURI=prefs.getValue("contentURI", "");

		PreparedStatement selectStatement = null;
		ResultSet resultSet = null;
		Connection connection = null;
		String content="";
		
		
		try{
			connection = UsdSql.getPoolConnection();
			selectStatement = connection.prepareStatement("SELECT cachedContent FROM channelCache WHERE url=?");
			selectStatement.setString(1,contentURI);
			resultSet = selectStatement.executeQuery();
			if(resultSet.next()){
				content = (String) resultSet.getString("cachedContent");
			}
		}catch(Exception e){
			content = "There was a problem retrieving the requested content.";
		}finally{
			UsdSql.closeResultSet(resultSet);
			UsdSql.closePreparedStatement(selectStatement);
			UsdSql.closePoolConnection(connection);
		}
		
		
		if(content.equals("")){
			content = "This channel contains no data.";
		}
		out.println(content);
		

	    /* *************************************** */
    	/*Get user attributes mapped in portlet.xml*/
    	//Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
    	/*String username = (String)userInfo.get("username");
        String datatelid = (String)userInfo.get("datatelid");*/
    	/* **************************************** */
        response.setContentType("text/html");
        PortletURL editURL = response.createRenderURL();
        editURL.setPortletMode(PortletMode.EDIT);
        PortletURL viewURL = response.createRenderURL();
        viewURL.setPortletMode(PortletMode.VIEW);
        //out.println("This is the default view!:");
       
        
        out.flush();
    }
    public void doEdit(RenderRequest request, RenderResponse response)
	throws PortletException, IOException
{
    	/*Get the publish params from the portlet */
    	PortletPreferences prefs = request.getPreferences();
    	/*int appID = Integer.parseInt(prefs.getValue("appID", "-1"));
    	int appMode=Integer.parseInt(prefs.getValue("appMode", "-1"));
    	String appName=prefs.getValue("appName", "Default");*/
    	/* *************************************** */
    	/*Get user attributes mapped in portlet.xml*/
    	Map userInfo = (Map)request.getAttribute(PortletRequest.USER_INFO);
    	/*String username = (String)userInfo.get("username");
        String datatelid = (String)userInfo.get("datatelid");*/
    	/* **************************************** */
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        PortletURL editURL = response.createRenderURL();
        editURL.setPortletMode(PortletMode.EDIT);
        PortletURL viewURL = response.createRenderURL();
        viewURL.setPortletMode(PortletMode.VIEW);
        //out.println("This is the default view!:");
       
        PreparedStatement pStmt = null;
        ResultSet rst = null;
        Connection conn = null;
        /*String mode = request.getParameter("mode"); */
        
        
        out.flush();
	}

}
