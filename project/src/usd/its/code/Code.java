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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.*;
import java.util.Random;
import java.util.Date;
import java.lang.Math;
import java.util.*;
import java.net.*;
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
    	String proxyFile=prefs.getValue("proxyFile", ""); //---Web Development Channel ONLY!
    	String isPrivate=prefs.getValue("isPrivate", "true");
		
		//--proxy the file
		if(!proxyFile.equals("")){
			Random generator = new Random();
			String randNum = "commonspotCMS_"+String.valueOf(Math.abs(generator.nextInt()))+new java.util.Date().getTime();
			
			out.print("<span id='"+randNum+"'><img src='/html/portalCMImages/ajax.gif' alt='loading' /></span>");
	    	out.print("<script>var "+randNum+"_var = new ajaxObject('"+randNum+"', '"+proxyFile+"?isPrivate="+isPrivate+"&xmlURL="+URLEncoder.encode(contentURI, "UTF-8")+"',true);"+randNum+"_var.update();</script>");
		//--get the content from the database
		}else{
			PreparedStatement pStmt = null;
			ResultSet rst = null;
			Connection conn = null;
			String content="";
			try {
				Context initContext = new InitialContext();
				Context envContext = (Context) initContext.lookup("java:/comp/env");
	
				if(envContext != null){
					DataSource ds = null;
					ds = (DataSource) envContext.lookup("jdbc/PortalDb");
	
					if(ds != null){
						conn = ds.getConnection();
						pStmt = conn.prepareStatement("SELECT cachedContent FROM UPC_USD_WEB_CONTENT_CACHE WHERE url=?");
						pStmt.setString(1,contentURI);
						rst = pStmt.executeQuery();
						if(rst.next()){
							content = (String) rst.getString("cachedContent");
						}
					 }
				 }
				 
	
			} catch (Exception e) { 
				content = "";
			}
			finally{//release the connection and other resources
				try{
					if(rst!=null){
						rst.close();
					}
				}catch(Exception e1){}
				try{
					if(pStmt != null){
						pStmt.close();
					}
				}catch(Exception e2){}
				try{
					if(conn != null){
						conn.close();	
					}
				}catch(Exception e3){}
			}//end finally
			
			if(content.equals("")){
				content = "This channel contains no data.";
			}
			out.println("<span class='cmsContent'>"+content+"</span>");
		}
		

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
