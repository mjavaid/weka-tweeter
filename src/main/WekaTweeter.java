package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import utils.DynamicArray;
import utils.TweeterToken;
import utils.Utilities;
import wekatweeter.tools.ARFFGenerator;
import wekatweeter.tools.PreProcessor;
import wekatweeter.tools.WeightHandler;

/**
 * <p>The main gateway file to the <code><strong>weka-tweeter</strong></code> assignment.</p>
 * 
 * <p>Hosts the <code>main</code> method that is used to parse and process the 
 * tweeter data set and generate a <code>.arff</code> that can be used by Weka 
 * for data classification.</p>
 * 
 * <p>To execute this method, make sure that you are in the <code>weka-tweeter
 * /bin/</code> directory and then run the following command from the console:
 * </p>
 * 
 * <ul>
 * 		<li>
 * 			<strong>Mac OSX & Linux:</strong><br/>
 * 			<ul><li>
 * 			<code>
 * java -cp <strong>/Path/to/</strong>weka.jar:<strong>/Path/to/</strong>snowball-20051019.jar:.
 *  ./main/WekaTweeter <strong>&lt;data_source_path&gt;</strong> <strong>[&lt;relation_name&gt;]</strong>
 * 			</code>
 * 			</li></ul>
 * 		</li>
 * 
 * 		<li>
 * 			<strong>Windows:</strong><br/>
 * 			<ul><li>
 * 			<code>
 * java -cp <strong>/Path/to/</strong>weka.jar;<strong>/Path/to/</strong>snowball-20051019.jar;.
 *  ./main/WekaTweeter <strong>&lt;data_source_path&gt;</strong> <strong>[&lt;relation_name&gt;]</strong>
 * 			</code>
 * 			</li></ul>
 * 		</li>
 * </ul>
 * 
 * @author Muhammad Sajawal Javaid
 * @version 1.0
 */
public class WekaTweeter {
	
	/**
	 * Gateway method to the <code><strong>weka-tweeter</strong></code> assignment.
	 * 
	 * @param args The path to the tweeter data and an optional relation name.
	 */
	public static void main(String [] args) {
		if(args.length < 1) {
			System.out.println("Error::Usage: java ARFFGenerator.java <data_source_path> [<relation_name>]");
			System.exit(1);
		}
		String dataPath = args[0];
		String relationName = ARFFGenerator.DEFAULT_RELATION_NAME;
		if(args.length >= 2)
			relationName = args[1];
		
		Utilities utils = new Utilities();
		utils.initializeUtilities();
		
		Utilities.log("Using Data File: ["+dataPath+"] with Relation Name: ["+relationName+"]", Utilities.ADD_NEW_LINE);
		
		FileInputStream fis = null;
		BufferedReader reader = null;
		
		DynamicArray<TweeterToken> tweeterData = null;

		WeightHandler weightHandler = new WeightHandler();
		
		try {
			fis = new FileInputStream(dataPath);
			reader = new BufferedReader(new InputStreamReader(fis));
			PreProcessor preprocessor = new PreProcessor();
			
			/*
			 * Read data from the corpus file and create a data entry for each corpus document.
			 */
			String line = reader.readLine();
			tweeterData = new DynamicArray<TweeterToken>();
			
			Utilities.log("Reading Tweeter Data...", Utilities.NO_NEW_LINE);
			
			while(line != null) {
				String category;
				String [] lineValues = line.split("\t");
				
				category = (lineValues[2].split("\""))[1];
				if(category.equals("objective")) category = "neutral";
				
				String sentence = preprocessor.preprocess(lineValues[3], category);
				
				double weight = 1.0;
				if(category.equals("positive")) {
					weight = weightHandler.calcPositiveWeight(sentence);
				} else if(category.equals("negative")) {
					weight = weightHandler.calcNegativeWeight(sentence);
				} else if(category.equals("neutral")) {
					weight = weightHandler.calcNeutralDeviation(sentence);
				}
				TweeterToken token = new TweeterToken(sentence, category, weight);
				tweeterData.insert(token);
				
				line = reader.readLine();
			}
			
			Utilities.log(" Done.", Utilities.ADD_NEW_LINE);
			
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
		 * Calculate weights for all the data instances.
		 */
		Utilities.log("Calculating weights...", Utilities.ADD_NEW_LINE);
		
		tweeterData = weightHandler.finalizeNeutralWeights(tweeterData);
		
		Utilities.log("> Positive Instances...", Utilities.NO_NEW_LINE);
		tweeterData = weightHandler.normalizeWeights(tweeterData, "positive");
		Utilities.log(" Done.", Utilities.ADD_NEW_LINE);
		
		Utilities.log("> Negative Instances...", Utilities.NO_NEW_LINE);
		tweeterData = weightHandler.normalizeWeights(tweeterData, "negative");
		Utilities.log(" Done.", Utilities.ADD_NEW_LINE);
		
		Utilities.log("> Neutral Instances...", Utilities.NO_NEW_LINE);
		tweeterData = weightHandler.normalizeWeights(tweeterData, "neutral");
		Utilities.log(" Done.", Utilities.ADD_NEW_LINE);
		
		/*
		 * Create the output .arff file.
		 */
		Utilities.log("Creating ARFF File...", Utilities.NO_NEW_LINE);
		ARFFGenerator generator = new ARFFGenerator();
		generator.generateARFFFile(tweeterData, relationName);
		Utilities.log(" Done.", Utilities.ADD_NEW_LINE);
		
		Utilities.log("ARFF File Created: ["+generator.getFileLocation()+"]", Utilities.ADD_NEW_LINE);
	}

}
