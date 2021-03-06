package wekatweeter.tools;

import java.io.File;
import java.io.IOException;

import utils.DynamicArray;
import utils.TweeterToken;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

/**
 * Creates the <code>.arff</code> file for the tweeter data set provided for this assignment.
 * 
 * @author Muhammad Sajawal Javaid
 * @version 1.0
 */
public class ARFFGenerator {
	
	/**
	 * Default relation value for an instance.
	 */
	public static final String DEFAULT_RELATION_NAME = "opinion";
	
	/**
	 * Categories for an instance. The <code>objective</code> tag is counted as <code>neutral</code>.
	 */
	private final String [] CATEGORIES = {"positive","negative","neutral"};
	
	/**
	 * The default location for the generated <code>.arff</code> file. 
	 */
	private final String DEFAULT_SAVE_FILE_LOCATION = System.getProperty("user.dir") + File.separator;
	
	/**
	 * The default name for the generated <code>.arff</code> file.
	 */
	private final String DEFAULT_SAVE_FILE_NAME = "test.arff";
	
	/**
	 * The output name of the generated <code>.arff</code> file.
	 */
	private String fileName;
	
	/**
	 * The output directory of the generated <code>.arff</code> file.
	 */
	private String fileLocation;
	
	/**
	 * Initializes the output file name and directory to the default values.
	 */
	public ARFFGenerator() {
		this.fileLocation = DEFAULT_SAVE_FILE_LOCATION;
		this.fileName = DEFAULT_SAVE_FILE_NAME;
	}
	
	/**
	 * Initializes the output file name and directory to the provided arguments.
	 * 
	 * @param fileName The name of the generated <code>.arff</code> file.
	 * @param fileLocation The output directory of the generated <code>.arff</code> file.
	 */
	public ARFFGenerator(String fileName, String fileLocation) {
		this.fileLocation = fileLocation;
		this.fileName = fileName;
	}
	
	/**
	 * Returns the path to the generated <code>.arff</code> file.
	 * @return The path to the generated <code>.arff</code> file.
	 */
	public String getFileLocation() {
		return this.fileLocation+this.fileName;
	}
	
	/**
	 * Calls the <code>generateARFFFile</code> method with the provided tweet data set 
	 * and the default relation name.
	 * @param data The tweet data set.
	 */
	public void generateARFFFile(DynamicArray<TweeterToken> data) {
		if(data == null) 
			throw new IllegalArgumentException("null input data provided.");
		generateARFFFile(data, DEFAULT_RELATION_NAME);
	}
	
	/**
	 * Generates the <code>.arff</code> file with the provided tweet data set and relation name.
	 * 
	 * @param data The tweet data set.
	 * @param relationName The relation name of the data set.
	 */
	public void generateARFFFile(DynamicArray<TweeterToken> data, String relationName) {
		if(data == null) 
			throw new IllegalArgumentException("null input data provided.");
		
		// 1. Create a FastVector object to store the attributes (sentence, category)
		FastVector attrs = new FastVector();
		
		// 2. Add attributes
		attrs.addElement(new Attribute("sentence", (FastVector) null));
		FastVector categories = new FastVector();
		for(int i = 0; i < CATEGORIES.length; i++) 
			categories.addElement(CATEGORIES[i]);
		attrs.addElement(new Attribute("category", categories));
		
		// 3. Create data instance
		Instances tweetData = new Instances(relationName, attrs, 0);
		
		int dataSize = data.getSize();
		for(int i = 0; i < dataSize; i++) {
			TweeterToken dataToken = data.get(i);
			
			double [] instanceValues = new double[tweetData.numAttributes()];
			instanceValues[0] = tweetData.attribute(0).addStringValue(dataToken.getSentence());
			instanceValues[1] = categories.indexOf(dataToken.getCategory());
			
			Instance dataInstance = new Instance(1.0, instanceValues);
			dataInstance.setWeight(dataToken.getWeight());
			
			tweetData.add(dataInstance);
		}
		
		// 4. Write the data instance object to the save file
		ArffSaver saver = new ArffSaver();
		saver.setInstances(tweetData);
		try {
			saver.setFile(new File(this.fileLocation+this.fileName));
			saver.writeBatch();
		} catch(IOException e) {
			System.err.println("Could not save arff data!");
			System.exit(1);
		}
	}

}
