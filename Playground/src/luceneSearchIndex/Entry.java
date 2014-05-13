package luceneSearchIndex;

public class Entry {

	int DocId;
	String Title;
	String FullText;
	
	public Entry(int id, String title, String text) {
		this.DocId = id;
		this.Title = title;
		this.FullText = text;
	}
	
	public String toString() {
		return this.DocId + "," + this.Title + "," + this.FullText;
	}
}
