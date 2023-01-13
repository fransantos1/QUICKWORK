package pt.iade.QUICKWORK.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pt.iade.QUICKWORK.models.Comment;
import pt.iade.QUICKWORK.models.exceptions.NotFoundException;
import pt.iade.QUICKWORK.models.exceptions.NotavailableException;
import pt.iade.QUICKWORK.models.repositories.CommentRepository;
import pt.iade.QUICKWORK.models.repositories.UserRepository;
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
  @Autowired
  private UserRepository userRepository;


//Post Comment 
  @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
  public Comment PostComment(@RequestBody Comment comment) throws NotavailableException{
    
    //get the comment
    Comment _comment = comment;
    int uw_id = comment.getRatedusrwork();
    //get work  
    Usrworkview getwork = commentRepository.getusrworkusrwork(uw_id);
    int work_id = getwork.getwork_id();


    //verify that work is finished
    String work_State = workRepository.getState(work_id);
    Logger.info(work_State+work_id);
    if(work_State.equals("Completo")){
      Logger.info(""+ _comment.getComment()+" posted");
      commentRepository.save(_comment);

      //change rating avg on profile

      //get id 
      int id = commentRepository.getcommentedusrid(_comment.getRatedusrwork());
      ArrayList<Comment> comments = commentRepository.getcomments(id);
      //modify average work rating
      int avg1 = 0, avg = 0;
      for (int i=0;i< comments.size();i++){
        avg1 = comments.get(i).getRating() + avg1;
        Logger.info(""+comments.get(i).getRating());
      }
      avg = avg1 / comments.size();
      Logger.info("average: "+avg+" all added up "+avg1+" size "+comments.size());
      userRepository.setrating(id, avg);

      //verify if all parties have commented and if its true finish the work
      /* 
      ArrayList<Usrworkview> usrworkarray = commentRepository.getuserworkwork(work_id);
      ArrayList<Comment> commentsarray = commentRepository.getcommentsfromwork(work_id);
      if(usrworkarray.size() == commentsarray.size()){
        Optional<Work> _work = workRepository.findById(work_id);
        if(_work.isPresent()){
            LocalDateTime teste = LocalDateTime.now();
            workRepository.FinishJob(id, teste);

        }else throw new NotFoundException(""+id, "work", "id");
      }*/


      return _comment;

    }else throw new NotavailableException(""+work_id, "work", "Job not finished yet or canceled" );
  } 

//get information before commenting from usrwork
@GetMapping(path = "/usrwork/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
public ArrayList<Usrworkview> getLastWorkInfo(@PathVariable("usrid") int usrid) throws NotFoundException{
  Usrworkview _usrwork= commentRepository.getuserworkusr(usrid);
  ArrayList<Usrworkview> __usrwork = commentRepository.getuserworkwork(_usrwork.getwork_id());

  

  ArrayList<Usrworkview> usrwork = new ArrayList<>();

  for(int i=0; __usrwork.size() > i  ; i++){
    if(__usrwork.get(i).getuser_id() != usrid){
      
      usrwork.add(__usrwork.get(i));
    }
  }
  if (usrwork.size() == 0){throw new NotFoundException("id", "work", "User");}

  return usrwork;
} 
//get comments from usrwork
  @GetMapping(path = "/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Iterable<Comment> getComments(@PathVariable("usrid") int usrid){
    Iterable<Comment> comments = commentRepository.getcomments(usrid);
    return comments;
  }  
         
}
