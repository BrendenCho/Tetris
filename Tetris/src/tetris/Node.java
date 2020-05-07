package tetris;

import javafx.scene.shape.Rectangle;

/**
 *
 * @author Brenden Cho
 */
public class Node {


    int x;
    int y;
    int state = 0;
    Rectangle r;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Node() {

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Rectangle getR() {
        return r;
    }

    public void setR(Rectangle r) {
        this.r = r;
    }

 
 
    
}
