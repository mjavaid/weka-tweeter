package wekatweeter.tools;

import utils.DynamicArray;
import utils.TweeterToken;
import utils.Utilities;

public class WeightHandler {
	
	public double calcPositiveWeight(String input) {
		double weight = 1.0;
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			if(Utilities.POSITIVE_WORDS.exists(tokens[i])) {
				weight++;
			}
		}
		return weight;
	}
	
	public double calcNegativeWeight(String input) {
		double weight = 1.0;
		String [] tokens = input.split(" ");
		for(int i = 0; i < tokens.length; i++) {
			if(Utilities.NEGATIVE_WORDS.exists(tokens[i])) {
				weight++;
			}
		}
		return weight;
	}
	
	public double calcNeutralWeight(String input) {
		double weight = 1.0;
		double positiveWeight = calcPositiveWeight(input);
		double negativeWeight = calcNegativeWeight(input);
		return weight + Math.abs(positiveWeight - negativeWeight);
	}
	
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
	
	private double getMaxWeight(DynamicArray<TweeterToken> data, String category) {
		double max = 1.0;
		for(int i = 1; i < data.getSize(); i++) {
			double weight = data.get(i).getWeight();
			if(weight > max && data.get(i).getCategory().equals(category))
				max = weight;
		}
		return max;
	}
	
}
