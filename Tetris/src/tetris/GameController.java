/*

 */
package tetris;

import java.util.Random;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.*;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Brenden Cho
 */
// States: 0 unoccupied square 1 occupied square 2 active piece 
// Pieces: 0 i 1 o 2 j 3 l 4 t 5 s 6z
public class GameController extends Thread {

    Random r = new Random();
    boolean game = true;
    MainWindow mw;
    int[] nextArr = new int[3];
    int X = 0;
    int Y = 25;
    Node[][] board = new Node[25][10];
    Node[][] hold = new Node[4][4];
    Node[][] next = new Node[4][4];
    Node[][] next1 = new Node[4][4];
    Node[][] next2 = new Node[4][4];
    Node[] activeArr = new Node[4];
    Node[] ghostArr = new Node[4];
    Pane pane;
    final int WIDTH = 25;
    final int HEIGHT = WIDTH;
    Queue moveQueue = new Queue();
    Queue ghostQueue = new Queue();
    final int START_ROW = 5;
    final int NUM_ROWS = 25;
    final int NUM_COLUMNS = 10;
    boolean play = true;
    int orientation = 1;
    int id;
    boolean activePiece = true;
    Color tile = Color.web("rgb(0,0,0)");
    Color tileOutline = Color.web("rgb(255,255,255)");
    int rawScore = 0;
    int score = 1;
    int delayCounter = 0;
    int delay = 500;
    boolean pause = false;
    PauseTransition pt = new PauseTransition();
    int ghostCounter;
    int holdId;
    boolean firstHold = true;
    boolean validHold;
    boolean ghost = true;
    boolean slam = true;
     boolean created = false;
     
    public GameController(MainWindow mw) {
        this.mw = mw;
        pane = mw.getPane();

    }

