import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

public class ARFFGenerator {
	
	/**
	 * Default relation value for an instance.
	 */
	static final String DEFAULT_RELATION = "opinion";
	
	/**
	 * Categories for an instance. The <code>objective</code> tag is counted as <code>neutral</code>.
	 */
	static final String [] CATEGORIES = {"positive","negative","neutral"};
	
	public static void main(String [] args) {
		/*
		 * Read arguments. Should contain the path of the corpus
		 */
		if(args.length < 1) {
			System.out.println("Error::Usage: java ARFFGenerator.java <data_source_path> [<relation_name>]");
			System.exit(1);
		}
		String dataPath = args[0];
		String relationName = DEFAULT_RELATION;
		if(args.length >= 2)
			relationName = args[1];
		
		// 1. Create a FastVector object to store the attributes (sentence, category)
		FastVector attrs = new FastVector();
		
		// 2. Add attributes
		attrs.addElement(new Attribute("sentence", (FastVector) null));
		FastVector categories = new FastVector();
		for(int i=0; i<CATEGORIES.length; i++)
			categories.addElement(CATEGORIES[i]);
		attrs.addElement(new Attribute("category", categories));
		
		// 3. Create data instance
		Instances tweetData = new Instances(relationName, attrs, 0);
		
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(dataPath);
			reader = new BufferedReader(new InputStreamReader(fis));
			PreProcessor preprocessor = new PreProcessor();
			
			/*
			 * Read data from the corpus file and create a data entry for each corpus document
			 */
			String line = reader.readLine();
			while(line != null){
				String category;
				String [] lineValues = line.split("\t");
				category = (lineValues[2].split("\""))[1];
				double [] dataInstance = new double[tweetData.numAttributes()];
				if(category.equals("objective")) category = "neutral";
				dataInstance[0] = tweetData.attribute(0).addStringValue(preprocessor.preprocess(lineValues[3], category));
				dataInstance[1] = categories.indexOf(category);
				tweetData.add(new Instance(1.0, dataInstance));
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
		
		/*
		 * Write the data instance object to the <code>test.arff</code> file
		 */
		ArffSaver saver = new ArffSaver();
		saver.setInstances(tweetData);
		try {
			saver.setFile(new File("./Resources/test.arff"));
			saver.writeBatch();
		} catch(IOException e) {
			System.err.println("Could not save arff data!");
			System.exit(1);
		}
	}

}
