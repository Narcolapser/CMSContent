package edu.usd.portlet.cmscontent.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;

//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;


public class UsdSql{
	public static Connection getPoolConnection(){
		try{
			return (Connection)((DataSource)((Context)new InitialContext().lookup("java:/comp/env")).lookup("jdbc/uPortalUSD")).getConnection();
		}catch(Exception e){
			System.out.println("Error generating pool: "+e.getMessage());
			return null;
		}
	}

	public static void closePoolConnection(Connection conn){
		try{
			if (conn != null){
				conn.close();
			}
		}catch(Exception e){/*do nothing*/}
	}	

	public static void closeResultSet(ResultSet rst){
		try{
			if (rst != null){
				rst.close();
			}
		}catch(Exception e){/*do nothing*/}
	}

	public static void closePreparedStatement(PreparedStatement pStmt){
		try{
			if (pStmt != null){
				pStmt.close();
			}
		}catch(Exception e){/*do nothing*/}
	}	

	public static void closeCallableStatement(CallableStatement cStmt){
		try{
			if (cStmt != null){
				cStmt.close();
			}
		}catch(Exception e){/*do nothing*/}
	}

 
}