    @Override
    public void run() {
        super.setName("Game Controller Thread");

        setUpBoard();
        setUpHold();
        setUpNext(next, 25);
        setUpNext(next1, 150);
        setUpNext(next2, 275);

        for (int x = 0; x < 3; x++) {

            nextArr[x] = r.nextInt(7);

        }

        spawn();
       
               pt.setDuration(Duration.millis(delay));

        pt.setOnFinished(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e) {
                ghost();
                gravity();
                checkRows();
                checkLoss();
                modifyDelay();
                spawn();
            }

        });
       
        
        while (play == true) {
       
            if (game == true) {
            
          
                
                pt.play();
            } else if (created == false){
                    created = true;
                Platform.runLater(new Runnable() {

                    public void run() {
                        Alert a = new Alert(AlertType.INFORMATION);
                    
                        a.setOnCloseRequest(e ->{
                        mw.newGame();
                        });
                       
                        a.setTitle("Brenden Cho");
                        a.setContentText("Game Over. You scored:" + rawScore);
                        a.show();
                    }

                });

                

            }
        }

    }

    public void hold() {

        if (firstHold == true) {

            holdId = id;
            piece(hold, holdId, 1, 0);
            clearActive();
            spawn();
            firstHold = false;
            validHold = false;

        } else if (validHold == true) {
            int i = holdId;
            holdId = id;
            clearHold(hold);
            clearActive();
            spawn(i);
            piece(hold, holdId, 1, 0);
            validHold = false;

        }

    }

    public void clearAll(){
    
    for(int x = 0; x < NUM_ROWS;x++){
    
        for(int y = 0; y < NUM_COLUMNS;y++){
            board[x][y].setState(0);
        
        
        }
    
    
    }
    
    }
    
    public void clearActive() {

        for (int x = 0; x < NUM_ROWS; x++) {

            for (int y = 0; y < NUM_COLUMNS; y++) {
                if (board[x][y].getState() == 2) {

                    board[x][y].setState(0);

                }

            }

        }
        clean();

    }

    public void clearHold(Node[][] arr) {

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                final Rectangle r = arr[x][y].getR();
                Platform.runLater(new Runnable() {

                    public void run() {
                        r.setFill(tile);
                        r.setStroke(tileOutline);
                    }

                });

            }

        }

    }

    public void next() {

        clearHold(next);
        clearHold(next1);
        clearHold(next2);

        nextArr[0] = nextArr[1];
        nextArr[1] = nextArr[2];
        nextArr[2] = r.nextInt(7);

        piece(next, nextArr[0], 1, 0);
        piece(next1, nextArr[1], 1, 0);
        piece(next2, nextArr[2], 1, 0);

    }

    public void slam() {
      if(slam == true){
        int num = id;
        for (int a = 0; a < 4; a++) {
            ghostArr[a].setState(1);
            final Rectangle r = ghostArr[a].getR();
            Platform.runLater(new Runnable() {
                public void run() {
                    r.setFill(getColor(num));
                    r.setStroke(BLACK);
                }

            });

        }

        for (int x = 0; x < NUM_ROWS; x++) {
            for (int y = 0; y < NUM_COLUMNS; y++) {
                if (board[x][y].getState() == 2) {
                    board[x][y].setState(0);

                    final Rectangle r = board[x][y].getR();
                    Platform.runLater(new Runnable() {
                        public void run() {
                            r.setFill(tile);
                            r.setStroke(tileOutline);
                        }

                    });

                }

            }
        }
        clean();
        ghost = false;
        slam = false;
      }
    }

    public void clean() {

        for (int a = 0; a < NUM_ROWS; a++) {

            for (int b = 0; b < NUM_COLUMNS; b++) {

                if (board[a][b].getState() == 0) {
                    Rectangle r = board[a][b].getR();
                    Platform.runLater(new Runnable() {
                        public void run() {
                            r.setFill(tile);
                            r.setStroke(tileOutline);
                        }

                    });

                }

            }

        }
    }

    public Color getColor(int id) {

        switch (id) {

            case 0:
                return CYAN;
            case 1:
                return YELLOW;
            case 2:
                return BLUE;
            case 3:
                return ORANGE;
            case 4:
                return PURPLE;
            case 5:
                return LIME;
            case 6:
                return RED;
            default:
                return RED;
        }

    }

    public void ghost() {
        if (ghost == true) {
            if (ghostArr[0] != null) {

                for (int z = 0; z < 4; z++) {

                    if (ghostArr[z].getState() != 1 && ghostArr[z].getState() != 2) {
                        final Rectangle r = ghostArr[z].getR();
                        Platform.runLater(new Runnable() {

                            public void run() {
                                r.setFill(tile);
                                r.setStroke(tileOutline);

                            }

                        });

                    }

                }

            }

            int index = 0;
            for (int x = NUM_ROWS - 1; x >= 0; x--) {
                for (int y = 0; y < NUM_COLUMNS; y++) {
                    if (board[x][y].getState() == 2) {
                        activeArr[index] = board[x][y];
                        index++;
                    }
                }
            }

            boolean conflict = false;
            int count = 1;

            while (conflict == false) {

                for (int i = 0; i < 4; i++) {
                    if (activeArr[i].getX() + count >= NUM_ROWS) {
                        int a = activeArr[i].getX();
                        count = 23 - a;
                        conflict = true;
                    } else if (board[activeArr[i].getX() + count][activeArr[i].getY()].getState() == 1) {
                        count -= 2;
                        conflict = true;
                    }
                }

                count++;

            }

            for (int a = 0; a < 4; a++) {
                ghostArr[a] = board[activeArr[a].getX() + count][activeArr[a].getY()];

                if (ghostArr[a].getState() != 1 && ghostArr[a].getState() != 2) {

                    final Rectangle r = ghostArr[a].getR();

                    Platform.runLater(new Runnable() {

                        public void run() {
                            r.setStroke(getColor(id));
                        }

                    });
                }

            }
        
         if(slam == false){
            slam = true;
            }
        
        }
    }

    public void modifyDelay() {

        if (score % 5 == 0 && score != 0) {
            delayCounter++;
            score++;
        }

        pt.setDuration(Duration.millis(delay - 50 * (delayCounter)));

    }

    public void setUpNext(Node[][] arr, int startB) {
        int a = 525;
        int b = startB;
        for (int x = 0; x < 4; x++) {
            a = 525;
            b += HEIGHT;
            for (int y = 0; y < 4; y++) {

                arr[x][y] = new Node(x, y);
                arr[x][y].setR(new Rectangle());
                arr[x][y].getR().setWidth(WIDTH);
                arr[x][y].getR().setHeight(HEIGHT);
                arr[x][y].getR().setFill(tile);
                arr[x][y].getR().setStroke(tileOutline);
                arr[x][y].getR().setX(a);
                arr[x][y].getR().setY(b);
                a += WIDTH;

                final Pane p = pane;
                final Rectangle r = arr[x][y].getR();

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        p.getChildren().add(r);
                    }

                });

            }

        }

    }

    public void setUpHold() {
        int a = 125;
        int b = 25;

        for (int x = 0; x < 4; x++) {
            a = 125;
            b += HEIGHT;
            for (int y = 0; y < 4; y++) {

                hold[x][y] = new Node(x, y);
                hold[x][y].setR(new Rectangle());
                hold[x][y].getR().setWidth(WIDTH);
                hold[x][y].getR().setHeight(HEIGHT);
                hold[x][y].getR().setFill(tile);
                hold[x][y].getR().setStroke(tileOutline);
                hold[x][y].getR().setX(a);
                hold[x][y].getR().setY(b);
                a += WIDTH;

                final Pane p = pane;
                final Rectangle r = hold[x][y].getR();

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        p.getChildren().add(r);
                    }

                });

            }

        }

    }

    public void setUpBoard() {

        for (int a = 0; a < START_ROW; a++) {
            for (int b = 0; b < NUM_COLUMNS; b++) {
                board[a][b] = new Node(a, b);
                board[a][b].setState(0);
                board[a][b].setR(new Rectangle());
            }
        }

        for (int x = START_ROW; x < NUM_ROWS; x++) {
            X = 250;
            Y += HEIGHT;
            for (int y = 0; y < NUM_COLUMNS; y++) {
                board[x][y] = new Node(x, y);
                board[x][y].setState(0);
                board[x][y].setR(new Rectangle());
                board[x][y].getR().setWidth(WIDTH);
                board[x][y].getR().setHeight(HEIGHT);
                board[x][y].getR().setX(X);
                board[x][y].getR().setY(Y);
                board[x][y].getR().setStroke(tileOutline);
                board[x][y].getR().setFill(tile);
                X = X + WIDTH;

                final Pane p = pane;
                final Rectangle r = board[x][y].getR();

                Platform.runLater(new Runnable() {

                    @Override
                    public void run() {

                        p.getChildren().add(r);
                    }

                });

            }

        }

    }

    public void checkRows() {
        boolean removeRow = true;

        for (int x = NUM_ROWS - 1; x > START_ROW; x--) {
            removeRow = true;
            for (int y = 0; y < NUM_COLUMNS; y++) {
                if (board[x][y].getState() != 1) {
                    removeRow = false;
                }

            }

            if (removeRow == true) {
                clean();
                score++;
                rawScore++;
                pull(x);
                
                Platform.runLater(new Runnable(){
                
                public void run(){
                
                mw.getScoreValue().setText(Integer.toString(rawScore));
                
                
                }
                
                 
                });

            }

        }

    }

    public void pull(int row) {

        for (int x = row; x > 4; x--) {

            for (int y = 0; y < NUM_COLUMNS; y++) {

                if (x == 5) {

                    if (board[x][y].getState() != 2) {
                        board[x][y].setState(0);

                        final Rectangle r = board[x][y].getR();
                        Platform.runLater(new Runnable() {

                            public void run() {
                                r.setFill(tile);
                                r.setStroke(tileOutline);
                            }

                        });
                    }
                } else {
                    if (board[x][y].getState() != 2 && board[x - 1][y].getState() != 2) {
                        board[x][y].setState(board[x - 1][y].getState());
                        board[x - 1][y].setState(0);
                        final Rectangle r = board[x][y].getR();
                        final Rectangle r1 = board[x - 1][y].getR();
                        Platform.runLater(new Runnable() {

                            public void run() {
                                r.setFill(r1.getFill());
                                r1.setFill(tile);
                                r.setStroke(r1.getStroke());
                                r1.setStroke(tileOutline);
                            }

                        });

                    }
                }

            }

        }
    }

    public void spawn(int num) {

        piece(board, num, 3, 4);
        orientation = 1;
        id = num;

        if (validHold == false) {
            validHold = true;
        }

    }

    public void spawn() {
        boolean piece = false;
        for (int x = 0; x < NUM_ROWS; x++) {

            for (int y = 0; y < NUM_COLUMNS; y++) {

                if (board[x][y].getState() == 2) {
                    piece = true;

                }

            }

        }

        activePiece = piece;

        if (activePiece == false) {

            int num = nextArr[0];
            next();
            piece(board, num, 3, 4);
            orientation = 1;
            id = num;

            if (validHold == false) {
                validHold = true;
            }
            
            if(ghost == false){
            ghost = true;
            }
            
           
        }

    }

    public void piece(Node[][] arr, int pieceID, int startX, int startY) {

        switch (pieceID) {

            case 0:
                for (int x = 0; x < 4; x++) {
                    arr[startX][x + startY].setState(2);
                    final Rectangle r = arr[startX][x + startY].getR();
                    Platform.runLater(new Runnable() {
                        public void run() {
                            r.setFill(CYAN);
                            r.setStroke(BLACK);
                        }
                    });
                }
                break;
            case 1:
                int counter = startY;
                int xC = startX;

                for (int x = 0; x < 4; x++) {

                    if (x == 2) {
                        counter = startY;
                        xC++;
                    }
                    if (x == 1 || x == 3) {
                        counter++;
                    }

                    arr[xC][counter].setState(2);
                    final Rectangle r = arr[xC][counter].getR();
                    Platform.runLater(new Runnable() {

                        public void run() {
                            r.setFill(YELLOW);
                            r.setStroke(BLACK);
                        }

                    });

                }
                break;
            case 2:
                int Xc = startX;
                int Yc = startY;
                for (int x = 0; x < 4; x++) {

                    if (x == 0) {

                    } else {
                        Xc = startX + 1;
                        Yc = startY + x - 1;
                    }

                    arr[Xc][Yc].setState(2);
                    final Rectangle r = arr[Xc][Yc].getR();
                    Platform.runLater(new Runnable() {

                        public void run() {
                            r.setFill(BLUE);
                            r.setStroke(BLACK);

                        }

                    });

                }
                break;
            case 3:
                int X = startX;
                int Y = startY + 3;

                for (int x = 0; x < 4; x++) {

                    if (x < 3) {
                        Y--;
                    } else {
                        X++;
                    }
                    arr[X][Y].setState(2);
                    final Rectangle r = arr[X][Y].getR();
                    Platform.runLater(new Runnable() {

                        public void run() {
                            r.setFill(ORANGE);
                            r.setStroke(BLACK);

                        }

                    });
                }
                break;
            case 4:
                int a = startX;
                int b = startY;

                for (int x = 0; x < 4; x++) {

                    if (x == 3) {
                        a++;
                    }

                    if (x < 3 && x > 0) {
                        b++;
                    } else if (x == 3) {
                        b = startY + 1;
                    }

                    arr[a][b].setState(2);
                    final Rectangle r = arr[a][b].getR();
                    Platform.runLater(new Runnable() {

                        public void run() {
                            r.setFill(PURPLE);
                            r.setStroke(BLACK);

                        }

                    });

                }
                break;
            case 5:
                int q = startX + 1;
                int c = startY;

                for (int x = 0; x < 4; x++) {
                    if (x == 1) {
                        c++;
                    }
                    if (x == 2) {
                        q--;
                    }
                    if (x == 3) {
                        c++;
                    }
                    arr[q][c].setState(2);
                    final Rectangle r = arr[q][c].getR();
                    Platform.runLater(new Runnable() {

                        public void run() {
                            r.setFill(LIME);
                            r.setStroke(BLACK);

                        }

                    });
                }
                break;
            case 6:
                int d = startX;
                int e = startY;

                for (int x = 0; x < 4; x++) {

                    if (x == 2) {
                        d++;
                    }

                    if (x == 1 || x == 3) {
                        e++;
                    }

                    arr[d][e].setState(2);
                    final Rectangle r = arr[d][e].getR();
                    Platform.runLater(new Runnable() {

                        public void run() {
                            r.setFill(RED);
                            r.setStroke(BLACK);

                        }

                    });

                }

                break;

        }

    }

    public void gravity() {
        boolean go = true;

        for (int x = NUM_ROWS - 1; x > 0; x--) {
            for (int y = 0; y < NUM_COLUMNS; y++) {

                if (board[x - 1][y].getState() == 2 && board[x][y].getState() == 0) {

                } else if (board[x - 1][y].getState() == 2 && board[x][y].getState() == 1) {
                    go = false;
                    for (int a = NUM_ROWS - 1; a >= 0; a--) {
                        for (int b = 0; b < NUM_COLUMNS; b++) {
                            if (board[a][b].getState() == 2) {
                                board[a][b].setState(1);
                            }

                        }

                    }

                } else if (board[NUM_ROWS - 1][y].getState() == 2) {
                    for (int a = 0; a < NUM_ROWS; a++) {
                        for (int b = 0; b < NUM_COLUMNS; b++) {
                            if (board[a][b].getState() == 2) {
                                board[a][b].setState(1);
                            }

                        }

                    }

                }

            }

        }

        if (go == true) {
            for (int x = NUM_ROWS - 1; x > 0; x--) {
                for (int y = 0; y < NUM_COLUMNS; y++) {
                    if (board[x - 1][y].getState() == 2 && board[x][y].getState() == 0) {

                        board[x][y].setState(2);
                        board[x - 1][y].setState(0);
                        final Rectangle r = board[x][y].getR();
                        final Rectangle r1 = board[x - 1][y].getR();
                        Platform.runLater(new Runnable() {

                            public void run() {
                                r.setFill(r1.getFill());
                                r1.setFill(tile);
                                r.setStroke(r1.getStroke());
                                r1.setStroke(tileOutline);
                            }

                        });
                    }
                }
            }
        }

    }

    public void printBoard() {
        System.out.println("");
        System.out.println("Board");
        for (int x = 0; x < NUM_ROWS; x++) {
            System.out.println("");
            for (int y = 0; y < NUM_COLUMNS; y++) {
                System.out.print(board[x][y].getState() + " ");
            }

        }
    }

    public void shift(String key) {
        boolean conflict = false;
        if (key.equals("Left")) {

            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (y - 1 >= 0 && board[x][y].getState() == 2 && (board[x][y - 1].getState() == 0 || board[x][y - 1].getState() == 2)) {
                        moveQueue.enqueue(board[x][y]);
                    } else if (board[x][y].getState() == 2) {
                        conflict = true;
                    }

                }
            }

            if (conflict == false) {
                while (moveQueue.isEmpty() != true) {

                    Node n = (Node) moveQueue.peek();
                    board[n.getX()][n.getY() - 1].setState(2);
                    board[n.getX()][n.getY()].setState(0);

                    Platform.runLater(new Runnable() {

                        public void run() {
                            board[n.getX()][n.getY() - 1].getR().setFill(board[n.getX()][n.getY()].getR().getFill());
                            board[n.getX()][n.getY()].getR().setFill(tile);
                            board[n.getX()][n.getY() - 1].getR().setStroke(board[n.getX()][n.getY()].getR().getStroke());
                            board[n.getX()][n.getY()].getR().setStroke(tileOutline);
                        }

                    });
                    moveQueue.dequeue();
                }
            } else {
                moveQueue.dequeueAll();
            }

        } else if (key.equals("Right")) {

            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 9; y >= 0; y--) {

                    if (y + 1 < NUM_COLUMNS && board[x][y].getState() == 2 && (board[x][y + 1].getState() == 0 || board[x][y + 1].getState() == 2)) {
                        moveQueue.enqueue(board[x][y]);
                    } else if (board[x][y].getState() == 2) {
                        conflict = true;
                    }

                }
            }

            if (conflict == false) {
                while (moveQueue.isEmpty() != true) {

                    Node n = (Node) moveQueue.peek();
                    board[n.getX()][n.getY() + 1].setState(2);
                    board[n.getX()][n.getY()].setState(0);

                    Platform.runLater(new Runnable() {

                        public void run() {
                            board[n.getX()][n.getY() + 1].getR().setFill(board[n.getX()][n.getY()].getR().getFill());
                            board[n.getX()][n.getY()].getR().setFill(tile);
                            board[n.getX()][n.getY() + 1].getR().setStroke(board[n.getX()][n.getY()].getR().getStroke());
                            board[n.getX()][n.getY()].getR().setStroke(tileOutline);
                        }

                    });
                    moveQueue.dequeue();
                }
            } else {
                moveQueue.dequeueAll();
            }

        } else if (key.equals("Up")) {

            switch (id) {
                case 0:
                    iRotate();
                    break;
                case 1:
                    break;
                case 2:
                    jRotate();
                    break;
                case 3:
                    lRotate();
                    break;
                case 4:
                    tRotate();
                    break;
                case 5:
                    sRotate();
                    break;
                case 6:
                    zRotate();
                    break;

            }

        } else if (key.equals("Down")) {

            for (int x = NUM_ROWS - 1; x >= 0; x--) {
                for (int y = 0; y < NUM_COLUMNS; y++) {
                    if (board[x][y].getState() == 2) {
                        moveQueue.enqueue(board[x][y]);
                    }
                }
            }
            Node[] arr = new Node[4];
            int index = 0;

            while (moveQueue.isEmpty() != true) {

                Node n = (Node) moveQueue.peek();
                if (n.getX() + 1 < NUM_ROWS && (board[n.getX() + 1][n.getY()].getState() == 2 || board[n.getX() + 1][n.getY()].getState() == 0)) {

                    arr[index] = n;
                    index++;
                } else {
                    conflict = true;
                }
                moveQueue.dequeue();
            }

            if (conflict == false) {
                for (int x = 0; x < 4; x++) {
                    board[arr[x].getX() + 1][arr[x].getY()].setState(2);
                    board[arr[x].getX()][arr[x].getY()].setState(0);
                    final Rectangle r = board[arr[x].getX() + 1][arr[x].getY()].getR();
                    final Rectangle r1 = board[arr[x].getX()][arr[x].getY()].getR();
                    Platform.runLater(new Runnable() {

                        public void run() {
                            r.setFill(r1.getFill());
                            r1.setFill(tile);
                            r.setStroke(r1.getStroke());
                            r1.setStroke(tileOutline);

                        }

                    });

                }
            }

        } else if (key.equals("Space")) {
            slam();
        } else if (key.equals("C")) {

            hold();

        }
    }

    public void zRotate() {
        boolean conflict = false;

        if (orientation == 1) {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            if (n.getX() - 1 >= 0 && n.getY() + 2 < NUM_COLUMNS && board[n.getX() - 1][n.getY() + 2].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false && board[n.getX()][n.getY() + 2].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false) {

                board[n.getX() - 1][n.getY() + 2].setState(2);
                board[n.getX()][n.getY() + 2].setState(2);
                board[n.getX()][n.getY()].setState(0);
                board[n.getX() + 1][n.getY() + 2].setState(0);

                final Rectangle r = board[n.getX() - 1][n.getY() + 2].getR();
                final Rectangle r1 = board[n.getX()][n.getY() + 2].getR();
                final Rectangle r2 = board[n.getX()][n.getY()].getR();
                final Rectangle r3 = board[n.getX() + 1][n.getY() + 2].getR();

                Platform.runLater(new Runnable() {

                    public void run() {

                        r.setFill(RED);
                        r.setStroke(BLACK);
                        r1.setFill(RED);
                        r1.setStroke(BLACK);
                        r2.setFill(tile);
                        r2.setStroke(tileOutline);
                        r3.setFill(tile);
                        r3.setStroke(tileOutline);
                    }

                });

                orientation++;
            }

        } else {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            if (n.getY() - 2 >= 0 && board[n.getX() + 1][n.getY() - 2].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false && board[n.getX() + 2][n.getY()].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false) {

                board[n.getX() + 1][n.getY() - 2].setState(2);
                board[n.getX() + 2][n.getY()].setState(2);
                board[n.getX()][n.getY()].setState(0);
                board[n.getX() + 1][n.getY()].setState(0);

                final Rectangle r = board[n.getX() + 1][n.getY() - 2].getR();
                final Rectangle r1 = board[n.getX() + 2][n.getY()].getR();
                final Rectangle r2 = board[n.getX()][n.getY()].getR();
                final Rectangle r3 = board[n.getX() + 1][n.getY()].getR();

                Platform.runLater(new Runnable() {

                    public void run() {
                        r.setFill(RED);
                        r.setStroke(BLACK);
                        r1.setFill(RED);
                        r1.setStroke(BLACK);
                        r2.setFill(tile);
                        r2.setStroke(tileOutline);
                        r3.setFill(tile);
                        r3.setStroke(tileOutline);
                    }
                });

                orientation = 1;
            }
        }
    }

    public void sRotate() {
        boolean conflict = false;

        if (orientation == 1) {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            if (n.getX() - 1 >= 0 && n.getY() - 1 >= 0 && board[n.getX() - 1][n.getY() - 1].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false && board[n.getX()][n.getY() - 1].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false) {

                board[n.getX() - 1][n.getY() - 1].setState(2);
                board[n.getX()][n.getY() - 1].setState(2);
                board[n.getX()][n.getY() + 1].setState(0);
                board[n.getX() + 1][n.getY() - 1].setState(0);
                final Rectangle r = board[n.getX() - 1][n.getY() - 1].getR();
                final Rectangle r1 = board[n.getX()][n.getY() - 1].getR();
                final Rectangle r2 = board[n.getX()][n.getY() + 1].getR();
                final Rectangle r3 = board[n.getX() + 1][n.getY() - 1].getR();

                Platform.runLater(new Runnable() {

                    public void run() {

                        r.setFill(LIME);
                        r.setStroke(BLACK);
                        r1.setFill(LIME);
                        r1.setStroke(BLACK);
                        r2.setFill(tile);
                        r2.setStroke(tileOutline);
                        r3.setFill(tile);
                        r3.setStroke(tileOutline);

                    }

                });

                orientation++;
            }

        } else {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            if (n.getY() + 2 < NUM_COLUMNS && board[n.getX() + 1][n.getY() + 2].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false && board[n.getX() + 2][n.getY()].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false) {

                board[n.getX() + 1][n.getY() + 2].setState(2);
                board[n.getX() + 2][n.getY()].setState(2);
                board[n.getX()][n.getY()].setState(0);
                board[n.getX() + 1][n.getY()].setState(0);
                final Rectangle r = board[n.getX() + 1][n.getY() + 2].getR();
                final Rectangle r1 = board[n.getX() + 2][n.getY()].getR();
                final Rectangle r2 = board[n.getX()][n.getY()].getR();
                final Rectangle r3 = board[n.getX() + 1][n.getY()].getR();

                Platform.runLater(new Runnable() {

                    public void run() {

                        r.setFill(LIME);
                        r.setStroke(BLACK);
                        r1.setFill(LIME);
                        r1.setStroke(BLACK);
                        r2.setFill(tile);
                        r2.setStroke(tileOutline);
                        r3.setFill(tile);
                        r3.setStroke(tileOutline);

                    }

                });

                orientation = 1;
            }

        }

    }

    public void tRotate() {
        boolean conflict = false;
        if (orientation == 1) {
            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            if (n.getX() - 1 >= 0 && n.getY() + 1 < NUM_COLUMNS && board[n.getX() - 1][n.getY() + 1].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false) {

                board[n.getX()][n.getY() + 2].setState(0);
                board[n.getX() - 1][n.getY() + 1].setState(2);
                final Rectangle r = board[n.getX()][n.getY() + 2].getR();
                final Rectangle r1 = board[n.getX() - 1][n.getY() + 1].getR();
                Platform.runLater(new Runnable() {

                    public void run() {
                        r1.setFill(PURPLE);
                        r.setFill(tile);
                        r1.setStroke(BLACK);
                        r.setStroke(tileOutline);
                    }

                });

                orientation++;
            }

        } else if (orientation == 2) {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            if (n.getX() + 1 < NUM_ROWS && n.getY() + 1 < NUM_COLUMNS && board[n.getX() + 1][n.getY() + 1].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false) {

                board[n.getX() + 2][n.getY()].setState(0);
                board[n.getX() + 1][n.getY() + 1].setState(2);
                final Rectangle r = board[n.getX() + 2][n.getY()].getR();
                final Rectangle r1 = board[n.getX() + 1][n.getY() + 1].getR();
                Platform.runLater(new Runnable() {

                    public void run() {
                        r1.setStroke(BLACK);
                        r1.setFill(PURPLE);
                        r.setFill(tile);
                        r.setStroke(tileOutline);
                    }

                });

                orientation++;
            }
        } else if (orientation == 3) {
            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            if (n.getX() + 2 < NUM_ROWS && board[n.getX() + 2][n.getY()].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false) {

                board[n.getX() + 1][n.getY() - 1].setState(0);
                board[n.getX() + 2][n.getY()].setState(2);
                final Rectangle r = board[n.getX() + 1][n.getY() - 1].getR();
                final Rectangle r1 = board[n.getX() + 2][n.getY()].getR();
                Platform.runLater(new Runnable() {

                    public void run() {
                        r1.setStroke(BLACK);
                        r1.setFill(PURPLE);
                        r.setFill(tile);
                        r.setStroke(tileOutline);
                    }

                });

                orientation++;
            }

        } else {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            if (n.getX() + 1 <= NUM_ROWS && n.getY() - 1 >= 0 && board[n.getX() + 1][n.getY() - 1].getState() == 0) {
                conflict = false;
            } else {
                conflict = true;
            }

            if (conflict == false) {

                board[n.getX()][n.getY()].setState(0);
                board[n.getX() + 1][n.getY() - 1].setState(2);
                final Rectangle r1 = board[n.getX() + 1][n.getY() - 1].getR();
                final Rectangle r = board[n.getX()][n.getY()].getR();
                Platform.runLater(new Runnable() {

                    public void run() {
                        r1.setStroke(BLACK);
                        r1.setFill(PURPLE);
                        r.setFill(tile);
                        r.setStroke(tileOutline);
                    }

                });

                orientation = 1;
            }

        }

    }

    public void lRotate() {
        boolean conflict = false;

        if (orientation == 1) {
            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            int xC = n.getX();
            int yC = n.getY();
            int checkX = n.getX() - 1;
            int checkY = n.getY() + 1;

            for (int i = 0; i < 4; i++) {

                if (i > 0 && i < 3) {
                    checkX += 1;
                } else {
                    checkX = n.getX() - 1;
                }

                if (i == 3) {
                    checkY--;
                }

                if (checkX < NUM_ROWS && checkY < NUM_COLUMNS && checkY >= 0 && checkX >= 0 && (board[checkX][checkY].getState() == 0 || board[checkX][checkY].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    i = 10000;
                }
            }

            if (conflict == false) {

                xC = n.getX();
                yC = n.getY();
                checkX = n.getX() - 1;
                checkY = n.getY() + 1;

                for (int a = 0; a < 4; a++) {

                    if (a > 0 && a < 3) {
                        checkX += 1;
                        yC += 1;
                    } else {
                        checkX = n.getX() - 1;
                        yC = n.getY();
                    }

                    if (a == 3) {
                        checkY--;
                        xC++;
                    }

                    if (a != 1) {
                        board[checkX][checkY].setState(2);
                        board[xC][yC].setState(0);
                        final Rectangle r = board[checkX][checkY].getR();
                        final Rectangle r1 = board[xC][yC].getR();

                        Platform.runLater(new Runnable() {

                            public void run() {

                                r.setFill(ORANGE);
                                r1.setFill(tile);
                                r.setStroke(BLACK);
                                r1.setStroke(tileOutline);

                            }

                        });

                    }

                }
                orientation++;
            }

        } else if (orientation == 2) {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            int xC = n.getX();
            int yC = n.getY();
            int checkX = n.getX();
            int checkY = n.getY() + 2;

            for (int i = 0; i < 4; i++) {

                if (i != 0) {
                    checkX = n.getX() + 1;
                }

                if (i >= 2) {
                    checkY--;
                }

                if (checkX < NUM_ROWS && checkY < NUM_COLUMNS && checkY >= 0 && checkX >= 0 && (board[checkX][checkY].getState() == 0 || board[checkX][checkY].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    i = 10000;
                }

            }

            if (conflict == false) {

                xC = n.getX();
                yC = n.getY();
                checkX = n.getX();
                checkY = n.getY() + 2;

                for (int a = 0; a < 4; a++) {

                    if (a > 1) {
                        xC++;
                    }

                    if (a > 0) {
                        yC = n.getY() + 1;
                    }

                    if (a != 0) {
                        checkX = n.getX() + 1;
                    }

                    if (a >= 2) {
                        checkY--;
                    }

                    if (a != 2) {

                        board[checkX][checkY].setState(2);
                        board[xC][yC].setState(0);
                        final Rectangle r = board[checkX][checkY].getR();
                        final Rectangle r1 = board[xC][yC].getR();

                        Platform.runLater(new Runnable() {

                            public void run() {

                                r.setFill(ORANGE);
                                r1.setFill(tile);
                                r.setStroke(BLACK);
                                r1.setStroke(tileOutline);

                            }

                        });
                    }

                }
                orientation++;
            }

        } else if (orientation == 3) {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            int xC = n.getX();
            int yC = n.getY();
            int checkX = n.getX() + 2;
            int checkY = n.getY();

            for (int i = 0; i < 4; i++) {

                if (i > 1) {
                    checkX--;
                } else if (i >= 1) {
                    checkY = n.getY() - 1;
                }

                if (checkX < NUM_ROWS && checkY < NUM_COLUMNS && checkY >= 0 && checkX >= 0 && (board[checkX][checkY].getState() == 0 || board[checkX][checkY].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    i = 10000;
                }

            }

            if (conflict == false) {

                xC = n.getX();
                yC = n.getY();
                checkX = n.getX() + 2;
                checkY = n.getY();

                for (int a = 0; a < 4; a++) {
                    if (a > 1) {
                        checkX--;
                        yC--;
                    } else if (a >= 1) {
                        checkY = n.getY() - 1;
                        xC = n.getX() + 1;
                    }

                    if (a != 2) {

                        board[checkX][checkY].setState(2);
                        board[xC][yC].setState(0);
                        final Rectangle r = board[checkX][checkY].getR();
                        final Rectangle r1 = board[xC][yC].getR();

                        Platform.runLater(new Runnable() {

                            public void run() {

                                r.setFill(ORANGE);
                                r1.setFill(tile);
                                r.setStroke(BLACK);
                                r1.setStroke(tileOutline);

                            }

                        });
                    }

                }
                orientation++;
            }

        } else {
            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            int xC = n.getX();
            int yC = n.getY();
            int checkX = n.getX() + 1;
            int checkY = n.getY() + 1;

            for (int i = 0; i < 4; i++) {

                if (i == 3) {
                    checkX++;
                }

                if (i <= 2) {
                    checkY--;
                }

                if (checkX < NUM_ROWS && checkY < NUM_COLUMNS && checkY >= 0 && checkX >= 0 && (board[checkX][checkY].getState() == 0 || board[checkX][checkY].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    i = 10000;
                }

            }

            if (conflict == false) {

                xC = n.getX();
                yC = n.getY();
                checkX = n.getX() + 1;
                checkY = n.getY() + 1;

                for (int a = 0; a < 4; a++) {

                    if (a == 3) {
                        checkX++;
                        yC++;
                    }

                    if (a <= 2 && a > 0) {
                        checkY--;
                    }

                    if (a > 0 && a < 3) {
                        xC++;
                    }

                    if (a != 1) {

                        board[checkX][checkY].setState(2);
                        board[xC][yC].setState(0);
                        final Rectangle r = board[checkX][checkY].getR();
                        final Rectangle r1 = board[xC][yC].getR();

                        Platform.runLater(new Runnable() {

                            public void run() {

                                r.setFill(ORANGE);
                                r1.setFill(tile);
                                r.setStroke(BLACK);
                                r1.setStroke(tileOutline);

                            }

                        });
                    }

                }
                orientation = 1;
            }

        }

    }

    public void iRotate() {
        boolean conflict = false;
        int a = 0;
        int b = 0;
        if (orientation == 1 || orientation == 3) {

            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {
                    if (board[x][y].getState() == 2) {
                        a = x;
                        b = y;
                        x = 10000;
                        y = 10000;
                    }
                }

            }
            int yC;
            int xC;
            int temp = a;
            int tempB = b;
            for (int x = 0; x < 4; x++) {
                yC = tempB + 2;
                xC = (temp - 3) + x;
                if (yC < 10 && (board[xC][yC].getState() == 0 || board[xC][yC].getState() == 2)) {
                    moveQueue.enqueue(board[a][b]);
                }
                b++;
            }

            if (conflict == false) {
                int counter = 0;
                while (moveQueue.isEmpty() != true) {
                    yC = tempB + 2;
                    xC = (temp - 3) + counter;
                    Node n = (Node) moveQueue.peek();

                    board[n.getX()][n.getY()].setState(0);
                    board[xC][yC].setState(2);
                    moveQueue.dequeue();
                    counter++;
                    final Rectangle r = board[xC][yC].getR();
                    Platform.runLater(new Runnable() {

                        public void run() {
                            board[n.getX()][n.getY()].getR().setFill(tile);
                            r.setFill(CYAN);
                            r.setStroke(BLACK);
                            board[n.getX()][n.getY()].getR().setStroke(WHITE);

                        }

                    });

                }

            } else {
                moveQueue.dequeueAll();
            }
            orientation++;
        } else if (orientation == 2 || orientation == 4) {
            Node n = new Node();

            for (int f = 0; f < NUM_ROWS; f++) {

                for (int g = 0; g < NUM_COLUMNS; g++) {
                    if (board[f][g].getState() == 2) {
                        n = board[f][g];
                        f = 100000;
                        g = 100000;
                    }

                }
            }

            for (int i = 0; i < 4; i++) {
                if (((n.getX() + 3) < NUM_ROWS && (n.getY() - 2) >= 0 && (n.getY() - 2 + 3) < NUM_COLUMNS) && (board[n.getX() + 3][((n.getY() - 2) + i)].getState() == 0 || board[n.getX() + 3][(n.getY() - 2) + i].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    break;
                }
            }

            if (conflict == false) {

                for (int c = 0; c < 4; c++) {
                    if (c == 3) {
                        board[n.getX() + 3][((n.getY() - 2) + c)].setState(2);
                        final Rectangle r = board[n.getX() + 3][((n.getY() - 2) + c)].getR();
                        Platform.runLater(new Runnable() {

                            public void run() {
                                r.setFill(CYAN);
                                r.setStroke(BLACK);

                            }

                        });

                    } else {
                        board[n.getX() + 3][((n.getY() - 2) + c)].setState(2);
                        board[n.getX() + c][n.getY()].setState(0);

                        final Rectangle r = board[n.getX() + 3][((n.getY() - 2) + c)].getR();
                        final Rectangle r1 = board[n.getX() + c][n.getY()].getR();

                        Platform.runLater(new Runnable() {

                            public void run() {
                                r.setFill(CYAN);
                                r1.setFill(tile);
                                r.setStroke(BLACK);
                                r1.setStroke(tileOutline);

                            }

                        });

                    }
                }

                orientation++;

                if (orientation > 4) {
                    orientation = 1;
                }
            }
        }

    }

    public void jRotate() {
        boolean conflict = false;

        if (orientation == 1) {
            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }
            int xC = (n.getX());
            int yC = (n.getY());
            int temp = yC;
            int checkX = xC;
            int checkY = yC + 2;
            for (int i = 0; i < 4; i++) {

                if (i != 0) {
                    xC = (n.getX() + 1);
                    checkY = temp + 1;
                }

                if (i > 1) {
                    yC = n.getY() + i - 1;
                    checkX = n.getX() + i - 1;
                }

                if (checkX < NUM_ROWS && checkY < NUM_COLUMNS && checkY >= 0 && checkX >= 0 && (board[checkX][checkY].getState() == 0 || board[checkX][checkY].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    i = 10000;
                }

            }
            xC = (n.getX());
            yC = (n.getY());
            temp = yC;
            checkX = xC;
            checkY = yC + 2;

            if (conflict == false) {
                for (int a = 0; a < 4; a++) {

                    if (a != 0) {
                        xC = (n.getX() + 1);
                        checkY = temp + 1;
                    }

                    if (a > 1) {
                        yC = n.getY() + a - 1;
                        checkX = n.getX() + a - 1;
                    }

                    if (a != 2) {

                        board[checkX][checkY].setState(2);
                        board[xC][yC].setState(0);
                        final Rectangle r = board[checkX][checkY].getR();
                        final Rectangle r1 = board[xC][yC].getR();
                        Platform.runLater(new Runnable() {

                            public void run() {

                                r.setFill(BLUE);
                                r1.setFill(tile);
                                r.setStroke(BLACK);
                                r1.setStroke(tileOutline);

                            }

                        });

                    }
                }
            }
            orientation++;
        } else if (orientation == 2) {
            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            int checkX = n.getX() + 1;
            int checkY = n.getY() + 1;
            int xC = n.getX();
            int yC = n.getY();

            for (int i = 0; i < 4; i++) {

                if (i == 1) {
                    checkX = n.getX() + 2;
                } else {
                    checkX = n.getX() + 1;
                }

                if (i > 1) {
                    checkY = n.getY() - i + 2;
                }

                if (checkX < NUM_ROWS && checkY < NUM_COLUMNS && checkY >= 0 && checkX >= 0 && (board[checkX][checkY].getState() == 0 || board[checkX][checkY].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    i = 10000;
                }

            }
            checkX = n.getX() + 1;
            checkY = n.getY() + 1;
            xC = n.getX();
            yC = n.getY();

            if (conflict == false) {

                for (int a = 0; a < 4; a++) {

                    if (a >= 2) {
                        xC = n.getX() + a - 1;
                    }

                    if (a == 1) {
                        yC = n.getY() + 1;
                    } else {
                        yC = n.getY();
                    }

                    if (a == 1) {
                        checkX = n.getX() + 2;
                    } else {
                        checkX = n.getX() + 1;
                    }

                    if (a > 1) {
                        checkY = n.getY() - a + 2;
                    }

                    if (a != 2) {

                        board[checkX][checkY].setState(2);
                        board[xC][yC].setState(0);
                        final Rectangle r = board[checkX][checkY].getR();
                        final Rectangle r1 = board[xC][yC].getR();

                        Platform.runLater(new Runnable() {

                            public void run() {

                                r.setFill(BLUE);
                                r1.setFill(tile);
                                r.setStroke(BLACK);
                                r1.setStroke(tileOutline);

                            }

                        });

                    }

                }

                orientation++;
            }
        } else if (orientation == 3) {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            int xC = n.getX();
            int yC = n.getY();
            int checkX = n.getX() - 1;
            int checkY = n.getY() + 1;

            for (int i = 0; i < 4; i++) {

                if (i < 3) {
                    checkX += i;
                }
                if (i == 2) {
                    checkX--;
                }

                if (i == 3) {
                    checkY--;
                }

                if (checkX < NUM_ROWS && checkY < NUM_COLUMNS && checkY >= 0 && checkX >= 0 && (board[checkX][checkY].getState() == 0 || board[checkX][checkY].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    i = 10000;
                }

            }

            xC = n.getX();
            yC = n.getY() - 1;
            checkX = n.getX() - 1;
            checkY = n.getY() + 1;

            for (int a = 0; a < 4; a++) {

                if (a == 3) {
                    xC++;
                }

                if (a < 3) {
                    yC++;
                }

                if (a < 3) {
                    checkX += a;
                }
                if (a == 2) {
                    checkX--;
                }

                if (a == 3) {
                    checkY--;
                }

                if (a != 1) {

                    board[checkX][checkY].setState(2);
                    board[xC][yC].setState(0);
                    final Rectangle r = board[checkX][checkY].getR();
                    final Rectangle r1 = board[xC][yC].getR();

                    Platform.runLater(new Runnable() {

                        public void run() {

                            r.setFill(BLUE);
                            r1.setFill(tile);
                            r.setStroke(BLACK);
                            r1.setStroke(tileOutline);

                        }

                    });

                }

            }
            orientation++;
        } else {

            Node n = new Node();
            for (int x = 0; x < NUM_ROWS; x++) {

                for (int y = 0; y < NUM_COLUMNS; y++) {

                    if (board[x][y].getState() == 2) {
                        n = board[x][y];
                        x = 100000;
                        y = 100000;
                    }

                }

            }

            int xC = n.getX();
            int yC = n.getY();
            int checkX = n.getX() + 1;
            int checkY = n.getY() + 1;

            for (int i = 0; i < 4; i++) {

                if (i == 3) {
                    checkX--;
                }

                if (i > 0 && i < 3) {
                    checkY--;
                }

                if (checkX < NUM_ROWS && checkY < NUM_COLUMNS && checkY >= 0 && checkX >= 0 && (board[checkX][checkY].getState() == 0 || board[checkX][checkY].getState() == 2)) {
                    conflict = false;
                } else {
                    conflict = true;
                    i = 10000;
                }
            }
            xC = n.getX();
            yC = n.getY();
            checkX = n.getX() + 1;
            checkY = n.getY() + 1;

            for (int a = 0; a < 4; a++) {

                if (a == 3) {
                    checkX--;
                    yC--;
                }

                if (a > 0 && a < 3) {
                    checkY--;
                    xC++;
                }

                if (a != 1) {

                    board[checkX][checkY].setState(2);
                    board[xC][yC].setState(0);
                    final Rectangle r = board[checkX][checkY].getR();
                    final Rectangle r1 = board[xC][yC].getR();

                    Platform.runLater(new Runnable() {

                        public void run() {

                            r.setFill(BLUE);
                            r1.setFill(tile);
                            r.setStroke(BLACK);
                            r1.setStroke(tileOutline);

                        }

                    });

                }

            }

            orientation = 1;
        }

    }

    public void checkLoss() {

        for (int x = 0; x < NUM_COLUMNS; x++) {

            if (board[4][x].getState() == 1) {
                game = false;
            }

        }

    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

}
