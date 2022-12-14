package pt.iade.QUICKWORK.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="worktype")
public class WorkType {
@Id 
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name="wt_id")private int id;
@Column(name="wt_name")private String name;
@Column(name="wt_avgprice_hr")private Double pricehr;
public int getId() {
    return id;
}
public String getName() {
    return name;
}
public Double getPricehr() {
    return pricehr;
}





}
