package luceneSearchIndex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

public class DocumentReader {
	
	/**
	 * Pfad zur Datei, die eingelesen werden soll, als Zeichenkette.
	 */
	String filePath;	
	
	/**
	 * Standard-Konstruktor
	 * @param path
	 */
	public DocumentReader(String path) {
		this.filePath = path;
	}
	
	public ArrayList<Entry> readCsvFile() {
		
		ArrayList<Entry> entries = new ArrayList<>();
		CSVReader in = null;
		
		try {
			in = new CSVReader(new FileReader(new File(filePath)));
			
			String[] line = in.readNext(); // Erste Zeile überspringen
			Entry newEntry;
			int newId;
			String title, text;
			
			while ((line = in.readNext()) != null) {
				if (line.length >= 3) {
					newId = Integer.parseInt(line[0]);
					title = line[1].trim();
					text = line[2];
					newEntry = new Entry(newId, title, text);
					entries.add(newEntry);					
				}	
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return entries;
	}

}
