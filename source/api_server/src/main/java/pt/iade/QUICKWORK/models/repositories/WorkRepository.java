package pt.iade.QUICKWORK.models.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.Work;
import pt.iade.QUICKWORK.models.views.Workmapview;
import pt.iade.QUICKWORK.models.views.customusrview;
import pt.iade.QUICKWORK.models.views.workview;


public interface WorkRepository extends CrudRepository<Work,Integer> {

        //show jobs on the map
        @Query(value =  " select work_id as id, work_loc[0] as lat, work_loc[1] as lon, wt_name as type"+
                        " from work"+
                        " inner join worktype on wt_id = work_wt_id"+
                        " inner join work_state on work_id = ws_work_id"+
                        " inner join _state on state_id = ws_state_id"+
                        " where state_name = 'Em espera'" , nativeQuery = true) 
            ArrayList<Workmapview> workmapshow();

        //get a work type by the id
        @Query(value =  "select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as started_time, work_finished as finished_time, work_loc[0] as lat, work_loc[1] as lon, wt_name as type"+
                        " from work"+
                        " inner join worktype on wt_id = work_wt_id"+
                        " where work_id = :id", nativeQuery = true)
        workview getworkandtype(@Param("id") int work_id);
        
        //get the work somebody is working on 
        @Query(value = "select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as started_time, work_finished as finished_time, work_loc[0] as lat, work_loc[1] as lon, wt_name as type from work inner join usrwork on work_id = uw_work_id inner join work_state on ws_work_id = work_id inner join worktype on wt_id = work_wt_id where uw_usr_id = :user_id and ws_state_id != 4 and ws_state_id != 3 order by work_id desc  limit 1", nativeQuery = true)
        workview getworkfromusr(@Param("user_id") int usrid);
        //get work
        @Query(value = "select work_id as id, work_pricehr as pricehr, work_tip as tip, work_starting as started_time, work_finished as finished_time, work_loc[0] as lat, work_loc[1] as lon, wt_name as type from work inner join usrwork on work_id = uw_work_id inner join work_state on ws_work_id = work_id inner join worktype on wt_id = work_wt_id where uw_usr_id = :user_id and ws_state_id != 4 and ws_state_id != 3 and uw_usrcreate is true order by work_id desc  limit 1", nativeQuery = true)
        workview getworkfromowner(@Param("user_id") int usrid);




        
        //save work 
        //-----------------------------------------------

        //add work on table
        @Transactional
        @Modifying
        @Query(value = "Insert into work (work_loc,  work_pricehr, work_tip, work_starting, work_finished, work_price, work_wt_id) values (POINT(:lat, :lon), :pricehr, null, null, null, null, :worktype_id);"
                        , nativeQuery = true)
        void savework(@Param("lat") double lat, @Param("lon") double lon, @Param("pricehr") double pricehr, @Param("worktype_id") int wt_id);

        //getting work with the correct id
        Work findByLatAndLon(double lat, double lon);
     
        //adding all dependencies to a work 
        @Transactional
        @Modifying
        @Query (value = "insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (:usrid ,:workid, true);"+
                        " insert into work_state (ws_work_id, ws_state_id) values (:workid, 1);",nativeQuery = true)
        void AddDependencies(@Param("workid") int work_id,@Param("usrid") int usr_id);


        //-------------------------------------------------------------------------------------

        //List of all users in a work except the owner

        @Query (value = "select usr_id as id, usr_name as name, usr_email as email, usr_njobs as jobnumber, usr_avg_rating as rating from usr inner join usrwork on uw_usr_id = usr_id  where uw_work_id = :workid and uw_usrcreate is not true",nativeQuery = true)
        ArrayList<customusrview> getusers(@Param("workid") int workid );

        //STATE QUERIES 
        //---------------------------------------------------------------

        //modify state 
        @Transactional
        @Modifying
        @Query (value = "insert into work_state (ws_work_id, ws_state_id) values (:workid, :work_state_id);",nativeQuery = true)
        void setState(@Param("workid") int workid,@Param("work_state_id") int state_id);

        //get work current state
        @Query (value = "select state_name as state"+
                " from work_state" +
                " inner join _state on ws_state_id = state_id"+
                " where ws_work_id = :workid" +
                " Order by ws_id DESC"+
                " limit 1", nativeQuery = true)
        String getState(@Param("workid")int workid);
        
        //get all states
        @Query(value = "select state_name"+
                        " from _state", nativeQuery = true)
        Iterable<String> getStates();


        //ACCEPT WORK
        //---------------------------------------------------------------------------------------------- 

        //changing job state ,time ,etc
        @Transactional
        @Modifying
        @Query (value = "update work"+
                        " set work_starting = :time"+
                        " where work_id = :id", nativeQuery = true)
        void acceptWork(@Param("time") LocalDateTime time, @Param("id") int workid);

        //add user to usr work
        @Transactional
        @Modifying
        @Query (value = "insert into usrwork (uw_usr_id, uw_work_id, uw_usrcreate) values (:usrid ,:workid, false);", nativeQuery = true)
        void acceptWork1(@Param("usrid") int usrid, @Param("workid") int workid);
        //-----------------------------------------------------------------------------------------------------------------

        //cancel job
        //when job is canceled the job can be deleted
        @Transactional
        @Modifying
        @Query(value =  " delete from usrwork where uw_work_id = :workid ;"+
                        " delete from work_state where ws_work_id = :workid ;"+
                        " Delete from work where work_id = :workid ;", nativeQuery = true)
        void deleteJob(@Param("workid") int id);

        //finish job

        @Transactional
        @Modifying
        @Query(value =  " UPDATE work"+
                        " SET work_finished = :time"+
                        " WHERE work_id = :workid ;"+
                        " insert into work_state (ws_work_id, ws_state_id) values ( :workid , 3);", nativeQuery = true)
        void FinishJob(@Param("workid") int id, @Param("time") LocalDateTime time);
        



}
