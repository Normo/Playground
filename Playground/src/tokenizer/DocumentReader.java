package tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import au.com.bytecode.opencsv.CSVReader;

public class DocumentReader {

	private String fileName;
	private File docFile;
	
	public DocumentReader(String pathString) {
		this.fileName = pathString;
		this.docFile= new File(this.fileName);
	}
	
	public TxtDocument readTxtDocFile() {
		
		TxtDocument document =  null;
		BufferedReader in = null;
		String strLine;
		StringBuilder sb = new StringBuilder();
		
		try {
			in = new BufferedReader(new FileReader(this.docFile));
			
			while ((strLine = in.readLine()) != null) {
				sb.append(strLine + "\n");
			}
			
			document = new TxtDocument(docFile, "de", "UTF-8", sb);
			
		} catch (FileNotFoundException e) {
			System.out.println("Datei " + this.fileName + " wurde nicht gefunden!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O-Fehler aufgetreten (womöglich kein lesender Zugriff auf Datei?)");
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				System.out.println("Input-Stream konnte nicht geschlossen werden.");
				e.printStackTrace();
			}
		}
		return document;
	}
	
	public CsvDocument readCSVDocFile() {
		
		CsvDocument document = null;
		CSVReader reader = null;
		
		try {
			reader = new CSVReader(new FileReader(this.docFile));
			String[] line;
			ArrayList<String[]> contents = new ArrayList<>();
			line = reader.readNext(); // Erste Zeile überspringen
			while ((line = reader.readNext()) != null) {
				contents.add(line);
			}
			
			document = new CsvDocument(docFile, "de", "UTF-8", contents);
			
		} catch (FileNotFoundException e) {
			System.out.println("Datei " + this.fileName + " wurde nicht gefunden!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O-Fehler aufgetreten (womöglich kein lesender Zugriff auf Datei?)");
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return document;
	}

	public File getDocFile() {
		return docFile;
	}

	public void setDocFile(File docFile) {
		this.docFile = docFile;
	}
	
}
