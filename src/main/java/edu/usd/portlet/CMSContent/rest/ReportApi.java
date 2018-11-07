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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import edu.usd.portlet.cmscontent.dao.FormDaoImpl;
import edu.usd.portlet.cmscontent.dao.ReportDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSResponder;
import edu.usd.portlet.cmscontent.dao.DatabaseResponse;
import edu.usd.portlet.cmscontent.dao.DatabaseRepo;

/**
 * This class provides a REST API for the CMS Reports 
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */

@RestController
@RequestMapping("/v2/report")
public final class ReportApi {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	ReportDaoImpl reportdao;
	
	@Autowired
	List<CMSResponder> responders;
	
	@Autowired
	private DatabaseRepo databaseRepo;

	@RequestMapping("pagination")
	public String pagination(
		@RequestParam(value="start", defaultValue = "0") String start,
		@RequestParam(value="end", defaultValue = "5") String end,
		@RequestParam(value="report") String report
		)
	{
		int iStart = Integer.parseInt(start);
		int iEnd = Integer.parseInt(end);
		List<DatabaseResponse> responses = databaseRepo.getResponsesPaged(report,iStart,iEnd);
		String ret = "[ ";
		for(DatabaseResponse resp:responses)
			ret += resp.json() + ",";
		return ret.substring(0,ret.length()-1) + "]";
	}
}

