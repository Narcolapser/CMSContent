package edu.usd.portlet.cmscontent.service;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.stereotype.Component;

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
	private StandardAnalyzer analyzer;
	private Directory index;
	private IndexWriterConfig config;
	private IndexWriter writer;
	
	@Autowired
	List<CMSDocumentDao> dataSources;
	
	@PostConstruct
	public void init()
	{
		try
		{
		//CharArraySet((Collection)Arrays.asList(this.ENGLISH_STOP_WORDS),true));
			//CharArraySet stopers = new CharArraySet();
			analyzer = new StandardAnalyzer(new CharArraySet(Arrays.asList(this.ENGLISH_STOP_WORDS),true));
			index = new RAMDirectory();
			config = new IndexWriterConfig(analyzer);
			writer = new IndexWriter(index,config);
			for(CMSDocumentDao dao: dataSources)
			{
				logger.debug("Indexing " + dao.getDaoName());
				List<CMSDocument> docs = dao.getAllDocumentsContentless();
				for(CMSDocument doc: docs)
				{
					addDoc(dao.getDocument(doc.getId()));
				}
			}
			writer.commit();
			writer.close();
			logger.debug("Indexer built");
		}
		catch(Exception e)
		{
			logger.error("Error building index: " + e);
		}
	}

	public List<CMSDocument> search(String query)
	{
		List<CMSDocument> ret = new ArrayList<CMSDocument>();
		try
		{
			//Query q = new QueryParser("content",analyzer).parse(query);
			//BooleanQuery q = new BooleanQuery();
			Builder bq = new Builder();
			for(String term:query.split(" "))
			{
				if(term.length() == 1)
					continue;				
				bq.add(new TermQuery(new Term("content",""+term+"")),BooleanClause.Occur.SHOULD);
				//bq.add(new RegexpQuery(new Term("content","[^a-zA-Z\\d\\s:]"+term+"")));
			}
			BooleanQuery q = bq.build();
			logger.debug("Query: ");
			logger.debug(q);
			int hitsPerPage = 1;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(q,hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;
			if(hits.length > 0)
			{
//				logger.debug("Found " + hits.length + " hits.");
				for(ScoreDoc hit: hits)
				{
//					logger.debug("Hit: ");
					Document d = searcher.doc(hit.doc);
//					logger.debug(d.get("title") + " " + d.get("id"));
					CMSDocumentDao dao = getDbo(d.get("source"));
					
					ret.add(dao.getDocument(d.get("id")));
				}
			}
		}
		catch(Exception e)
		{
			//logger.error("Problem searching: " + e);
		}
		
		
		return ret;
	}
	private void addDoc(CMSDocument cmsdoc) throws IOException
	{
		Document doc = new Document();
		doc.add(new TextField("title", cmsdoc.getTitle(), Field.Store.YES));

		doc.add(new TextField("content", cmsdoc.getContent(), Field.Store.NO));
		doc.add(new StringField("id",cmsdoc.getId(), Field.Store.YES));
		doc.add(new StringField("source",cmsdoc.getSource(), Field.Store.YES));
		writer.addDocument(doc);
	}

	public CMSDocumentDao getDbo(String name)
	{
		CMSDocumentDao dbo = dataSources.get(0);
		for(CMSDocumentDao ds:dataSources)
			if (ds.getDaoName().equals(name))
				dbo = ds;
		return dbo;
	}
}
