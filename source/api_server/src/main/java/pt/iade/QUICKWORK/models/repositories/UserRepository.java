package pt.iade.QUICKWORK.models.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.views.UsrJobsView;
import pt.iade.QUICKWORK.models.views.getownerview;


public interface UserRepository extends CrudRepository<User,Integer> {

    // *FIND JOBS HISTORY
    @Query(value = "select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as starting, work_finished as finished, work_wt_id as worktype_id "+
                    "from usr "+
                    "inner join usrwork on usr_id = uw_usr_id "+
                    "inner join work on uw_work_id = work_id " + "Where usr_id=:id ", nativeQuery = true) 
        Iterable<UsrJobsView> findusrjobs(@Param("id") int id);

    

    
        //! FIND CREATOR OF WORK
        //! "Select usr_id, usr_name, usr_email, usr_password, usr_njobs, usr_avg_rating, usr_loc from usr inner join usrwork on usr_id = uw_usr_id inner join work on work_id = uw_work_id where uw_usrcreate = true and work_id= 2"

        @Query(value = "select usr_id as ownerid from work inner join usrwork on work_id = uw_work_id inner join usr on usr_id = uw_usr_id where uw_usrcreate = true and work_id =:workid", nativeQuery = true)
        Optional<getownerview> getownerid1(@Param("workid") int workid);
        


}   
