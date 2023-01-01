package pt.iade.QUICKWORK.models.views;

import java.time.LocalDate;

public interface workview {
    int getid();
    Double getpricehr();
    Double gettip();
    LocalDate getstarted_time();
    LocalDate getfinished_time();
    double getlat();
    double getlon();
    String gettype();
}
//? couldnt find another way of combining the work table with the work_type