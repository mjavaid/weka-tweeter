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

public class WekaTweeter {
	
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
			 * Read data from the corpus file and create a data entry for each corpus document
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
					weight = weightHandler.calcNeutralWeight(sentence);
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
		
		Utilities.log("Creating ARFF File...", Utilities.NO_NEW_LINE);
		ARFFGenerator generator = new ARFFGenerator();
		generator.generateARFFFile(tweeterData, relationName);
		Utilities.log(" Done.", Utilities.ADD_NEW_LINE);
		
		Utilities.log("ARFF File Created: ["+generator.getFileLocation()+"]", Utilities.ADD_NEW_LINE);
	}

}
