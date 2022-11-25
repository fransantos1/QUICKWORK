package pt.iade.QUICKWORK.models.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.views.UsrJobsView;


public interface UserRepository extends CrudRepository<User,Integer> {


    @Query(value = "select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as starting, work_finished as finished, work_wt_id as workstate_id"+
                    "from usr"+
                    "inner join usrwork on usr_id = uw_usr_id"+
                    "inner join work on uw_work_id = work_id" + "Where usr_id=:id", nativeQuery = true) 
        Iterable<UsrJobsView> findusrjobs(@Param("id") int id);


}
