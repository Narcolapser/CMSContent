package edu.usd.portlet.cmscontent.rest;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.io.OutputStream;
import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

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
	
	@RequestMapping("csv")
	public String csv(
		@RequestParam(value="report") String report,
		@RequestParam(value="token") String token
		)
	{
		String sheet = "\"Response Number\",\"Username\",\"Date Time\",";
		String[] fields = new FormDoc(this.internalDocumentDao.getDocumentById(report)).getFields();
		sheet += String.join("\",\"",Arrays.asList(fields));
		List<DatabaseResponse> responses = databaseRepo.getResponses(report);
		for(DatabaseResponse resp:responses)
			sheet += "\n"+ resp.csvRow();
		return sheet;
	}

	@RequestMapping(value = "/export/{file_name}.csv", method = RequestMethod.GET)
	public void getCSV(
		@PathVariable("file_name") String file_name,
		@RequestParam(value="report") String report,
//		@RequestParam(value="token") String token,
		HttpServletResponse response)
	{
		//report = "academics/registrar/diploma-order-form";
//		if(!tokenRepo.validateToken(token,report))
//			return;
		try
		{
			OutputStream os = response.getOutputStream();
			String sheet = "\"Response Number\",\"Username\",\"Date Time\",";
			String[] fields = new FormDoc(this.internalDocumentDao.getDocumentById(report)).getFields();
			sheet += String.join("\",\"",Arrays.asList(fields));
			List<DatabaseResponse> responses = databaseRepo.getResponses(report);
			for(DatabaseResponse resp:responses)
				sheet += "\n"+ resp.csvRow();
			for(char ch: sheet.toCharArray())
				os.write((byte)ch);
		} catch(IOException ex)
		{
			logger.info("error writing file to output stream. File was:" + report);
			throw new RuntimeException("IOError writing file to output stream");
		}
	}


	@RequestMapping(value = "/{file_name}.xlsx", method = RequestMethod.GET)
	public void getXLSX(
		@PathVariable("file_name") String file_name,
		@RequestParam(value="token") String token,
		HttpServletResponse response) throws IOException
	{
	
		if(!tokenRepo.validateToken(token,file_name))
		{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized. Please go to report page and try again.");
			return;
		}
		try
		{
			OutputStream os = response.getOutputStream();
			logger.debug(file_name);
			String[] fields = new FormDoc(this.internalDocumentDao.getDocumentById(file_name)).getFields();
			
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet("Responses");
			Row header = sheet.createRow(0);
			Cell headerCell = header.createCell(0);
			headerCell.setCellValue("Response Number");
			headerCell = header.createCell(1);
			headerCell.setCellValue("Username");
			headerCell = header.createCell(2);
			headerCell.setCellValue("Date Time");
			
			int index = 3;
			for(String field:fields)
			{
				headerCell = header.createCell(index);
				headerCell.setCellValue(field);
				index++;
			}
			
			Row row = sheet.createRow(1);
			row.createCell(0).setCellValue("1");
			row.createCell(1).setCellValue("John Doe");
			
			List<DatabaseResponse> responses = databaseRepo.getResponses(file_name);
			index = 1;
			for(DatabaseResponse resp:responses)
			{
				Map<String,String> resp_map = resp.asMap();
				row = sheet.createRow(index);
				int j = 0;
				Cell cell =  row.createCell(j);
				cell.setCellValue(resp_map.get("Response Number"));
				j++;
				
				cell =  row.createCell(j);
				cell.setCellValue(resp_map.get("Username"));
				j++;
				
				cell =  row.createCell(j);
				cell.setCellValue(resp_map.get("Date Time"));
				j++;
				
				for(String field:fields)
				{
					cell = row.createCell(j);
					cell.setCellValue(resp_map.get(field));
					j++;
				}
				index++;
			}
			
			workbook.write(os);
			workbook.close();
		} catch(IOException ex)
		{
			logger.info("error writing file to output stream. File was:" + file_name);
			throw new RuntimeException("IOError writing file to output stream");
		}
	}
}

