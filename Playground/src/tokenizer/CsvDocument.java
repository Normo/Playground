package tokenizer;

import java.io.File;
import java.util.ArrayList;

public class CsvDocument implements Document {

	String docName;
	String docEncoding;
	String docCharSet;
	String docLang;
	ArrayList<String[]> docContents;
	
	public CsvDocument(File docFile, String lang, String encoding, ArrayList<String[]> content) {
		this.docName = docFile.getName();
		this.docLang = lang;
		this.docEncoding = encoding;
		this.docContents = content;
	}
	
	public ArrayList<String[]> getContents() {
		return this.docContents;
	}

	@Override
	public String getContent() {
		return null;
	}
	
}