package pt.iade.QUICKWORK.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.iade.QUICKWORK.models.repositories.UserRepository;
import pt.iade.QUICKWORK.models.repositories.WorkRepository;
import pt.iade.QUICKWORK.models.views.Workmapview;
import pt.iade.QUICKWORK.models.views.customusrview;
import pt.iade.QUICKWORK.models.views.workview;
import pt.iade.QUICKWORK.models.exceptions.NotFoundException;
import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.Work;

import java.util.ArrayList;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path ="/api/work")
public class WorkController {
    private Logger Logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WorkRepository workrepository;
    @Autowired
    private UserRepository userRepository;

    //TODO NEED TO VERIFY if user isnt occupied
    // create a job and do all the necessery inserts
    @PutMapping(path ="/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Work savework(@RequestBody Work work, @PathVariable("usrid") int usrid)throws NotFoundException{
        Optional<User> _user = userRepository.findById(usrid);
        if(_user.isPresent()){
            workrepository.savework(work.getLat(), work.getLon(), work.getPricehr(), work.getTypeid());

            Logger.info("work added\nAdding foreighn keys");
            Work _work = workrepository.findByLatAndLon(work.getLat(), work.getLon());
            Logger.info("id: "+ _work.getId());
            workrepository.AddDependencies(_work.getId(),usrid);
            return _work;
        }else throw new NotFoundException(""+usrid, "user", "id");

    }
    @GetMapping(path = "/users/{workid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<customusrview> getusers(@PathVariable("workid") int id)throws NotFoundException{
        Optional<Work> _work= workrepository.findById(id);
        if (_work.isPresent()){
            ArrayList<customusrview> test = workrepository.getusers(id);
            Logger.info(test.get(1).getname());



        return test;
        } else throw new NotFoundException(""+id, "id", "work" );
    }



    //DEBUG 
    @GetMapping(path = "/debug/{workid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Work getworkdebug(@PathVariable("workid") int id){
        Logger.info("sending all jobs");

        return workrepository.findById(id).get();
    }
    



    //get all available jobs (only needs loc, type and id) 
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Workmapview> getworks(){
        Logger.info("sending all jobs");

        return workrepository.workmapshow();
    }
    
    
    //get work from usr
    @GetMapping(path = "/user/{usrid}")
    public workview getworkfromworker(@PathVariable("usrid")int usr_id)throws NotFoundException{
        Optional<User> usr = userRepository.findById(usr_id);
        if( usr.isPresent()) {
            workview _work= workrepository.getworkfromusr(usr_id, 2);
            return _work;
        }else throw new NotFoundException(""+usr_id, "id", "usr" ); 
    }

    //get specific work
    @GetMapping(path = "/{workid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public workview getwork(@PathVariable("workid") int id)throws NotFoundException{

        workview _work = workrepository.getworkandtype(id);

        if( _work != null) {
            Logger.info("Sending "+ id);
            return _work;
        }else throw new NotFoundException(""+id, "id", "Work" ); 
    }  
    //TODO modify work state
    
    //TODO see all comments on a specified work

    //TODO see all people that worked in a job

    //TODO accept work


    
}
