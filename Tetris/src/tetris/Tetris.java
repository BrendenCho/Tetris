package tetris;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Brenden Cho 
 */
public class Tetris extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        MainWindow mw = new MainWindow();
       
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
