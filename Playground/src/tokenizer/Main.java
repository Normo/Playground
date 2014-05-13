package tokenizer;

import java.io.IOException;
import java.util.ArrayList;

import opennlp.tools.util.InvalidFormatException;

public class Main {

	public static void main(String[] args) throws InvalidFormatException, IOException {
		if (args.length > 0) {
			String fileName = args[0];
			TokenizerInterface tokenizer;
			
			if (args.length >= 3 && args[1].equals("opennlp")) {
				tokenizer = new OpenNLPTokenizer(args[2]);
				System.out.println("Starte Apache OpenNLP Tokenizer..\n");
			} else  {
				tokenizer = new MyTokenizer();
				System.out.println("Starte MyTokenizer..\n");
			}
			
			DocumentReader docReader = new DocumentReader(fileName);
			Document document = null;
			
			if (fileName.endsWith(".csv")) {
				document = docReader.readCSVDocFile();
				ArrayList<String[]> contents = document.getContents();
				
				for (String[] value : contents) {
					if(value.length == 3) {
						System.out.println("\nID: " + value[0] + " \"" + value[1] + "\"" );
						System.out.println(tokenizer.tokenize(value[2]));
					}
				}
				
			} else {
				document = docReader.readTxtDocFile();
				System.out.println(tokenizer.tokenize(document.getContent()));
			}

			
		} else {
			System.out.println("Keine Kommandozeilenargumente übergeben!\n");
			System.out.println("Aufruf: java -jar Tokenizer.jar /Pfad/zur/Textdatei [opennlp Pfad/zur/Model-Datei]");
		}	
	}
}
