package utils;

/**
 * <p>An object that stores a data set instance.</p>
 * 
 * <p>Stores the following information:
 * <ul>
 * 		<li>Sentence</li>
 * 		<li>Category</li>
 * 		<li>Weight</li>
 * </ul>
 * </p>
 * 
 * @author Muhammad Sajawal Javaid
 * @version 1.0
 */
public class TweeterToken {
	
	/**
	 * The default weight of an instance.
	 */
	private final double DEFAULT_WEIGHT = 1.0;
	
	/**
	 * The sentence attribute of an instance.
	 */
	private String sentence;
	
	/**
	 * The category attribute of an instance.
	 */
	private String category;
	
	/**
	 * The weight of an instance.
	 */
	private double weight;
	
	/**
	 * Creates a <code>TweeterToken</code> object with the provided sentence and category
	 * but with the default weight.
	 * 
	 * @param sentence The sentence to create the object with.
	 * @param category The category to create the object with.
	 */
	public TweeterToken(String sentence, String category) {
		this.sentence = sentence;
		this.category = category;
		this.weight = DEFAULT_WEIGHT;
	}
	
	/**
	 * Creates a <code>TweeterToken</code> object with the provided sentence, category
	 * and weight.
	 * 
	 * @param sentence The sentence to create the object with.
	 * @param category The category to create the object with.
	 * @param weight The weight to create the object with.
	 */
	public TweeterToken(String sentence, String category, double weight) {
		this.sentence = sentence;
		this.category = category;
		this.weight = weight;
	}
	
	/**
	 * Sets the sentence of the instance to the provided sentence.
	 * 
	 * @param newSentence The sentence to set for the instance.
	 */
	public void setSentence(String newSentence) {
		this.sentence = newSentence;
	}
	
	/**
	 * Returns the sentence of the instance.
	 * 
	 * @return The sentence of the instance.
	 */
	public String getSentence() {
		return this.sentence;
	}
	
	/**
	 * Returns the category of the instance.
	 * 
	 * @return The category of the instance.
	 */
	public String getCategory() {
		return this.category;
	}
	
	/**
	 * Sets the weight of the instance to the provided weight.
	 * 
	 * @param newWeight The weight to set for the instance.
	 */
	public void setWeight(double newWeight) {
		this.weight = newWeight;
	}
	
	/**
	 * Returns the weight of the instance.
	 * 
	 * @return The weight of the instance.
	 */
	public double getWeight() {
		return this.weight;
	}
	
	/**
	 * String representation of the <code>TweeterToken</code> object.
	 */
	public String toString() {
		String result = "{\n " +
				" \tCategory: " + this.category + 
				",\n \tSentence: " + this.sentence +
				",\n \tWeight: " + this.weight +
				"\n}";
		return result;
	}

}
