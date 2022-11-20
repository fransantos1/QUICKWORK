package pt.iade.QUICKWORK.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="usr")
public class User {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
@Column(name="usr_id") private int id;
@Column(name="usr_name") private String name;
@Column(name="usr_email") private String email;
@Column(name="usr_password") private String password;
@Column(name="usr_njobs") private int number;
@Column(name="usr_avg_rating") private int rating;

public User() {}

public int getId(){return id;}
public String getName() {
    return name;
}
public String getEmail() {
    return email;
}
public String getPassword() {
    return password;
}
public int getNumber() {
    return number;
}
public int getRating() {
    return rating;
}

}

