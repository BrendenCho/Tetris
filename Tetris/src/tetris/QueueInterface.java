/*

 */
package tetris;

/**
 *
 * @author Brenden Cho
 */
public interface QueueInterface {

public void enqueue(Object o);

public Object dequeue() throws QueueException;

public Object peek() throws QueueException;

public boolean isEmpty();

public void dequeueAll();
    
}
