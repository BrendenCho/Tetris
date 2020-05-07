package tetris;

import java.io.File;
import java.util.Random;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Brenden Cho
 */
public class Audio extends Thread {

    private long curr;
    private Media currentTrack;
    private int arr[] = new int[]{1, 2, 3, 4, 5};
    private boolean play = true;
    private String path;
    private MediaPlayer mp;
    private boolean change = false;

    public Audio() {
    }

    public int pickFromList() {
        int num = 0;
        boolean trackLeft = false;
        for (int x = 0; x < 5; x++) {
            if (arr[x] != -1) {
                trackLeft = true;
            }

        }

        if (trackLeft == false) {
            arr = new int[]{1, 2, 3, 4, 5};
        }
        boolean found = false;
        Random r = new Random();
        while (found != true) {

            num = r.nextInt(5);

            if (arr[num] != -1) {
                int temp = num;
                num = arr[num];
                arr[temp] = -1;
                found = true;
            }
        }

        return num;
    }

    public String setUp(int clip) {
        String path = System.getProperty("user.dir");
        path = path + "\\music\\Track" + Integer.toString(clip) + ".mp3";
        currentTrack = new Media(new File(path).toURI().toString());
        mp = new MediaPlayer(currentTrack);
        mp.setVolume(10);
        mp.setOnEndOfMedia(new Runnable() {

            public void run() {
     
                change = true;
            }

        });

        return path;
    }

    @Override
    public void run() {
      
        try{
        super.setName("Audio Thread");

        int num = pickFromList();
        setUp(num);

        while (play == true) {
            mp.play();

            if (change != false) {
      
                num = pickFromList();
                setUp(num);
                change = false;
            }

        }

        }catch (MediaException e){
        System.out.println("Media file unavailable thread closing");
        }
    }

    public long getCurr() {
        return curr;
    }

    public void setCurr(long curr) {
        this.curr = curr;
    }

    public Media getCurrentTrack() {
        return currentTrack;
    }

    public void setCurrentTrack(Media currentTrack) {
        this.currentTrack = currentTrack;
    }

    public int[] getArr() {
        return arr;
    }

    public void setArr(int[] arr) {
        this.arr = arr;
    }

    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

}
