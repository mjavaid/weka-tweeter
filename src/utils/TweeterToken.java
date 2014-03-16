package utils;

public class TweeterToken {
	
	private final double DEFAULT_WEIGHT = 1.0;
	
	private String sentence;
	
	private String category;
	
	private double weight;
	
	public TweeterToken(String sentence, String category) {
		this.sentence = sentence;
		this.category = category;
		this.weight = DEFAULT_WEIGHT;
	}
	
	public TweeterToken(String sentence, String category, double weight) {
		this.sentence = sentence;
		this.category = category;
		this.weight = weight;
	}
	
	public void setSentence(String newSentence) {
		this.sentence = newSentence;
	}
	
	public String getSentence() {
		return this.sentence;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public void setWeight(double newWeight) {
		this.weight = newWeight;
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public String toString() {
		String result = "{\n " +
				" \tCategory: " + this.category + 
				",\n \tSentence: " + this.sentence +
				",\n \tWeight: " + this.weight +
				"\n}";
		return result;
	}
	
	public static void main(String [] args) {
		TweeterToken a = new TweeterToken("This is a sentence", "neutral");
		System.out.println(a.getCategory());
		System.out.println(a.getSentence());
		a.setSentence("This is a new sentence!");
		System.out.println(a.getSentence());
		System.out.println(a);
	}

}
