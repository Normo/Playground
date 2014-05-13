package luceneSearchIndex;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TotalHitCountCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 * Class for creating search indeces.
 * @author normo
 *
 */
public class Indexer {
	
	Version version;
	Analyzer analyzer;
	Directory index;
	ArrayList<Entry> entries;
	
	/**
	 * Standard-Konstruktor.
	 * @param luceneVersion verwendete Lucene-Version
	 */
	public Indexer(Version luceneVersion) {
		this.version = luceneVersion;
	}
	
	/**
	 * Creates a index file in the file system to the given path.
	 * @param entries List of entries from csv file
	 * @param analyzer anlyzes the text of entries
	 * @param filePath path where index file will be stored
	 * @return
	 */
	public Directory createSearchIndexIntoFileSystem(ArrayList<Entry> entries, Analyzer analyzer, String filePath) {
		this.index = null;
		this.analyzer = analyzer;
		
		
		try {
			this.index = new SimpleFSDirectory(new File(filePath)); // stores index in the file system

			IndexWriterConfig config = new IndexWriterConfig(this.version, this.analyzer);
			IndexWriter writer = null;

			try {
				writer = new IndexWriter(this.index, config);

				for (Entry entry : entries) {
					addDoc(writer, entry);
				}
				
				writer.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return this.index;
	}
	
	/**
	 * Creates a Lucene Search Index into main memory using a standard analyzer.
	 * @param entries List of entries from CSV-File
	 * @return the created Index
	 */
	public Directory createRAMSearchIndex(ArrayList<Entry> entries, Analyzer analyzer) {
		this.index = new RAMDirectory();  									// Speicherort des Index im RAM
		this.analyzer = analyzer;
		
		IndexWriterConfig config = new IndexWriterConfig(this.version, this.analyzer);
		IndexWriter writer = null;
		
		try {
			writer = new IndexWriter(this.index, config);
			
			for (Entry entry : entries) {
				addDoc(writer, entry);
			}
			
			writer.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return this.index;
	}
	
	/**
	 * Adds documents to the index.
	 * @param writer IndexWriter
	 * @param entry Entry from CSV-File
	 * @throws IOException
	 */
	private static void addDoc(IndexWriter writer, Entry entry) {
		Document doc = new Document();
		doc.add(new StringField("docId", Integer.toString(entry.DocId), Field.Store.YES));
		doc.add(new TextField("title", entry.Title, Field.Store.YES));
		doc.add(new TextField("fullText", entry.FullText, Field.Store.YES));
		try {
			writer.addDocument(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Evaluates the query string, which the user entered into console. Creates for every search term 
	 * two query objects: one each for the index fields 'title' and 'fullText'. The boolean key words 
	 * 'AND' and 'OR' are allowed.
	 * @param queryStr entered query string from console
	 * @return List of query objects in a group of two for each search term
	 */
	public ArrayList<Query> createQueriesFromString(String queryStr) {

		ArrayList<Query> queries = new ArrayList<>();		// list which stores the resulting query objects
		String[] splittedQueryStr; 							// holds the splitted query string

		Occur occur; // MUST for AND, SHOULD for OR
		
		if (queryStr.contains(" AND ")) {
			
			splittedQueryStr = queryStr.split(" AND ");
			occur = Occur.MUST;
			
		} else if (queryStr.contains( " OR ")) {
			
			splittedQueryStr = queryStr.split(" OR ");
			occur = Occur.SHOULD;
			
		} else if (!queryStr.isEmpty()) {
			try {
				queries.add(new org.apache.lucene.queryparser.classic.QueryParser(this.version, "title", this.analyzer).parse(queryStr));
				queries.add(new org.apache.lucene.queryparser.classic.QueryParser(this.version, "fullText", this.analyzer).parse(queryStr));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return queries;
		} else {
			System.out.println("Your search query was empty!");
			System.exit(1);
			return null;
		}
		
		BooleanQuery titleBooleanQuery = new BooleanQuery();
		BooleanQuery textBooleanQuery = new BooleanQuery();
		
		Query query1;
		Query query2;
		
		for (String string : splittedQueryStr) {
			try {
				query1 = new org.apache.lucene.queryparser.classic.QueryParser(this.version, "title", this.analyzer).parse(string);
				query2 = new org.apache.lucene.queryparser.classic.QueryParser(this.version, "fullText", this.analyzer).parse(string);
				
				titleBooleanQuery.add(query1, occur);
				textBooleanQuery.add(query2, occur);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			//titleBooleanQuery.add(new TermQuery(new Term("title", string)), occur);
			//textBooleanQuery.add(new TermQuery(new Term("fullText", string)), occur);
		}
		
		queries.add(titleBooleanQuery);
		queries.add(textBooleanQuery);
				
		return queries;
	}
	
	
	public void executeIndexSearch(String queryStr) {

		//At first evaluate the query string
		ArrayList<Query> queries = this.createQueriesFromString(queryStr);
		
		IndexReader indexReader;
		try {
			indexReader = DirectoryReader.open(this.index);
			IndexSearcher searcher = new IndexSearcher(indexReader);

			TotalHitCountCollector titleCollector;
			TotalHitCountCollector textCollector;

			TopDocs titleDocs;
			TopDocs textDocs;
			
			ScoreDoc[] titleHits;
			ScoreDoc[] textHits;

			titleCollector = new TotalHitCountCollector();
			textCollector = new TotalHitCountCollector();

			searcher.search(queries.get(0), titleCollector);
			searcher.search(queries.get(1), textCollector);

			titleDocs = searcher.search(queries.get(0), Math.max(1, titleCollector.getTotalHits()));
			textDocs = searcher.search(queries.get(1), Math.max(1, textCollector.getTotalHits()));

			titleHits = titleDocs.scoreDocs;
			textHits = textDocs.scoreDocs;
			
			System.out.println("\nFound " + titleHits.length + " hits matching title.");
			this.displayHits(titleHits, searcher);

			System.out.println("\nFound " + textDocs.totalHits + " hits matching text.");
			this.displayHits(textHits, searcher);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void displayHits(ScoreDoc[] hits, IndexSearcher searcher) throws IOException {
		
		int hitsCount = hits.length;
		
		int docId;
		Document doc;
		
		for (int i = 0; i < hitsCount; i++) {
			docId = hits[i].doc;
			doc = searcher.doc(docId);
			System.out.printf("%d. \tDOCUMENT_ID:%d\t\""+doc.get("title")+"\"\n", (i+1), Integer.parseInt(doc.get("docId")));
		}
	}
	
}
