package pt.iade.QUICKWORK.models.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.views.UsrJobsView;
import pt.iade.QUICKWORK.models.views.UsrLocview;
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

                //Verify if a user has any active jobs 
        //-----------------------------------------------------------------

        //verifying if it has any available jobs
        
        @Query(value =  "select exists (select state"+
                        " from (select state_id as state "+
                        " from work_state "+
                        " inner join _state on ws_state_id = state_id inner join work on ws_work_id = work_id inner join usrwork on uw_work_id = work_id"+
                        " where uw_usr_id = :id "+
                        " Order by ws_id DESC limit 1) subquerie"+
                        " where state != 4 and state != 3)", nativeQuery = true)
        Boolean isUserOccupied(@Param("id") int id);

        //verifying if he is the owner of the jobs
        @Query(value =  " select uw_usrcreate"+
                        " from usrwork" +
                        " where uw_usr_id = :id order by uw_id desc limit 1", nativeQuery = true)
        Boolean isUserowner(@Param("id") int id);
        //-------------------------------------------------------------------------------------
        
        //user location 
        //----------------------------------------------------------------------------------------
        //get location
        @Query(value = "select usr_loc[0] as lat, usr_loc[1] as lon from usr where usr_id = :usrid", nativeQuery = true)
        UsrLocview getlocation(@Param("usrid") int id);

        //set location
        @Transactional
        @Modifying
        @Query(value =  "UPDATE usr"+
                        " SET usr_loc = POINT(:lat, :lon)"+
                        " where usr_id = :usrid ", nativeQuery = true)
        void setlocation(@Param("usrid") int id, @Param("lat") Double lat, @Param("lon") Double lon);

        
        @Transactional
        @Modifying
        @Query(value =  "UPDATE usr"+
                        " SET usr_njobs = :njob"+
                        " where usr_id = :usrid ", nativeQuery = true)
        void setnjob(@Param("usrid") int id, @Param("njob") int njob);





}   



/*select ws_state_id ,work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as started_time, work_finished as finished_time, work_loc[0] as lat, work_loc[1] as lon, wt_name as type
from work
inner join usrwork on uw_work_id = work_id
inner join work_state on ws_work_id = work_id
inner join worktype on wt_id = work_wt_id
where uw_usr_id = 14 and ws_state_id != 4 and ws_state_id != 3 and uw_usrcreate is true
limit 1 */