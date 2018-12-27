package edu.usd.portlet.cmscontent.rest;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Thread;
import java.lang.Runnable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import edu.usd.portlet.cmscontent.dao.FormDaoImpl;
import edu.usd.portlet.cmscontent.dao.FormDoc;
import edu.usd.portlet.cmscontent.dao.ReportDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSResponder;
import edu.usd.portlet.cmscontent.dao.DatabaseResponse;
import edu.usd.portlet.cmscontent.dao.DatabaseRepo;
import edu.usd.portlet.cmscontent.dao.InternalDocumentDao;
import edu.usd.portlet.cmscontent.dao.TokenRepo;

/**
 * This class provides a REST API for the CMS Reports 
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */

@RestController
@RequestMapping("/v2/reports")
public final class ReportApi {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	ReportDaoImpl reportdao;
	
	@Autowired
	List<CMSResponder> responders;
	
	@Autowired
	private DatabaseRepo databaseRepo;
	
	@Autowired
	private InternalDocumentDao internalDocumentDao;
	
	@Autowired
	private TokenRepo tokenRepo;

	@RequestMapping("/{report_id}")
	public String pagination(
		@PathVariable(value="report_id") String report,
		@RequestParam(value="offset", defaultValue = "0") String offset,
		@RequestParam(value="limit", defaultValue = "25") String limit,
		@RequestParam(value="token") String token
		)
	{
		int iOffset = Integer.parseInt(offset);
		int iLimit = Integer.parseInt(limit);
		logger.debug("Token: " + token);
		if(tokenRepo.validateToken(token,report))
		{
			logger.debug("limit token");
			List<DatabaseResponse> responses = databaseRepo.getResponsesPaged(report,iOffset,iLimit);
			logger.debug("Number of responses: " + responses.size());
			String ret = "[ ";
			for(DatabaseResponse resp:responses)
				ret += resp.json() + ",";
			return (ret.substring(0,ret.length()-1) + "]").replace("\n","");
		}
		else
			return "{\"result\":\"failure\",\"reason\":\"Invalid token\"}";
	}

	@RequestMapping("/{report_id}/rows")
	public String rows(
		@PathVariable(value="report_id") String report
		)
	{
		String ret = "{\"rowCount\":"+databaseRepo.getResponseCount(report)+"}";
		return ret;
	}

	@RequestMapping("/{report_id}/fields")
	public String fields(
		@PathVariable(value="report_id") String report
		)
	{
		String[] fields = new FormDoc(this.internalDocumentDao.getDocumentById(report)).getFields();
		String ret = "[\"Response Number\",\"Username\",\"Date Time\",";
		for(String field:fields)
			ret += "\"" + field + "\",";
		
		return ret.substring(0,ret.length()-1) + "]";
	}
}

