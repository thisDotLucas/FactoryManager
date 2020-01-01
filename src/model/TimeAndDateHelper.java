package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeAndDateHelper {

    private DateTimeFormatter date;
    private DateTimeFormatter time;
    private DateTimeFormatter dateAndTime;

    public TimeAndDateHelper() {
        date = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        time = DateTimeFormatter.ofPattern("HH:mm:ss");
        dateAndTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    }

    public String getDate() {
        return date.format(LocalDateTime.now());
    }

    public String formatDate(LocalDate date){ return this.date.format(date); }

    public String getTime() {
        return time.format(LocalDateTime.now());
    }

    public String getTimeAndDate() {
        return dateAndTime.format(LocalDateTime.now());
    }
}
