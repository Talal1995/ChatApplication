package gruppuppgift;

import java.util.LinkedList;

/*
 * Synkroniserad objektsamling för Message-objekt. 
 */

public class MessageBuffer<T> {
private LinkedList<T> buffer = new LinkedList<T>();
	
	public synchronized void put(T obj) {
		buffer.addLast(obj);
		notifyAll();
	}
	
	public synchronized T get() throws InterruptedException {
		while(buffer.isEmpty()) {
			wait();
		}
		return buffer.removeFirst();
	}
	
	public int size() {
		return buffer.size();
	}
}
