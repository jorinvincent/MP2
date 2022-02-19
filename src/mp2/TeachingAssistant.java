package mp2;

public class TeachingAssistant implements Runnable {	
	private MyBoundedQueue<Student> queue;
	
	
	public TeachingAssistant(MyBoundedQueue<Student> officeHoursQueue) {
		this.queue = officeHoursQueue;
	}
	
	public void run() {
		try {
			while (true) {
				Student student = this.queue.take();
				
				System.out.println("TA "+this.hashCode()+" is assisting Student "+student.hashCode());
				
				student.setAssistingTA(this);
				
				synchronized(student) {
					student.notifyAll();
				} 
			}
		} catch (Exception e) {
			System.out.println("All students have been helped. TA "+this.hashCode()+" ends Office Hours!");
		}	
	}
}
