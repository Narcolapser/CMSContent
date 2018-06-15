package edu.usd.portlet.cmscontent.service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
//import java.util.Integer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;

@Component
@Service
public class CMSSearchIndex
{
	public static final String[] ENGLISH_STOP_WORDS = {
	"a", "an", "and", "are", "as", "at", "be", "but", "by",
	"for", "if", "in", "into", "is", "it",
	"no", "not", "of", "on", "or", "such",
	"that", "the", "their", "then", "there", "these",
	"they", "this", "to", "was", "will", "with"
	};
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	Map<String,IndexEntry> index = new HashMap<>();
	
	@Autowired
	List<CMSDocumentDao> dataSources;
	
	@PostConstruct
	public void init()
	{
		try
		{

			for(CMSDocumentDao dao: dataSources)
			{
				logger.debug("Indexing " + dao.getDaoName());
				List<CMSDocument> docs = dao.getAllDocumentsContentless();
				logger.debug("1");
				for(CMSDocument doc: docs)
				{
					logger.debug("2");
					CMSDocument val = dao.getDocument(doc.getId());
					IndexEntry ie = new IndexEntry(val);
					logger.debug(ie);
					index.put(dao.getDaoName() + doc.getId(),ie);
					logger.debug(index.get(dao.getDaoName() + doc.getId()));
				}
			}
			logger.debug("Indexer built");
		}
		catch(Exception e)
		{
			logger.error("Error building index: " + e);
		}
	}

	public int search(String query, String doc_source, String doc_id)
	{
		int ret = 0;
		try
		{
			for(String term:query.split(" "))
			{
				if(Arrays.asList(ENGLISH_STOP_WORDS).contains(term))
					continue;
				IndexEntry val = index.get(doc_source+doc_id);
				logger.debug(val);
				logger.debug(val.words);
				if (val.words.containsKey(term))
				{
					ret += val.words.get(term);
					logger.debug("Got a match: " + term + " with a score of: " + val.words.get(term));
				}
			}
		}
		catch(Exception e)
		{
			logger.error("Problem searching " + doc_source + doc_id + ":" + e);
			return 0;
		}
		
		
		return ret;
	}

	public CMSDocumentDao getDbo(String name)
	{
		CMSDocumentDao dbo = dataSources.get(0);
		for(CMSDocumentDao ds:dataSources)
			if (ds.getDaoName().equals(name))
				dbo = ds;
		return dbo;
	}
	
	private class IndexEntry
	{
		private String source;
		private String id;
		private String title;
		public Map<String,Integer> words;
		
		public IndexEntry(String source, String id, String title, String content, List<String> keywords)
		{
			this.source = source;
			this.id = id;
			this.title = title;
			
			//Remove html and non alpha numeric characters
			content = Jsoup.parse(content).text();
			content = content.replaceAll("[^A-Za-z0-9 ]", "");
			
			words = new HashMap<>();
			for(String word:content.split(" "))
				if(words.containsKey(word))
					words.put(word,words.get(word)+1);
				else
					words.put(word,1);

			for(String word:keywords)
				if(words.containsKey(word))
					words.put(word,words.get(word)+100);
				else
					words.put(word,100);
		}
		public IndexEntry(CMSDocument doc)
		{
			this.source = doc.getSource();
			this.id = doc.getId();
			this.title = doc.getTitle();
			
			//Remove html and non alpha numeric characters
			String content = Jsoup.parse(doc.getContent()).text();
			content = content.replaceAll("[^A-Za-z0-9 ]", "");
			
			words = new HashMap<>();
			for(String word:content.split(" "))
				if(words.containsKey(word))
					words.put(word,words.get(word)+1);
				else
					words.put(word,1);

			for(String word:doc.getKeyTerms().split(" "))
				if(words.containsKey(word))
					words.put(word,words.get(word)+100);
				else
					words.put(word,100);
		}
	}
}
