package mp2;

import java.util.Optional;
import java.util.Random;

public class Student implements Runnable {
	private MySemaphore sem;
	private MyConcurrentHashmap<TeachingAssistant, Integer> taPopularities;
	private MyBoundedQueue<Student> officeHoursQueue;
	private int approvalRate;
	
	private TeachingAssistant assistingTA;
	
	public Student(MySemaphore sem, MyConcurrentHashmap<TeachingAssistant, Integer>  taPopularities, MyBoundedQueue<Student> officeHoursQueue, int approvalRate) {
		this.sem = sem;
		this.taPopularities = taPopularities;
		this.officeHoursQueue = officeHoursQueue;
		this.approvalRate = approvalRate;
		
		this.assistingTA = null;
	}
	
	
	// Each Student should:
	//		(1) Add themselves to the officeHoursQueue (blocking as necessary if the room is full)
	//    (2) Block until a TeachingAssistant is able to dequeue and assist
	//		(3)	Update the taPopularities map for their assistingTA
	//
	// Use approveTA() to determine if the Student will increase or decrease the assistingTA's score
	// by one point.  
	public void run() {
		try {
			sem.acquire();
		} catch (Exception e) {
			System.out.println("Student "+this.hashCode()+" had error trying to enter A.V. Williams");
			e.printStackTrace();
		}

		try {
			synchronized(this) {
				this.officeHoursQueue.put(this);
				this.wait();
			}
		}
		catch (Exception e) {
			System.out.println("Student "+this.hashCode()+" had error trying to join Office Hours");
			e.printStackTrace();
		}
		
		if (this.assistingTA != null) {
			if (this.approvesTA()) {
				Optional<Integer> score = this.taPopularities.get(this.assistingTA);
				
				if (score.isPresent()) {
					int val = score.get();
					this.taPopularities.put(this.assistingTA, val+1);
				} else {
					this.taPopularities.put(this.assistingTA, 1);
				}	
			}
			else {
				Optional<Integer> score = this.taPopularities.get(this.assistingTA);
				
				if (score.isPresent()) {
					int val = score.get();
					this.taPopularities.put(this.assistingTA, val-1);
				} else {
					this.taPopularities.put(this.assistingTA, -1);
				}	
			}
			 
			System.out.println("Student "+this.hashCode()+" helped by TA "+this.assistingTA.hashCode());
			
		}
		else {
			System.out.println("Student "+this.hashCode()+" claims to have been assisted without TA present.");
		}

		sem.release();
	}
	
	public void setAssistingTA(TeachingAssistant ta) {
		this.assistingTA = ta;
	}
	
	public boolean approvesTA() {
		Random rand = new Random();
		
		if (rand.nextInt(100) <= this.approvalRate) {
			return true;
		}

		return false;
	}
}
