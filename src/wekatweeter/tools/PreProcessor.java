package wekatweeter.tools;

import utils.Utilities;

/**
 * <p>Provides the functionality of processing data before it is submitted to Weka for
 * data classification.</p>
 * 
 * <p>Performs the following preprocessing:
 * 	<ul>
 * 		<li>Data conversion to lower case.</li>
 * 		<li>Stopword removal.</li>
 * 		<li>Word stemming.</li>
 * 		<li>Removal of links, numerals and special characters.</li>
 * 	</ul>
 * </p>
 * 
 * @author Muhammad Sajawal Javaid
 * @version 1.0
 */
public class PreProcessor {
	
	/**
	 * Public interface for the <code>PreProcessor</code> class. Can be called to perform 
	 * preprocessing on the document corpus.
	 * 
	 * @param input A document string in the corpus.
	 * @param category The category classification of the document.
	 * @return The processed document string.
	 */
	public String preprocess(String input, String category) {
		input = input.toLowerCase();
		String output = regexHandler(input);
		output = removeStopwords(output, category);
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
	
	/**
	 * Removes all links, numerals and special characters from the provided document.
	 * 
	 * @param input A document string in the corpus.
	 * @return The stripped document string.
	 */
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

}
