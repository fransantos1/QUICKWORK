package pt.iade.QUICKWORK.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import pt.iade.QUICKWORK.models.Work;
import pt.iade.QUICKWORK.models.views.Workmapview;

public interface WorkRepository extends CrudRepository<Work,Integer> {
    
    
    @Query(value =  "select work_loc[0] as lat, work_loc[1] as lon, wt_name as type from work inner join worktype on work_wt_id = wt_id" , nativeQuery = true) 
            Iterable<Workmapview> workmapshow();





}
