package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeAndDateHelper {

    private DateTimeFormatter date;
    private DateTimeFormatter time;

    public TimeAndDateHelper() {
        date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        time = DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    public String getDate() {
        return date.format(LocalDateTime.now());
    }

    public String getTime() {
        return time.format(LocalDateTime.now());
    }
}
