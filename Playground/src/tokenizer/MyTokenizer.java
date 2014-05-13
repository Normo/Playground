package tokenizer;

public class MyTokenizer implements TokenizerInterface {
	
	public String tokenize(String content) {
		
		
		String str = new String();
		str = content;
		String[] contentArr = str.split("\\s+|(»|«|\"|›|‹|\\(|\\)|\\{|\\}|\\\\|/|\\:|\\.|,|\n)");

		int startIndex;
		StringBuilder sb = new StringBuilder("[ ");
		if (!contentArr[0].isEmpty()) {
			sb.append(contentArr[0]);
			startIndex = 1;
		} else {
			sb.append(contentArr[1]);
			startIndex = 2;
		}
		int tokenCounter = 1;
		for (int i = startIndex; i < contentArr.length; i++) {
			if (!contentArr[i].isEmpty()) {
				sb.append(", " + contentArr[i]);
				tokenCounter++;
			}
		}
		sb.append(" ]");
		
		return sb + "\nErgebnis: " + tokenCounter + " Token\n";
		
//		String key;
//		Integer value = 0;
//		HashMap<String, Integer> tokens = new HashMap<>();
//		for (int i = 0; i < contentArr.length; i++) {
//			if (!contentArr[i].isEmpty()) {
//				key = contentArr[i].toLowerCase();
//				if (!tokens.containsKey(key)) {
//					value = 1;
//				} else {
//					value = tokens.get(key);
//					value++;
//				}
//				tokens.put(key, value);
//			}
//		}
		
//		SortedSet<String> keys = new TreeSet<>(tokens.keySet());
		
//		return keys.toString() + "\n\nErgebnis: " + keys.size() + " Token\n";
	}
}
