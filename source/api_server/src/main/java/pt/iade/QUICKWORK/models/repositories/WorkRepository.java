package pt.iade.QUICKWORK.models.repositories;

import org.hibernate.annotations.Parent;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.Work;
import pt.iade.QUICKWORK.models.views.Workmapview;

public interface WorkRepository extends CrudRepository<Work,Integer> {
    
    
    @Query(value =  "select work_loc[0] as lat, work_loc[1] as lon, wt_name as type from work inner join worktype on work_wt_id = wt_id" , nativeQuery = true) 
            Iterable<Workmapview> workmapshow();

    @Modifying @Query(value = "Update work set work_wt_id = :state where work_id = :id ;",  nativeQuery = true)
        int setstate(@Param("state") Integer status,@Param("id") Integer id);


}
