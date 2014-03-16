package utils;

public class DynamicArray<T> {
	
	private final int DEFAULT_SIZE = 50;
	
	private T [] array;
	
	private int increment;
	
	private int size = 0;
	
	@SuppressWarnings("unchecked")
	public DynamicArray() {
		this.array = (T[]) new Object[DEFAULT_SIZE];
		this.increment = DEFAULT_SIZE;
	}
	
	@SuppressWarnings("unchecked")
	public DynamicArray(int size) {
		this.array = (T[]) new Object[size];
		this.increment = size;
	}
	
	public int getSize() {
		return this.size;
	}
	
	public void insert(T value) {
		if(value == null)
			throw new IllegalArgumentException("null argument provided.");
		if(isFull())
			increaseSize();
		this.array[size++] = value;
	}
	
	public T get(int index) {
		if(index >= this.size || index < 0)
			throw new IllegalArgumentException("Invalid array index supplied.");
		return this.array[index];
	}
	
	public boolean exists(T value) {
		for(int i = 0; i < this.size; i++) {
			if(this.array[i].equals(value))
				return true;
		}
		return false;
	}
	
	public String toString() {
		String result = "{";
		for(int i = 0; i < this.size; i++) {
			result += this.array[i];
			if(i != this.size-1) result += ", ";
		}
		result += "}";
		return result;
	}
	
	private boolean isFull() {
		return this.size == this.array.length;
	}
	
	private void increaseSize() {
		@SuppressWarnings("unchecked")
		T [] newArr = (T[]) new Object[this.array.length + this.increment];
		for(int i = 0; i < this.array.length; i++) {
			newArr[i] = this.array[i];
		}
		this.array = newArr;
	}

}
