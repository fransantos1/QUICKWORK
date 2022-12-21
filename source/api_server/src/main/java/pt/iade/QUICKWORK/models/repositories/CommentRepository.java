package pt.iade.QUICKWORK.models.repositories;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import pt.iade.QUICKWORK.models.Comment;
import pt.iade.QUICKWORK.models.views.Usrworkview;
public interface CommentRepository extends CrudRepository<Comment,Integer>{
    
        @Query(value = "select uw_usr_id as user_id, uw_work_id as work_id"+
                        " from usrwork "+
                        " where uw_id = :id ", nativeQuery = true)
        Usrworkview getworkuser(@Param("id") int id);


}   
