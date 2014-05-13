package tokenizer;

import java.io.File;
import java.util.ArrayList;

public class TxtDocument implements Document {

	String docName;
	String docEncoding;
	String docCharSet;
	String docLang;
	StringBuilder docContent;
	
	public TxtDocument(File docFile, String lang, String encoding, StringBuilder content) {
		this.docName = docFile.getName();
		this.docLang = lang;
		this.docEncoding = encoding;
		this.docContent = content;
	}
	
	public String getContent() {
		return this.docContent.toString();
	}

	@Override
	public ArrayList<String[]> getContents() {
		return null;
	}
	
}
