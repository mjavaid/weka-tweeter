import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;

public class PreProcessor {
	
	/**
	 * <code>SnowballStemmer</code> object that allows for <code>String</code> stemming.
	 */
	SnowballStemmer STEMMER = null;
	
	/**
	 * <code>Stopwords</code> object that allows for removal of stopwords from <code>String</code> objects.
	 */
	Stopwords STOPWORD_HANDLER = null;
	
	/**
	 * PreProcessor constructor, that initializes the <code>SnowballStemmer</code> and 
	 * <code>Stopwords</code> objects.
	 */
	public PreProcessor() {
		// TODO
		this.STEMMER = new SnowballStemmer();
		this.STOPWORD_HANDLER = new Stopwords();
	}
	
	/**
	 * Public interface for the <code>PreProcessor</code> class. Can be called to perform 
	 * preprocessing on the document corpus.
	 * 
	 * It first removes the stopwords from the document and then stems the remaining tokens.
	 * 
	 * @param input A document string in the corpus.
	 * @return The preprocessed document string.
	 */
	public String preprocess(String input) {
		String output = regexHandler(input);
		output = removeStopwords(output);
		output = stem(output);
		return output;
	}
	
	/**
	 * Uses the <code>SnowballStemmer</code> library that is packaged with <code>Weka</code>
	 * to perform stemming on documents in the corpus.
	 * 
	 * @param input A document string in the corpus.
	 * @return Stemmed document string.
	 */
	private String stem(String input) {
		if(this.STEMMER == null) return input;
		String output = "";
		String stemmed = "";
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			stemmed = this.STEMMER.stem(tokens[i]);
			output += stemmed + " ";
		}
		return output.trim();
	}
	
	private String regexHandler(String input) {
		String output = "";
		String [] tokens = input.split(" ");
		for(int i=0; i < tokens.length; i++) {
			if(!(tokens[i].startsWith("http") || tokens[i].startsWith("https") || tokens[i].startsWith("www"))) {
				output += tokens[i] + " ";
			}
		}
		output = output.replaceAll("[0-9]","");
		output = output.replaceAll("[\\W&&[^\\s{1}]]", "");
		output = output.replaceAll("\\w{1}&&[\\s]", "");
		return output;
	}
	
	private boolean isInteger(String token) {
		try {
			Integer.parseInt(token);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Uses the <code>Stopwrods</code> class that is packaged with <code>Weka</code>
	 * to remove stopwords from documents in the corpus.
	 * 
	 * @param input A document string in the corpus.
	 * @return The input string stripped of stopwords.
	 */
	private String removeStopwords(String input) {
		if(this.STOPWORD_HANDLER == null) return input;
		String output = "";
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			isInteger(tokens[i]);
			if(!(this.STOPWORD_HANDLER.is(tokens[i]))) {
				output += tokens[i] + " ";
			}
		}
		return output.trim();
	}
	
	/*
	 * PreProcessor testing code in absence of JUnit Testing
	 */
	public static void main(String[] args) {
		String testString = "shortly functional Dave nowhere Alice NOWHERE";
		PreProcessor p = new PreProcessor();
		String text = p.preprocess(testString);
		System.out.println(text);
	}

}
