package utils;

/**
 * Creates a self-sizing array with a generic parameter. This ensures that
 * the array can be created for multiple object types and is not restricted
 * to just one kind of object.
 * 
 * @author Muhammad Sajawal Javaid
 * @version 1.0
 * @param <T> Generic object type to create the array for.
 */
public class DynamicArray<T> {
	
	/**
	 * The default size of the array at creation.
	 */
	private final int DEFAULT_SIZE = 50;
	
	/**
	 * The array of <code>T</code> objects.
	 */
	private T [] array;
	
	/**
	 * The increment value for the array when the capacity is reached.
	 */
	private int increment;
	
	/**
	 * Active size of the array, denoting the number of objects in the array.
	 */
	private int size = 0;
	
	/**
	 * Creates the dynamic array with the default size.
	 */
	@SuppressWarnings("unchecked")
	public DynamicArray() {
		this.array = (T[]) new Object[DEFAULT_SIZE];
		this.increment = DEFAULT_SIZE;
	}
	
	/**
	 * Creates the dynamic array with the provided size.
	 * 
	 * @param size The initial size of the array.
	 */
	@SuppressWarnings("unchecked")
	public DynamicArray(int size) {
		this.array = (T[]) new Object[size];
		this.increment = size;
	}
	
	/**
	 * Returns the active size of the array.
	 * 
	 * @return The active size of the array.
	 */
	public int getSize() {
		return this.size;
	}
	
	/**
	 * Inserts a new object in the array. Increases the array size if necessary.
	 * 
	 * @param value The object to add to the array.
	 */
	public void insert(T value) {
		if(value == null)
			throw new IllegalArgumentException("null argument provided.");
		if(isFull())
			increaseSize();
		this.array[size++] = value;
	}
	
	/**
	 * Gets the value stored at the provided index.
	 * 
	 * @param index The index for the object to be returned.
	 * @return The object stored at the provided index.
	 */
	public T get(int index) {
		if(index >= this.size || index < 0)
			throw new IllegalArgumentException("Invalid array index supplied.");
		return this.array[index];
	}
	
	/**
	 * Sets the value of the element located at the provided index with the new
	 * provided value.
	 * 
	 * @param index The index that is to be set.
	 * @param value The value to set in the provided index.
	 */
	public void set(int index, T value) {
		if(value == null)
			throw new IllegalArgumentException("null argument provided.");
		else if(index >= this.size || index < 0)
			throw new IllegalArgumentException("Invalid array index supplied.");
		this.array[index] = value;
	}
	
	/**
	 * Checks if a value already exists in the array.
	 * 
	 * @param value The value to check for.
	 * @return True if the value exists, False if it does not.
	 */
	public boolean exists(T value) {
		for(int i = 0; i < this.size; i++) {
			if(this.array[i].equals(value))
				return true;
		}
		return false;
	}
	
	/**
	 * The string representation of the <code>DynamicArray</code> object.
	 */
	public String toString() {
		String result = "{";
		for(int i = 0; i < this.size; i++) {
			result += this.array[i];
			if(i != this.size-1) result += ", ";
		}
		result += "}";
		return result;
	}
	
	/**
	 * Checks if the capacity of the array has been reached.
	 * 
	 * @return True if capacity is reached, False if has not.
	 */
	private boolean isFull() {
		return this.size == this.array.length;
	}
	
	/**
	 * Increases the size of the array by its increment value if its
	 * capacity has been reached. 
	 */
	private void increaseSize() {
		@SuppressWarnings("unchecked")
		T [] newArr = (T[]) new Object[this.array.length + this.increment];
		for(int i = 0; i < this.array.length; i++) {
			newArr[i] = this.array[i];
		}
		this.array = newArr;
	}

}
