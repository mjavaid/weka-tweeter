

public class DynamicArray {
	
	private final int DEFAULT_SIZE = 50;
	
	private String [] words;
	
	private int increment;
	
	private int size = 0;
	
	public DynamicArray() {
		this.words = new String[DEFAULT_SIZE];
		this.increment = DEFAULT_SIZE;
	}
	
	public DynamicArray(int size) {
		this.words = new String[size];
		this.increment = size;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void insert(String word) {
		if(word == null)
			throw new IllegalArgumentException("null argument provided.");
		if(isFull())
			increaseSize();
		this.words[size++] = word.toLowerCase();
	}
	
	public String get(int index) {
		if(index >= this.size || index < 0)
			throw new IllegalArgumentException("Invalid array index supplied.");
		return this.words[index];
	}
	
	public boolean exists(String word) {
		for(int i = 0; i < this.size; i++) {
			if(this.words[i].equals(word.toLowerCase()))
				return true;
		}
		return false;
	}
	
	public String toString() {
		String result = "{";
		for(int i = 0; i < this.size; i++) {
			result += this.words[i];
			if(i != this.size-1) result += ", ";
		}
		result += "}";
		return result;
	}
	
	private boolean isFull() {
		return this.size == this.words.length;
	}
	
	private void increaseSize() {
		String [] newArr = new String[this.words.length + this.increment];
		for(int i = 0; i < this.words.length; i++) {
			newArr[i] = this.words[i];
		}
		this.words = newArr;
	}

}
