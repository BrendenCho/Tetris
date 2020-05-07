/*

 */
package tetris;

/**
 *
 * @author Brenden Cho
 */
public class QueueNode {

    Object value;
    QueueNode next;

    public QueueNode(Object o) {
        value = o;
    }

    public QueueNode() {
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public QueueNode getNext() {
        return next;
    }

    public void setNext(QueueNode next) {
        this.next = next;
    }

}
