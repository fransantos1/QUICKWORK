package pt.iade.QUICKWORK.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pt.iade.QUICKWORK.models.Comment;
import pt.iade.QUICKWORK.models.exceptions.NotavailableException;
import pt.iade.QUICKWORK.models.repositories.CommentRepository;
import pt.iade.QUICKWORK.models.repositories.WorkRepository;
import pt.iade.QUICKWORK.models.views.Usrworkview;

@RestController
@RequestMapping(path ="/api/comment")
public class CommentController {
        private Logger Logger = LoggerFactory.getLogger(UserController.class);
        @Autowired
        private CommentRepository commentRepository; 
        @Autowired
        private WorkRepository workRepository;
        
        //Post Comment 
          @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
          public Comment PostComment(@RequestBody Comment comment) throws NotavailableException{

            //get the comment
            Comment _comment = comment;
            int uw_id = comment.getRatedusrwork();
            //get work  
            Usrworkview getwork = commentRepository.getworkuser(uw_id);
            int work_id = getwork.getwork_id();
            //verify that work is finished
            String work_State = workRepository.state(work_id);
            if(work_State == "Completo"){
              Logger.info(""+ _comment.getComment()+" posted");
              commentRepository.save(_comment);
              return _comment;

            }else throw new NotavailableException(""+work_id, "work", "Job not finished yet or canceled" );
          } 
   
}
