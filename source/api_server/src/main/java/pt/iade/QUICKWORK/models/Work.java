package pt.iade.QUICKWORK.models;

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
@Column(name="work_tip") private int tip;
//@Column(name="work_starting") private int password;
//@Column(name="work_finished") private int number;
@Column(name="work_wt_id") private int state_id;

public Work() {}



}

