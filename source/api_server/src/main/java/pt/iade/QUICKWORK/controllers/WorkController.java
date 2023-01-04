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

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    //get all users from a work
    @GetMapping(path = "/users/{workid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<customusrview> getusers(@PathVariable("workid") int id)throws NotFoundException{
        Optional<Work> _work= workrepository.findById(id);
        if (_work.isPresent()){
            ArrayList<customusrview> test = workrepository.getusers(id);
        return test;
        } else throw new NotFoundException(""+id, "id", "work" );
    }
   


    //get all available jobs (only needs loc, type and id) 
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Workmapview> getworks(){
        ArrayList<Workmapview> _workmap = workrepository.workmapshow();
        ArrayList<Workmapview> workmap = new ArrayList<>();
        for(int i = 0; i< _workmap.size(); i++){
                Workmapview temp = _workmap.get(i);
                String state = workrepository.getState(temp.getid());
                if(state.equals("Em espera")){
                    workmap.add(temp);
                }
            }
        return workmap;
    }
    
    //get work from owner 
    @GetMapping(path = "/owner/{usrid}")
    public workview getworkfromowner(@PathVariable("usrid")int usr_id)throws NotFoundException{
        Optional<User> usr = userRepository.findById(usr_id);
        if( usr.isPresent()) {

            workview _work= workrepository.getworkfromowner(usr_id);
            return _work;
        }else throw new NotFoundException(""+usr_id, "id", "usr" ); 
    }





    
    //get work from working usr
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
    // cancel job
    @DeleteMapping(path="/cancel/{workid}",produces = MediaType.APPLICATION_JSON_VALUE)
    public void CancelJob(@PathVariable("workid") int id)throws NotFoundException{
        Optional<Work> _work = workrepository.findById(id);
        if(_work.isPresent()){
            workrepository.deleteJob(id);
        }else throw new NotFoundException(""+id, "work", "id");
        
        
    }
    //finish job
    @PatchMapping(path= "/finish/{workid}", produces =MediaType.APPLICATION_JSON_VALUE)
    public void FinishJob(@PathVariable("workid") int id)throws NotFoundException{
        Optional<Work> _work = workrepository.findById(id);
        if(_work.isPresent()){
            LocalDateTime teste = LocalDateTime.now();
            workrepository.FinishJob(id, teste);

        }else throw new NotFoundException(""+id, "work", "id");
        
        
    }










    //get the state a work is on
    @GetMapping(path="/getState/{workid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getState(@PathVariable("workid") int id) throws NotFoundException{
        Optional<Work> _work = workrepository.findById(id);
        if(_work.isPresent()){
            String state = workrepository.getState(id);

            return state;

        }else throw new NotFoundException(""+id, "work", "id");
    }

    //acceptWork
    @PatchMapping(path="/accept/{usrid}/{workid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void acceptwork(@PathVariable("workid") int workid, @PathVariable("usrid") int usrid){
        LocalDateTime starting = LocalDateTime.now();
        //modify work
        workrepository.acceptWork(starting, workid);
        //add dependecies   
        workrepository.acceptWork1(usrid, workid);
        //change state
        workrepository.setState(workid, 2);
    } 





    //TODO see all comments on a specified work

    //TODO see all people that worked in a job



    
}
