package mp2;

public class MySemaphore {
	private int permits;
	private Object instanceLock;
	
	public MySemaphore(int permits) {
		this.permits = permits;
		this.instanceLock = new Object();
	}
	
	// TODO: Implement acquire(). 
	// In a thread-safe manner: 
	// 		If there are a non-zero amount of permits, decrement the permit count
	// 		If there are zero or less permits, block
	public void acquire() throws Exception {
			synchronized(this.instanceLock){
				while (this.permits <= 0) {
					this.instanceLock.wait();
				}

				this.permits--;
			}
	}
	
	// TODO: Implement release()
	// In a thread-safe manner:
	//		Increment the permit count
	public void release() {
			synchronized(this.instanceLock){
				this.permits++;
				this.instanceLock.notify();
			}
	}
	
	public int getPermits() {
		return this.permits;
	}
}
