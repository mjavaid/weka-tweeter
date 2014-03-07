import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class ARFFGenerator {
	
	static final String DEFAULT_RELATION = "opinion";
	static final String [] CATEGORIES = {"positive","negative","neutral"};
	
	public static void main(String [] args) {
		// TODO
		if(args.length < 1) {
			System.out.println("Error::Usage: java ARFFGenerator.java <data_source_path> [<relation_name>]");
			System.exit(1);
		}
		String dataPath = args[0];
		String relationName = DEFAULT_RELATION;
		if(args.length >= 2)
			relationName = args[1];
		
		FastVector attrs = new FastVector();
		attrs.addElement(new Attribute("sentence", (FastVector) null));
		FastVector categories = new FastVector();
		for(int i=0; i<CATEGORIES.length; i++)
			categories.addElement(CATEGORIES[i]);
		attrs.addElement(new Attribute("category", categories));
		
		Instances tweetData = new Instances(relationName, attrs, 0);
		
		double [] val1 = new double[tweetData.numAttributes()];
		val1[0] = tweetData.attribute(0).addStringValue("This is a string!");
		val1[1] = categories.indexOf("positive");
		tweetData.add(new Instance(1.0, val1));
		
		FileInputStream fis = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(dataPath);
			reader = new BufferedReader(new InputStreamReader(fis));
			
			String line = reader.readLine();
			while(line != null){
				String [] lineValues = line.split("\t");
				lineValues[2] = (lineValues[2].split("\""))[1];
				double [] dataInstance = new double[tweetData.numAttributes()];
				dataInstance[0] = tweetData.attribute(0).addStringValue(lineValues[3]);
				if(lineValues[2].equals("objective")) dataInstance[1] = categories.indexOf("neutral"); 
				else dataInstance[1] = categories.indexOf(lineValues[2]);
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
		System.out.println(tweetData);
	}

}
