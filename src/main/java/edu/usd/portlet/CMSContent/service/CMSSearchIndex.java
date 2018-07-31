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

/**
 * This class provides a prepared search index for CMSContent inorder to provide
 * a fast response to search requests. Principly it is fairly simple. Every
 * document is broken into it's lower case word tolkens. Then the occurances of
 * those words are counted and a dictionary of those word counts is created for
 * each document. When you search "campus housing" and a document uses the word
 * "campus" 10 times and "housing" 5, that document is passed back with a rank
 * of 15. However, documents can also have keywords associated with them. In
 * such a case, the keyword adds a weight of 100 to that word. 
 *
 * There is a collection of common words to be ignored at the beginning of the
 * class to skip those over powering everything.
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */

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
//			for(CMSDocumentDao dao: dataSources)
//			{
//				logger.info("Indexing " + dao.getDaoName());
//				
//				//get the documents without their content first. This is done
//				//primarily since the interface doesn't actually specifiy a way
//				//to request all the documents with content to keep it simiple.
//				List<CMSDocument> docs = dao.getAllDocumentsContentless();
//				
//				//loop over every document and construct an index entry for it.
//				for(CMSDocument doc: docs)
//				{
//					try
//					{
//						CMSDocument val = dao.getDocument(doc.getId());
//						IndexEntry ie = new IndexEntry(val);
//						index.put(dao.getDaoName() + doc.getId(),ie);
//						doc_index.put(dao.getDaoName() + doc.getId(),val.render());
//					}
//					catch(IllegalArgumentException e)
//					{
//						logger.error("Error indexing document: " + doc.getId() + " Error: " + e);
//					}
//				}
//			}
			logger.info("" + index.size() + " documents were indexed");
			logger.info("Indexer built");
		}
		catch(Exception e)
		{
			logger.error("Error building index: " + e);
		}
		
		//this seems to be the simplest way to create a set from a static array.
		STOP_WORDS = new HashSet<String>(Arrays.asList(ENGLISH_STOP_WORDS));
	}

	public int search(String query, String doc_source, String doc_id)
	{
		//We are going to be returning the search rank value of the document
		//that we have been requested to search.
		int ret = 0;
		
		//this prevents case sensativity.
		query = query.toLowerCase();
		
		//if there is a problem searching we'll just ignore this document to
		//keep things moving quickly. 
		try
		{
			//break the query into tolkens and get the rank for each one.
			for(String term:query.split(" "))
			{
				//if it is a common english word, skip it.
				if(STOP_WORDS.contains(term))
					continue;
					
				//get the index entry from the index.
				IndexEntry val = index.get(doc_source+doc_id);
				
				//if the entry has the term in it's word list, add the value to
				//this documents search rank value.
				if (val.words.containsKey(term))
					ret += val.words.get(term);
			}
		}
		catch(Exception e)
		{
			logger.error("Error searching document: " + e);
			return 0;
		}
		
		return ret;
	}

	//convinence method for getting the document source.
	private CMSDocumentDao getDbo(String name)
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
