package luceneSearchIndex;

import java.util.ArrayList;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

public class LuceneMain {

	public static void main(String[] args) {
		
		if (args.length < 1) {
			System.out.println("Zu wenig Parameter!");
			System.out.println("Aufruf: java -jar searchRequest.jar <Pfad zur CSV-Datei>");
		} else {
			String path = args[0];

			DocumentReader reader = new DocumentReader(path);
			ArrayList<Entry> entries = reader.readCsvFile();
			
			Version luceneVersion = Version.LUCENE_48;
			Indexer indexer = new Indexer(luceneVersion);
			
			StandardAnalyzer analyzer = new StandardAnalyzer(luceneVersion);
			@SuppressWarnings("unused")
			Directory index = indexer.createRAMSearchIndex(entries, analyzer);
			
			Scanner scanner = new Scanner(System.in);
			String choice = "";
			while(!choice.equals("q")) {

				System.out.println("Enter search query: ");
				String querystr;
				querystr = scanner.nextLine().trim();

				indexer.executeIndexSearch(querystr);
				
				System.out.println("\nType 'q' to exit or return for a new search request.");
				choice = scanner.nextLine().trim();
			}
			scanner.close();
		}
	}	
}
