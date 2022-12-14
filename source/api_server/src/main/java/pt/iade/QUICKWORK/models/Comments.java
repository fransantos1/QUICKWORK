package pt.iade.QUICKWORK.models;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*



public class WorkType {
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
 */



@Entity
@Table(name="rating")
public class Comments {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rating_id")private int id;
    @Column(name="rating_comment")private String comment;
    @Column(name="rating_rat")private int rating;
    @Column(name="rating_usr1_id")private int ratingusrid;
    @Column(name="rating_uw_id")private int ratedusrwork;
    public int getId() {
        return id;
    }
    public String getComment() {
        return comment;
    }
    public int getRating() {
        return rating;
    }
    public int getRatingusrid() {
        return ratingusrid;
    }
    public int getRatedusrwork() {
        return ratedusrwork;
    }
    
    



}
