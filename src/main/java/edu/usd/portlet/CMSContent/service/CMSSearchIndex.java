package edu.usd.portlet.cmscontent.service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
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
	
	public Set<String> STOP_WORDS;
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	Map<String,IndexEntry> index = new HashMap<>();
	
	public Map<String,String> doc_index = new HashMap<>();
	
	@Autowired
	List<CMSDocumentDao> dataSources;
	
	@PostConstruct
	public void init()
	{
		try
		{

//Temporarily removed to save development time.
			for(CMSDocumentDao dao: dataSources)
			{
				logger.info("Indexing " + dao.getDaoName());
				List<CMSDocument> docs = dao.getAllDocumentsContentless();
				for(CMSDocument doc: docs)
				{
					try
					{
						CMSDocument val = dao.getDocument(doc.getId());
						IndexEntry ie = new IndexEntry(val);
						index.put(dao.getDaoName() + doc.getId(),ie);
						doc_index.put(dao.getDaoName() + doc.getId(),val.render());
					}
					catch(IllegalArgumentException e)
					{
						logger.error("Error indexing document: " + doc.getId() + " Error: " + e);
					}
				}
			}
			logger.info("" + index.size() + " documents were indexed");
			logger.info("Indexer built");
		}
		catch(Exception e)
		{
			logger.error("Error building index: " + e);
		}
		STOP_WORDS = new HashSet<String>(Arrays.asList(ENGLISH_STOP_WORDS));
	}

	public int search(String query, String doc_source, String doc_id)
	{
		int ret = 0;
		query = query.toLowerCase();
		try
		{
			for(String term:query.split(" "))
			{
				//not sure if this works.
				if(STOP_WORDS.contains(term))
					continue;
				IndexEntry val = index.get(doc_source+doc_id);
				if (val.words.containsKey(term))
				{
					ret += val.words.get(term);
				}
			}
		}
		catch(Exception e)
		{
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
			content = Jsoup.parse(content).text().toLowerCase();
			content = content.replaceAll("[^A-Za-z0-9 ]", " ");
			
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
			String content = Jsoup.parse(doc.render()).text().toLowerCase();
			content = content.replaceAll("[^A-Za-z0-9 ]", " ");
			
			words = new HashMap<>();
			for(String word:content.split(" "))
				if(words.containsKey(word))
					words.put(word,words.get(word)+1);
				else
					words.put(word,1);

			if(doc.getKeyTerms() != null)
				for(String word:doc.getKeyTerms().split(" "))
					if(words.containsKey(word))
						words.put(word,words.get(word)+100);
					else
						words.put(word,100);
		}
	}
}
