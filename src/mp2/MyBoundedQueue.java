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
		synchronized(this.instanceLock){
			while (this.queue.size() >= this.capacity) {
				this.instanceLock.wait();
			}

			this.queue.add(val);
			this.instanceLock.notifyAll();
		}
	}
	
	// TODO: Implement take()
	// In a thread-safe manner:
	//		If there are items in the queue, remove and return 
	//			first value from queue in FIFO order
	//		Otherwise, block operation
	public T take() throws Exception {
		synchronized(this.instanceLock){
			while (this.queue.isEmpty()) {
				this.instanceLock.wait();
			}

			T temp = this.queue.remove(0);
			this.instanceLock.notifyAll();
			return temp;
		}
	}
}
