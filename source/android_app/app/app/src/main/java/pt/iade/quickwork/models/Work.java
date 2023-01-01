package pt.iade.quickwork.models;

import java.time.LocalDate;

public class Work {
    private int id;
    private Double pricehr;
    private Double tip; //allways null if the job hasnt started
    private LocalDate started_time; //allways null if the job hasnt started
    private LocalDate finished_time; //allways null if the job hasnt started
    private String type;
    private double lat;
    private double lon;

    public Work(int id, Double pricehr, Double tip, LocalDate started_time, LocalDate finished_time, String type, double lat, double lon) {
        this.id = id;
        this.pricehr = pricehr;
        this.tip = tip;
        this.started_time = started_time;
        this.finished_time = finished_time;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }
    public Work(Double pricehr, String type, double lat, double lon) {
        this.pricehr = pricehr;
        this.type = type;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPricehr() {
        return pricehr;
    }

    public void setPricehr(Double pricehr) {
        this.pricehr = pricehr;
    }

    public Double getTip() {
        return tip;
    }

    public void setTip(Double tip) {
        this.tip = tip;
    }

    public LocalDate getStarted_time() {
        return started_time;
    }

    public void setStarted_time(LocalDate started_time) {
        this.started_time = started_time;
    }

    public LocalDate getFinished_time() {
        return finished_time;
    }

    public void setFinished_time(LocalDate finished_time) {
        this.finished_time = finished_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
