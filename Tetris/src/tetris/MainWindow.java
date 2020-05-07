/*

 */
package tetris;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import static javafx.scene.paint.Color.WHITE;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author Brenden Cho
 */
public class MainWindow {

    Audio a = new Audio();
    Pane pane = new Pane();
    Stage stage = new Stage();
    Scene scene = new Scene(pane, 800, 650);
    GameController gc = new GameController(this);
    UserInput ui = new UserInput(gc);
    Text title = new Text("Tetris");
    Text hold = new Text("Hold");
    Text next = new Text("Next");
    Text score = new Text("Score");
    Text scoreValue = new Text("0");
    public MainWindow() {

        stage.setOnCloseRequest(e -> {
            ui.stop();
            a.stop();
            gc.stop();
            System.exit(0);
        });

        title.setFont(Font.font(50));
        title.setFill(WHITE);
        title.setX(310);
        title.setY(45);

        hold.setX(150);
        hold.setY(45);
        hold.setFont(Font.font(20));
        hold.setFill(WHITE);

        score.setX(125);
        score.setY(195);
        score.setFont(Font.font(40));
        score.setFill(WHITE);
        
        scoreValue.setX(165);
        scoreValue.setY(235);
        scoreValue.setFont(Font.font(40));
        scoreValue.setFill(WHITE);
        
        next.setX(555);
        next.setY(45);
        next.setFont(Font.font(20));
        next.setFill(WHITE);

        pane.getChildren().add(scoreValue);
        pane.getChildren().add(score);
        pane.getChildren().add(next);
        pane.getChildren().add(hold);
        pane.getChildren().add(title);

        pane.setStyle("-fx-background-color: #2E3340");

        stage.setTitle("Brenden Cho");
        stage.setHeight(650);
        stage.setWidth(800);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

        a.start();
        gc.start();
        ui.start();

    }

    public void newGame() {

        pane = new Pane();
        scene = new Scene(pane, 800, 650);
        pane.setStyle("-fx-background-color: #2E3340");

        title.setFont(Font.font(50));
        title.setFill(WHITE);
        title.setX(310);
        title.setY(45);

        hold.setX(150);
        hold.setY(45);
        hold.setFont(Font.font(20));
        hold.setFill(WHITE);
        
        score.setX(125);
        score.setY(195);
        score.setFont(Font.font(40));
        score.setFill(WHITE);
        
        scoreValue.setX(165);
        scoreValue.setY(235);
        scoreValue.setFont(Font.font(40));
        scoreValue.setFill(WHITE);
        scoreValue.setText("0");
        
        next.setX(555);
        next.setY(45);
        next.setFont(Font.font(20));
        next.setFill(WHITE);

        pane.getChildren().add(scoreValue);
        pane.getChildren().add(score);
        pane.getChildren().add(next);
        pane.getChildren().add(hold);
        pane.getChildren().add(title);

        gc.stop();
        ui.stop();
        stage.setScene(scene);
        gc = new GameController(this);
        ui = new UserInput(gc);
        gc.start();
        ui.start();
    }

    public Audio getA() {
        return a;
    }

    public void setA(Audio a) {
        this.a = a;
    }

    public Pane getPane() {
        return pane;
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public GameController getGc() {
        return gc;
    }

    public void setGc(GameController gc) {
        this.gc = gc;
    }

    public UserInput getUi() {
        return ui;
    }

    public void setUi(UserInput ui) {
        this.ui = ui;
    }

    public Text getTitle() {
        return title;
    }

    public void setTitle(Text title) {
        this.title = title;
    }

    public Text getHold() {
        return hold;
    }

    public void setHold(Text hold) {
        this.hold = hold;
    }

    public Text getNext() {
        return next;
    }

    public void setNext(Text next) {
        this.next = next;
    }

    public Text getScore() {
        return score;
    }

    public void setScore(Text score) {
        this.score = score;
    }

    public Text getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(Text scoreValue) {
        this.scoreValue = scoreValue;
    }

    
    
    
}
