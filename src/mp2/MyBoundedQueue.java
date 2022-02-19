package mp2;

import java.util.*;

public class MyBoundedQueue<T> {
	private Object instanceLock;
	private ArrayList<T> queue;
	private int capacity;
	
	public MyBoundedQueue(int size) {
		this.instanceLock = new Object();
		this.queue = new ArrayList<T>();
		this.capacity = size;
	}
	
	// TODO: Implement put()
	// In a thread-safe manner:
	//		If there is space in the queue, add value to queue in FIFO order
	//		Otherwise, block operation
	public void put(T val) throws Exception {
		return;
	}
	
	// TODO: Implement take()
	// In a thread-safe manner:
	//		If there are items in the queue, remove and return 
	//			first value from queue in FIFO order
	//		Otherwise, block operation
	public T take() throws Exception {
		return null;
	}
}
