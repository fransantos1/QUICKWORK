package pt.iade.QUICKWORK.models.views;

import java.time.LocalDate;

public interface UsrJobsView {
    int getId();
    int getPricehr();
    Integer getTip();
    LocalDate getstarting();
    LocalDate getfinished();
    int getworktype_id(); 
}
