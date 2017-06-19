package edu.usd.portlet.cmscontent.rest;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import edu.usd.portlet.cmscontent.dao.CommonSpotDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;

@RestController
@RequestMapping("/v1/api")
public final class CMSRestController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private CMSDocumentDao csdbo = new CommonSpotDaoImpl();

	@RequestMapping("test")
	public Message test(@RequestParam(value="name", defaultValue = "Me") String name)
	{
		logger.debug("Recieved request for name:" + name);
		Message msg = new Message();
		msg.setText("testing " + name);
		return msg;
	}

	@RequestMapping("getPages")
	public ArrayList<CMSDocument> getPages(@RequestParam(value="source", defaultValue = "CommonSpot") String source)
	{
		logger.debug("Recieved request to get pages for: " + source);
		ArrayList<CMSDocument> pages;
		if(source.equals("DNN"))
			pages = csdbo.getAllDocumentsContentless();
		else
			pages = csdbo.getAllDocumentsContentless();
		return pages;
	}

	@RequestMapping("getPagesWithIndex")
	public PagesAndIndex getPagesWithIndex(
		@RequestParam(value="source", defaultValue = "CommonSpot") String source,
		@RequestParam(value="index", defaultValue = "1") String index_str
		)
	{
		logger.debug("Recieved request to get pages for: " + source + " with index: " + index_str);
		//int index = Integer.parseInt(index_str);
		PagesAndIndex ret;
		try
		{
			ArrayList<CMSDocument> pages;
			if(source.equals("DNN"))
				pages = csdbo.getAllDocumentsContentless();
			else if (source.equals("CommonSpot"))
				pages = csdbo.getAllDocumentsContentless();
			else
			{
				logger.debug("Request made to the empty data provider");
				pages = new ArrayList<CMSDocument>();
			}
			ret = new PagesAndIndex(pages,index_str);
			logger.debug("Returning result");
		}
		catch(Exception e)
		{
			logger.info("Failed to retrieve information" + e.toString());
			ret = new PagesAndIndex();
		}
		return ret;
	}

	@XmlRootElement
	public final static class Message {
		String text;

		@XmlElement
		public String getText() {return text;}

		public void setText(String text) { this.text = text; }
	}

	@XmlRootElement
	public final static class PagesAndIndex
	{
		@XmlElement
		ArrayList<CMSDocument> pages;
		@XmlElement
		String index;

		public PagesAndIndex(){}

		public PagesAndIndex(ArrayList<CMSDocument> pages, String index)
		{
			this.pages = pages;
			this.index = index;
		}
		public ArrayList<CMSDocument> getPages()
		{
			return this.pages;
		}
		public void setPages(ArrayList<CMSDocument> val)
		{
			this.pages = val;
		}
		public String getIndex()
		{
			return this.index;
		}
		public void setIndex(String val)
		{
			this.index = val;
		}
	}
}

