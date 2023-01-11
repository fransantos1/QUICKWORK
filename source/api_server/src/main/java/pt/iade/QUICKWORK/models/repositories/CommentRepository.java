package pt.iade.QUICKWORK.models.repositories;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.Comment;
import pt.iade.QUICKWORK.models.views.Usrworkview;
public interface CommentRepository extends CrudRepository<Comment,Integer>{
    
        @Query(value = "select uw_usr_id as user_id, uw_work_id as work_id, uw_id, usr_name as name"+
                        " from usrwork inner join usr on uw_usr_id = usr_id "+
                        " where uw_id = :id ", nativeQuery = true)
        Usrworkview getusrworkusrwork(@Param("id") int id);
        @Query(value = "select uw_usr_id as user_id, uw_work_id as work_id, usr_name as name"+
                        " from usrwork inner join usr on uw_usr_id = usr_id"+
                        " where uw_usr_id = :usrid"+
                        " order by uw_id desc"+
                        " limit 1", nativeQuery = true)
        Usrworkview getuserworkusr(@Param("usrid") int id);
        @Query(value =  "select uw_usr_id as user_id, uw_work_id as work_id, uw_id, usr_name as name"+
                        " from usrwork inner join usr on uw_usr_id = usr_id"+
                        " where uw_work_id = :workid", nativeQuery = true)
        ArrayList<Usrworkview> getuserworkwork(@Param("workid") int workid);

        //get comments with work ID
         @Query(value =  "select rating_id, rating_comment, rating_rat, rating_usr1_id, rating_uw_id from rating"+
                        " inner join usrwork on uw_id = rating_uw_id "+
                        " where uw_work_id = :id", nativeQuery = true)
        ArrayList<Comment> getcommentsfromwork(@Param("id") int workid);



        //get comments
        //----------------------------------------------------------------
        
        @Query(value =  "select rating_id, rating_comment, rating_rat, rating_usr1_id, rating_uw_id"+
                        " from rating"+
                        " inner join usrwork on uw_id = rating_uw_id" +
                        " where uw_usr_id = :usrid", nativeQuery = true)
        ArrayList<Comment> getcomments(@Param("usrid") int id);
        
        @Query(value = "select uw_usr_id from usrwork where uw_id = :id", nativeQuery = true)
        int getcommentedusrid(@Param("id") int id);
        

}   
