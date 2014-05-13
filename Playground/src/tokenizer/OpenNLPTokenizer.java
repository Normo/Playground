package tokenizer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

@SuppressWarnings("unused")
public class OpenNLPTokenizer implements TokenizerInterface{
	
	Tokenizer tokenizer;
	
	public OpenNLPTokenizer(String args) throws InvalidFormatException, IOException {
		InputStream is = new FileInputStream(args);
		
		TokenizerModel model = new TokenizerModel(is);
//		TokenizerModel model = TrainTokenizer(args);
		
		this.tokenizer = new TokenizerME(model);
	}
	
	@SuppressWarnings({ "deprecation" })
	private static TokenizerModel TrainTokenizer(String args) throws IOException {
		Charset charset = Charset.forName("UTF-8");
		ObjectStream<String> lineStream = new PlainTextByLineStream(new FileInputStream(args), charset);
		
		ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);
		
		TokenizerModel model = null;
		
		try {
			model = TokenizerME.train("de", sampleStream, true, TrainingParameters.defaultParams());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			sampleStream.close();
		}
		
		return model;
		
	}

	public String tokenize(String content) {
		StringBuilder sb = new StringBuilder("[ ");
		String[] tokens = this.tokenizer.tokenize(content);
		int size = tokens.length;
		sb.append(tokens[0]);
		
		for (int i = 1; i < size; i++) {
			sb.append(", " + tokens[i]);
		}
		
		sb.append(" ]");
		
		return sb.toString() + "\n\nErgebnis: " + size + " Token\n";
	}
	
}
