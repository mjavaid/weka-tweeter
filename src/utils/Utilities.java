package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;

public class Utilities {
	
	public static DynamicArray<String> POSITIVE_WORDS = null;
	
	public static DynamicArray<String> NEGATIVE_WORDS = null;
	
	/**
	 * <code>SnowballStemmer</code> object that allows for <code>String</code> stemming.
	 */
	public static SnowballStemmer STEMMER = null;
	
	/**
	 * <code>Stopwords</code> object that allows for removal of stopwords from <code>String</code> objects.
	 */
	public static Stopwords STOPWORD_HANDLER = null;
	
	private final String POSITIVE_WORDS_DATA_FILE = "./Resources/Positive.txt";
	private final String NEGATIVE_WORDS_DATA_FILE = "./Resources/Negative.txt";
	
	public void initializeUtilities() {
		STEMMER = new SnowballStemmer();
		STOPWORD_HANDLER = new Stopwords();
		POSITIVE_WORDS = new DynamicArray<String>();
		NEGATIVE_WORDS = new DynamicArray<String>();
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
				if(! line.equals("")) POSITIVE_WORDS.insert(STEMMER.stem(line.toLowerCase()));
				line = reader.readLine();
			}
			line = null;
			
			fis = new FileInputStream(NEGATIVE_WORDS_DATA_FILE);
			reader = new BufferedReader(new InputStreamReader(fis));
			line = reader.readLine();
			while(line != null) {
				if(! line.equals("")) NEGATIVE_WORDS.insert(STEMMER.stem(line.toLowerCase()));
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

}