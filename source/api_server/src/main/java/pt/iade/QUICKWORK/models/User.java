package pt.iade.QUICKWORK.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name="usr")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="usr_id") private int id;
    @Column(name="usr_name") private String name;
    @Column(name="usr_email") private String email;
    @Column(name="usr_password") private String password;
    @Column(name="usr_njobs") private Integer jobnumber;
    @Column(name="usr_avg_rating") private Integer rating;



    public User() {}
    
    public void setJobnumber(Integer jobnumber) {
        this.jobnumber = jobnumber;
    }

    public int getId(){
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }
    public Integer getJobnumber() {
        return jobnumber;
    }
    public Integer getRating() {
        return rating;
    }

}

