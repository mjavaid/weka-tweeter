package main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import utils.DynamicArray;
import utils.TweeterToken;
import wekatweeter.tools.ARFFGenerator;
import wekatweeter.tools.PreProcessor;

public class WekaTweeter {
	
	public static void main(String [] args) {
		if(args.length < 1) {
			System.out.println("Error::Usage: java ARFFGenerator.java <data_source_path> [<relation_name>]");
			System.exit(1);
		}
		String dataPath = args[0];
		/*String relationName = DEFAULT_RELATION;
		if(args.length >= 2)
			relationName = args[1];*/
		
		FileInputStream fis = null;
		BufferedReader reader = null;
		
		DynamicArray<TweeterToken> tweeterData = null;
		
		try {
			fis = new FileInputStream(dataPath);
			reader = new BufferedReader(new InputStreamReader(fis));
			PreProcessor preprocessor = new PreProcessor();
			
			/*
			 * Read data from the corpus file and create a data entry for each corpus document
			 */
			String line = reader.readLine();
			tweeterData = new DynamicArray<TweeterToken>();
			while(line != null) {
				String category;
				String [] lineValues = line.split("\t");
				
				category = (lineValues[2].split("\""))[1];
				if(category.equals("objective")) category = "neutral";
				
				String sentence = preprocessor.preprocess(lineValues[3], category);
				double weight = preprocessor.calculateWeight(sentence, category);
				
				TweeterToken token = new TweeterToken(sentence, category, weight);
				tweeterData.insert(token);
				
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
		
		ARFFGenerator generator = new ARFFGenerator();
		generator.generateARFFFile(tweeterData);
	}

}
