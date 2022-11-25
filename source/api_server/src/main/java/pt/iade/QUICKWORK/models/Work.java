package pt.iade.QUICKWORK.models;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="work")
public class Work {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="work_id") private int id;
@Column(name="work_pricehr") private int pricehr;
@Column(name="work_tip") private Integer tip;
@Column(name="work_starting") private LocalDate started_time;
@Column(name="work_finished") private LocalDate finished_time;
@Column(name="work_wt_id") private int state_id;

public Work() {}

public int getId() {
    return id;
}

public int getPricehr() {
    return pricehr;
}

public Integer getTip() {
    return tip;
}

public LocalDate getStarted_time() {
    return started_time;
}

public LocalDate getFinished_time() {
    return finished_time;
}

public int getState_id() {
    return state_id;
}





}

