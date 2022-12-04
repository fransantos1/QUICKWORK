package pt.iade.QUICKWORK.models.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.Work;
import pt.iade.QUICKWORK.models.views.Workmapview;
import pt.iade.QUICKWORK.models.views.gettypeView;


public interface WorkRepository extends CrudRepository<Work,Integer> {

        //show jobs on the map
        @Query(value =  "select work_id as id, work_loc[0] as lat, work_loc[1] as lon, wt_name as type from work inner join worktype on work_wt_id = wt_id where work_starting is null " , nativeQuery = true) 
            Iterable<Workmapview> workmapshow();
            //WORK STARTING IS NULL BECAUSE IF THE WORK HASNT STARTED IS = TO SEEING IF THE WORK IS AVAILABLE VALID FOR PROTOTYPE
            //! WORKS FOR THE PROTOTYPE BUT I NEED TO CHANGE THIS BECUASE THE WORK NOT HAVING STARTED COULD ALSO MEAN THAT IT WAS CANCELED
        
        //modify work state
        @Transactional@Modifying @Query(value = "Update work set work_wt_id = :state where work_id = :id ;",  nativeQuery = true)
        int setstate(@Param("state") Integer status,@Param("id") Integer id);

        //find type by id
        
        @Query(value = "Select wt_name as name from worktype where wt_id = :id", nativeQuery = true)
        gettypeView gettype(@Param("id") int id);

        // get usr owner id



        //Iterable<Work> findbylat(Double lat);

}
