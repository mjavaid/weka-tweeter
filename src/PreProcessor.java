import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;

public class PreProcessor {
	
	/**
	 * <code>SnowballStemmer</code> object that allows for <code>String</code> stemming.
	 */
	private SnowballStemmer STEMMER = null;
	
	/**
	 * <code>Stopwords</code> object that allows for removal of stopwords from <code>String</code> objects.
	 */
	private Stopwords STOPWORD_HANDLER = null;
	
	private final String POSITIVE_WORDS_DATA_FILE = "./Resources/Positive.txt";
	private final String NEGATIVE_WORDS_DATA_FILE = "./Resources/Negative.txt";
	
	private DynamicArray POSITIVE_WORDS = null;
	private DynamicArray NEGATIVE_WORDS = null;
	
	/**
	 * PreProcessor constructor, that initializes the <code>SnowballStemmer</code> and 
	 * <code>Stopwords</code> objects.
	 */
	public PreProcessor() {
		// TODO
		this.STEMMER = new SnowballStemmer();
		this.STOPWORD_HANDLER = new Stopwords();
		this.POSITIVE_WORDS = new DynamicArray();
		this.NEGATIVE_WORDS = new DynamicArray();
		populatePositiveNegativeWords();
	}
	
	private void populatePositiveNegativeWords() {
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(POSITIVE_WORDS_DATA_FILE);
			reader = new BufferedReader(new InputStreamReader(fis));
			String line = reader.readLine();
			while(line != null) {
				if(! line.equals("")) this.POSITIVE_WORDS.insert(stem(line.toLowerCase()));
				line = reader.readLine();
			}
			line = null;
			
			fis = new FileInputStream(NEGATIVE_WORDS_DATA_FILE);
			reader = new BufferedReader(new InputStreamReader(fis));
			line = reader.readLine();
			while(line != null) {
				if(! line.equals("")) this.NEGATIVE_WORDS.insert(stem(line.toLowerCase()));
				line = reader.readLine();
			}
		} catch (FileNotFoundException ex) {
			System.err.println("File not found!");
			System.exit(1);
		} catch (IOException ex) {
			System.err.println("Could not read from the file!");
			System.exit(1);
		} finally {
			try {
				reader.close();
				fis.close();
			} catch (IOException ex) {
				System.err.println("Error closing the file readers!");
				System.exit(1);
			}
		}
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
		String output = regexHandler(input);
		output = removeStopwords(output, category);
		output = stem(output);
		return output;
	}
	
	public double calculateWeight(String input, String category) {
		double weight = 1.0;
		DynamicArray weightWords = null;
		if(category.equals("positive"))
			weightWords = this.POSITIVE_WORDS;
		else if(category.equals("negative")) 
			weightWords = this.NEGATIVE_WORDS;
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
		if(this.STOPWORD_HANDLER == null) return input;
		boolean isPositive = false;
		boolean isNegative = false;
		if(category.equals("positive"))
			isPositive = true;
		else if(category.equals("negative"))
			isNegative = true;
		String output = "";
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			if((isPositive && this.POSITIVE_WORDS.exists(stem(tokens[i]))) 
				|| (isNegative && this.NEGATIVE_WORDS.exists(stem(tokens[i]))) 
				|| !(this.STOPWORD_HANDLER.is(tokens[i]))) {
				output += tokens[i] + " ";
			}
		}
		return output.trim();
	}
	
	public void printWords() {
		System.out.println(this.POSITIVE_WORDS+"\n====\n"+this.NEGATIVE_WORDS);
	}
	
	/*
	 * PreProcessor testing code in absence of JUnit Testing
	 */
	public static void main(String[] args) {
		//String testString = "some abound accede abhor the";
		String testString = "taylor swift come ed sheeran june perf new iv heard night";
		PreProcessor p = new PreProcessor();
		p.printWords();
		String text = p.preprocess(testString, "positive");
		System.out.println(text);
		text = "Im GSP fan hate Nick Diaz wait februari";
		System.out.println(p.calculateWeight(text, "negative"));
	}

}
