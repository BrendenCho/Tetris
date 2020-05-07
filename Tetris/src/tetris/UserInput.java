/*

 */
package tetris;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Brenden Cho
 */
public class UserInput extends Thread{
boolean open = true;
boolean pause = false;
GameController gc;
@Override
public void run(){
super.setName("UserInput Thread");

setListeners();

while(open == true){
}

}

public UserInput(GameController gc){
this.gc = gc;

}



public void setListeners(){

gc.mw.scene.setOnKeyPressed(new EventHandler<KeyEvent>(){
    
    @Override
    public void handle(KeyEvent k){
    
    KeyCode key = k.getCode();
    String keyName = key.getName();
     
    
   if(keyName.equals("Esc")){ 
      
    if(pause == false){
    gc.suspend();
    pause = true;
    }else{   
    gc.resume();
    pause = false;   
    }   
       
       
   }else{    
   Platform.runLater(new Runnable(){
        
    public void run(){
    gc.shift(keyName);
    }
    
    
    
    });
    
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
           
        }
    
     
    }
    }

});

}

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public GameController getGc() {
        return gc;
    }

    public void setGc(GameController gc) {
        this.gc = gc;
    }







    
}
