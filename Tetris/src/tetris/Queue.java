/*

 */
package tetris;

/**
 *
 * @author Brenden Cho
 */
public class Queue implements QueueInterface {

QueueNode head;
QueueNode tail;

    @Override
    public void enqueue(Object o) {
        if(isEmpty() == true){
        head = new QueueNode(o);
        tail = head;
        }else{
        QueueNode qn = new QueueNode(o);
        tail.next = qn;
        tail = qn;
        }
    }

    @Override
    public Object dequeue() {
       if(isEmpty() == true){
       throw new QueueException("Empty Queue");
       }else{QueueNode n = head;
       head = head.next;
       return n.value;
       }
    }

    @Override
    public Object peek() {
        if(isEmpty() == true){
        throw new QueueException("Empty Queue");
        }else{
        return head.value;
        }
    }

    @Override
    public boolean isEmpty() {
        if(head == null){
        return true;
        }else{
        return false;
        }
    }

    @Override
    public void dequeueAll() {
        head = null;
        tail = null;
    }
    
    public void print(QueueNode node){
    if(node != null){
        System.out.print(node.value.toString() + " ");
        print(node.next);
        
        if(node == tail){
            System.out.print("\n");
        }
        
    }
    }


}
