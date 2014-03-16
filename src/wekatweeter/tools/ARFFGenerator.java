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

public class ARFFGenerator {
	
	/**
	 * Default relation value for an instance.
	 */
	private final String DEFAULT_RELATION_NAME = "opinion";
	
	/**
	 * Categories for an instance. The <code>objective</code> tag is counted as <code>neutral</code>.
	 */
	private final String [] CATEGORIES = {"positive","negative","neutral"};
	
	private final String DEFAULT_SAVE_FILE_LOCATION = "./Resources/";
	private final String DEFAULT_SAVE_FILE_NAME = "test.arff";
	
	private String fileName;
	private String fileLocation;
	
	// Constructor
	public ARFFGenerator() {
		this.fileLocation = DEFAULT_SAVE_FILE_LOCATION;
		this.fileName = DEFAULT_SAVE_FILE_NAME;
	}
	
	public ARFFGenerator(String fileName, String fileLocation) {
		this.fileLocation = fileLocation;
		this.fileName = fileName;
	}
	
	public void generateARFFFile(DynamicArray<TweeterToken> data) {
		if(data == null) 
			throw new IllegalArgumentException("null input data provided.");
		generateARFFFile(data, DEFAULT_RELATION_NAME);
	}
	
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
