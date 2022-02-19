package mp2;

public class Main {
	public static void main(String[] args) {
		int numStudents = 500;
		int numTAs = 10;
		int approvalRate = 90;
		int buildingCapacity = 50;
		int officeHoursCapacity = 20;
		
		Thread[] students = new Thread[numStudents];
		Thread[] tas = new Thread[numTAs];
		
		MySemaphore sem = new MySemaphore(buildingCapacity);
		MyBoundedQueue<Student> queue = new MyBoundedQueue<Student>(officeHoursCapacity);
		MyConcurrentHashmap<TeachingAssistant, Integer> scoreboard = new MyConcurrentHashmap<TeachingAssistant, Integer>(16);
		
		for (int i = 0; i < numStudents; i++) {
			students[i] = new Thread(new Student(sem, scoreboard, queue, approvalRate));
			students[i].start();
		}
		
		for (int i = 0; i < numTAs; i++) {
			tas[i] = new Thread(new TeachingAssistant(queue));
			tas[i].start();
			
		}
		
		for (int i = 0; i < numStudents; i++) {
			try {
				students[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i < numTAs; i++) {
			tas[i].interrupt();
		}
		for (int i = 0; i < numTAs; i++) {
			try {
				tas[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (TeachingAssistant ta : scoreboard.keySet()) {
			System.out.println(ta.hashCode() + ": " + scoreboard.get(ta).get());
		}
	}

}
