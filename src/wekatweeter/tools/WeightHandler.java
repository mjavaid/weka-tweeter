package wekatweeter.tools;

import utils.DynamicArray;
import utils.TweeterToken;
import utils.Utilities;

/**
 * <p>Weight handler for the ARFF file. Calculates the weights for the tweet data using
 * positive and negative terms present in a data instance.</p>
 * 
 * <p>Provides additional functionalities such as normalizing the data weights.</p>
 * 
 * @author Muhammad Sajawal Javaid
 * @version 1.0
 */
public class WeightHandler {
	
	final double WEIGHT_PER_EXCLAMATION_MARK = 1.0;
	
	/**
	 * Calculates the positive weight of a tweet data instance.
	 * 
	 * @param input A tweet data instance.
	 * @return The weight pf the tweet data instance for a positive category.
	 */
	public double calcPositiveWeight(String input) {
		double weight = 1.0;
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			if(Utilities.POSITIVE_WORDS.exists(tokens[i])) {
				weight++;
			}
		}
		weight += findExclamationMarksWeight(input);
		return weight;
	}
	
	/**
	 * Calculates the negative weight of a tweet data instance.
	 * 
	 * @param input A tweet data instance.
	 * @return The weight of the tweet data instance for a negative category.
	 */
	public double calcNegativeWeight(String input) {
		double weight = 1.0;
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			if(Utilities.NEGATIVE_WORDS.exists(tokens[i])) {
				weight++;
			}
		}
		weight += findExclamationMarksWeight(input);
		return weight;
	}
	
	/**
	 * Calculates the initial weights of the neutral instances provided. The weight is essentially
	 * the deviation of the instance's polarity from a neutral baseline (neither positive nor negative). 
	 * 
	 * @param input A tweet data instance.
	 * @return The deviation of instance's polarity from the neutral baseline.
	 */
	public double calcNeutralDeviation(String input) {
		double weight = 1.0;
		double positiveWeight = calcPositiveWeight(input);
		double negativeWeight = calcNegativeWeight(input);
		return weight + Math.abs(positiveWeight - negativeWeight);
	}
	
	/**
	 * Finalizes the calculation for the weights for neutral category instances. Has to be performed
	 * separately as the instances must have an existing weight associated with them, which is the deviation
	 * from a neutral score.
	 * 
	 * The higher this score (existing weight), the lower the resulting weight would be.
	 * 
	 * @param data Weighted tweet data set.
	 * @return Tweet data set with final neutral instance weights.
	 */
	public DynamicArray<TweeterToken> finalizeNeutralWeights(DynamicArray<TweeterToken> data) {
		String category = "neutral";
		double max = getMaxWeight(data, category);
		for(int i = 0; i < data.getSize(); i++) {
			if(data.get(i).getCategory().equals(category)) {
				TweeterToken token = data.get(i);
				double weight = Math.abs(max - token.getWeight()) + 1;
				token.setWeight(weight);
				data.set(i, token);
			}
		}
		return data;
	}
	
	/**
	 * Normalizes the weights of instances belonging to the specified category by finding the 
	 * maximum weight and dividing all the weights by that weight.
	 * 
	 * @param data Weighted tweet data set.
	 * @param category Category to normalize the weights for.
	 * @return The normalized weighted tweet data set.
	 */
	public DynamicArray<TweeterToken> normalizeWeights(DynamicArray<TweeterToken> data, String category) {
		double max = getMaxWeight(data, category);
		for(int i = 0; i < data.getSize(); i++) {
			if(data.get(i).getCategory().equals(category)) {
				TweeterToken token = data.get(i);
				double weight = token.getWeight() / max;
				token.setWeight(weight);
				data.set(i, token);
			}
		}
		return data;
	}
	
	/**
	 * Returns the maximum weight from the data set provided for the specified category.
	 * 
	 * @param data Weighted tweet data set.
	 * @param category Category to find the maximum weight for amongst the three possible categories.
	 * @return The maximum weight in the data set for the specified category.
	 */
	private double getMaxWeight(DynamicArray<TweeterToken> data, String category) {
		double max = 1.0;
		for(int i = 1; i < data.getSize(); i++) {
			double weight = data.get(i).getWeight();
			if(weight > max && data.get(i).getCategory().equals(category))
				max = weight;
		}
		return max;
	}
	
	private double findExclamationMarksWeight(String sentence) {
		int numExclamations = 0;
		for(int i=0; i < sentence.length();) {
			if(sentence.charAt(i) == '!') {
				int temp = 1;
				while(++i < sentence.length()) {
					if(sentence.charAt(i) == '!') {
						temp += 1;
					} else {
						i++;
						break;
					}
				}
				if(numExclamations < temp) numExclamations = temp;
			} else i++;
		}
		return numExclamations * WEIGHT_PER_EXCLAMATION_MARK;
	}
	
}
