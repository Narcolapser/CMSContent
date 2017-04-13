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
import edu.usd.portlet.cmscontent.dao.DNNDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDataDao;
import edu.usd.portlet.cmscontent.dao.CMSPageInfo;

@RestController
@RequestMapping("/v1/api")
public final class CMSRestController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private CMSDataDao csdbo = new CommonSpotDaoImpl();
	private CMSDataDao dnndbo = new DNNDaoImpl();

	@RequestMapping("test")
	public Message test(@RequestParam(value="name", defaultValue = "Me") String name)
	{
		logger.debug("Recieved request for name:" + name);
		Message msg = new Message();
		msg.setText("testing " + name);
		return msg;
	}

	@RequestMapping("getPages")
	public ArrayList<CMSPageInfo> getPages(@RequestParam(value="source", defaultValue = "CommonSpot") String source)
	{
		logger.debug("Recieved request to get pages for: " + source);
		ArrayList<CMSPageInfo> pages;
		if(source.equals("DNN"))
			pages = dnndbo.getAvailablePages();
		else
			pages = csdbo.getAvailablePages();
		return pages;
	}

	@RequestMapping("getPagesWithIndex")
	public PagesAndIndex getPagesWithIndex(
		@RequestParam(value="source", defaultValue = "CommonSpot") String source,
		@RequestParam(value="index", defaultValue = "1") String index_str
		)
	{
		logger.debug("Recieved request to get pages for: " + source + " with index: " + index_str);
		int index = Integer.parseInt(index_str);
		ArrayList<CMSPageInfo> pages;
		if(source.equals("DNN"))
			pages = dnndbo.getAvailablePages();
		else
			pages = csdbo.getAvailablePages();
		PagesAndIndex ret = new PagesAndIndex(pages,index);
		logger.debug("Returning result");
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
		ArrayList<CMSPageInfo> pages;
		@XmlElement
		int index;

		public PagesAndIndex(ArrayList<CMSPageInfo> pages, int index)
		{
			this.pages = pages;
			this.index = index;
		}
		public ArrayList<CMSPageInfo> getPages()
		{
			return this.pages;
		}
		public void setPages(ArrayList<CMSPageInfo> val)
		{
			this.pages = val;
		}
		public int getIndex()
		{
			return this.index;
		}
		public void setIndex(int val)
		{
			this.index = val;
		}
	}
}

