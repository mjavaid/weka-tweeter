package wekatweeter.tools;

import utils.DynamicArray;
import utils.Utilities;

public class PreProcessor {
	
	/**
	 * PreProcessor constructor, that initializes the <code>SnowballStemmer</code> and 
	 * <code>Stopwords</code> objects.
	 */
	public PreProcessor() {
		
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
	public String preprocess(String input, String category) {
		input = input.toLowerCase();
		String output = regexHandler(input);
		output = removeStopwords(output, category);
		output = stem(output);
		return output;
	}
	
	public double calculateWeight(String input, String category) {
		double weight = 1.0;
		DynamicArray<String> weightWords = null;
		if(category.equals("positive"))
			weightWords = Utilities.POSITIVE_WORDS;
		else if(category.equals("negative")) 
			weightWords = Utilities.NEGATIVE_WORDS;
		else if(category.equals("neutral")) {
			return 1.0;
			/*double positive = 0.0;
			double negative = 0.0;
			String [] tokens = input.split(" ");
			for(int i = 0; i < tokens.length; i++) {
				if(Utilities.NEGATIVE_WORDS.exists(tokens[i]))
					negative++;
				else if(Utilities.POSITIVE_WORDS.exists(tokens[i]))
					positive++;
			}
			return 1.0 + Math.abs(negative - positive);*/
		}
		if(weightWords == null) return weight;
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			if(weightWords.exists(tokens[i])) {
				weight++;
			}
		}
		return weight;
	}
	
	/**
	 * Uses the <code>SnowballStemmer</code> library that is packaged with <code>Weka</code>
	 * to perform stemming on documents in the corpus.
	 * 
	 * @param input A document string in the corpus.
	 * @return Stemmed document string.
	 */
	private String stem(String input) {
		if(Utilities.STEMMER == null) 
			return input;
		String output = "";
		String stemmed = "";
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			stemmed = Utilities.STEMMER.stem(tokens[i]);
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
		return output;
	}
	
	/**
	 * Uses the <code>Stopwrods</code> class that is packaged with <code>Weka</code>
	 * to remove stopwords from documents in the corpus.
	 * 
	 * @param input A document string in the corpus.
	 * @return The input string stripped of stopwords.
	 */
	private String removeStopwords(String input, String category) {
		if(Utilities.STOPWORD_HANDLER == null) return input;
		boolean isPositive = false;
		boolean isNegative = false;
		if(category.equals("positive"))
			isPositive = true;
		else if(category.equals("negative"))
			isNegative = true;
		String output = "";
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			if((isPositive && Utilities.POSITIVE_WORDS.exists(stem(tokens[i]))) 
				|| (isNegative && Utilities.NEGATIVE_WORDS.exists(stem(tokens[i]))) 
				|| !(Utilities.STOPWORD_HANDLER.is(tokens[i]))) {
				output += tokens[i] + " ";
			}
		}
		return output.trim();
	}
	
	/*
	 * PreProcessor testing code in absence of JUnit Testing
	 */
	public static void main(String[] args) {
		//String testString = "some abound accede abhor the";
		String testString = "taylor swift come ed sheeran june perf new iv heard night";
		PreProcessor p = new PreProcessor();
		String text = p.preprocess(testString, "positive");
		System.out.println(text);
		System.out.println(Utilities.POSITIVE_WORDS);
		text = "sat movi harri ron christma ohlawd";
		System.out.println(p.calculateWeight(text, "positive"));
		
	}

}
