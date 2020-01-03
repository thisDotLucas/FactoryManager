package model;

import controller.Viewable;
import javafx.application.Platform;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.concurrent.Task;

public class Clock {

    private Viewable controller; //UI
    private SimpleDateFormat formatter; //time format HH:mm
    private String time; //time displayed
    private String newTime; //current time

    public Clock(Viewable controller) { //constructor

        this.controller = controller;
        formatter = new SimpleDateFormat("HH:mm");
        time = formatter.format(new Date());
        newTime = time;
        controller.setTimeLabel(time);
        run();
    }

    private void run() {

        Task task = new Task() {
            @Override
            protected Object call() {

                while (true) {

                    newTime = formatter.format(new Date()); //update time

                    if (!time.equals(newTime)) { //check if updated time is different from previous time if different would
                        //indicate that one minute has passed and clock needs to be updated.
                        time = newTime; //newTime is set to the time to be checked

                        Platform.runLater(() -> {
                            controller.setTimeLabel(time); //clock is updated in user interface
                        });

                    } else { //else sleep for 1 second and repeat

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ie) {
                            ie.printStackTrace();
                        }

                    }

                }
            }
        };

        new Thread(task).start();

    }

}
