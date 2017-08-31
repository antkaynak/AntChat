package Client.Utility;

import Client.ApplicationContext;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class LabelClear implements Runnable{

    private Thread thread;
    private Label label;
    private int duration;

    public LabelClear(Label label){
        this.label = label;
        this.duration = 2000;
        thread = new Thread(this);
        thread.start();
    }

    public LabelClear(Label label, int duration){
        this.label = label;
        this.duration = duration;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while(true) {
            Platform.runLater(()-> label.setText(""));
            try {
                thread.sleep(duration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
