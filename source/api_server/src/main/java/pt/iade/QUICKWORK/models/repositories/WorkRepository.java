package pt.iade.QUICKWORK.models.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.Work;
import pt.iade.QUICKWORK.models.views.Workmapview;


public interface WorkRepository extends CrudRepository<Work,Integer> {

        //show jobs on the map
        @Query(value =  " select work_id as id, work_loc[0] as lat, work_loc[1] as lon, wt_name as type"+
                        " from work"+
                        " inner join worktype on wt_id = work_wt_id"+
                        " inner join work_state on work_id = ws_work_id"+
                        " inner join _state on state_id = ws_state_id"+
                        " where state_name = 'Em espera'" , nativeQuery = true) 
            Iterable<Workmapview> workmapshow();
        //get work state
        @Query(value =  "select state_name"+
                        " from _state"+
                        " inner join work_state on state_id = ws_state_id"+
                        " inner join work on work_id = ws_work_id"+
                        " where work_id = :id", nativeQuery = true)
        String state(@Param("id") int work_id);
        
  

        // get usr owner id



        //Iterable<Work> findbylat(Double lat);

}
