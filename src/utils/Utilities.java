package utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;

/**
 * Provides utlities such as logging, stemming and list of polar words that 
 * can be used to provide information to the user or process the data set 
 * before it is submitted to Weka for classification.
 * 
 * @author Muhammad Sajawal Javaid
 * @version 1.0
 */
public class Utilities {
	
	/**
	 * Logging Option: Adds a new line after logging message.
	 */
	public static int ADD_NEW_LINE = 0;
	
	/**
	 * logging Option: Does not add a new line after logging message.
	 */
	public static int NO_NEW_LINE = 1;
	
	/**
	 * Stores the stemmed Positive Words obtained from the <code>GeneralInquiry</code>.
	 */
	public static DynamicArray<String> POSITIVE_WORDS = null;
	
	/**
	 * Stores the stemmed Negative Words obtained from the <code>GeneralInquiry</code>.
	 */
	public static DynamicArray<String> NEGATIVE_WORDS = null;
	
	/**
	 * The directory where the project is stored on the system.
	 */
	public static String PROJECT_DIR;
	
	/**
	 * The directory where the project resources are located.
	 */
	public static String RESOURCE_DIR;
	
	/**
	 * <code>SnowballStemmer</code> object that allows for <code>String</code> stemming.
	 */
	public static SnowballStemmer STEMMER = null;
	
	/**
	 * <code>Stopwords</code> object that allows for removal of stopwords from <code>String</code> objects.
	 */
	public static Stopwords STOPWORD_HANDLER = null;
	
	/**
	 * Path to the Positive Words file obtained from <code>GeneralInquiry</code>.
	 */
	private String POSITIVE_WORDS_DATA_FILE;
	
	/**
	 * Path to the Negative Words file obtained from <code>GeneralInquiry</code>.
	 */
	private String NEGATIVE_WORDS_DATA_FILE;
	
	/**
	 * Initializes the global variables that will be used for pre-processing.
	 */
	public void initializeUtilities() {
		STEMMER = new SnowballStemmer();
		STOPWORD_HANDLER = new Stopwords();
		POSITIVE_WORDS = new DynamicArray<String>();
		NEGATIVE_WORDS = new DynamicArray<String>();
		
		String userDir = System.getProperty("user.dir");
		PROJECT_DIR = userDir.substring(0, userDir.indexOf("weka-tweeter")) + "weka-tweeter/";
		RESOURCE_DIR = PROJECT_DIR + "Resources/";
		POSITIVE_WORDS_DATA_FILE = RESOURCE_DIR + "Positive.txt";
		NEGATIVE_WORDS_DATA_FILE = RESOURCE_DIR + "Negative.txt";
		
		populatePositiveNegativeWords();
	}
	
	/**
	 * Reads the Positive and Negative Words files obtained from <code>GeneralInquiry</code>
	 * and parses them into <code>DynamicArrays</code>. These can be read later to determine
	 * the polarity of a data instance.
	 */
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
	
	/**
	 * Logger tool for the application. Can be extended to include further options.
	 * 
	 * @param data The information message to log to the user.
	 * @param option How to process the information message before logging.
	 */
	public static void log(String data, int option) {
		if(option == ADD_NEW_LINE)
			System.out.println(data);
		else if(option == NO_NEW_LINE)
			System.out.print(data);
	}

}
