import weka.core.stemmers.SnowballStemmer;
public class PreProcessor {
	
	SnowballStemmer STEMMER = null;
	
	public PreProcessor() {
		// TODO
		this.STEMMER = new SnowballStemmer();
	}
	
	public String preprocess(String input) {
		// TODO
		String output = stem(input);
		output = removeStopwords(output);
		return output;
	}
	
	/**
	 * Uses the <code>SnowballStemmer</code> that is packaged with <code>Weka</code>
	 * to perform stemming on documents in the corpus.
	 * 
	 * @param input A document string in the corpus.
	 * @return Stemmed document string.
	 */
	private String stem(String input) {
		if(this.STEMMER == null) return input;
		String output = "";
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			output += this.STEMMER.stem(tokens[i]);
			if(i != tokens.length-1) output += " ";
		}
		return output;
	}
	
	private String removeStopwords(String input) {
		// TODO
		return input;
	}
	
	/*
	 * PreProcessor testing code in absence of JUnit Testing
	 */
	public static void main(String[] args) {    
		String testString = "shortly functional"; 
		PreProcessor p = new PreProcessor();
		String text = p.stem(testString);
		System.out.println(text);
	}

}
