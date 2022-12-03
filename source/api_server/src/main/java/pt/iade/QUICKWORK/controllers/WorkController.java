package pt.iade.QUICKWORK.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.iade.QUICKWORK.models.repositories.UserRepository;
import pt.iade.QUICKWORK.models.repositories.WorkRepository;
import pt.iade.QUICKWORK.models.views.Workmapview;
import pt.iade.QUICKWORK.models.views.getownerview;
import pt.iade.QUICKWORK.models.views.gettypeView;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.Work;

@RestController
@RequestMapping(path ="/api/work")
public class WorkController {
    private Logger Logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WorkRepository workrepository;
    private UserRepository userRepository;





    //get all available jobs (only needs loc, type and id) 
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Workmapview> getworks(){

        Logger.info("sending all jobs");

        return workrepository.workmapshow();
    }
    

    //get specific work
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Work> getwork(@PathVariable("id") int id) {
        Logger.info("Sending "+ id);
        return workrepository.findById(id);
    }  


    //get specific type
    @GetMapping(path = "/type/{id:[2-5]}", produces = MediaType.APPLICATION_JSON_VALUE)
    public gettypeView gettype(@PathVariable("id") int id){


        return workrepository.gettype(id);
    }
    //modify work state
    //! THIS IS WRONG I'M MODIFYING WORK TYPE NEED TO CHANGE THIS ASAP
    @PostMapping(path = "/setstate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void setworkstate(@PathVariable("id") Integer id ,@RequestBody Integer state){
        if(workrepository.existsById(id) != false){
            workrepository.setstate(state, id);
            Logger.info("work "+ id + " set too: "+ state );//! need to send the correct work state
        }else {Logger.error("work doesnt exist");}


    }

    //TODO GET WORK STATE
    // * /state/{id} 

    //TODO get work "owner" usr
    // find job creator  






    //TODO see all comments on a specified work

    //TODO see all people that worked in a job

    //TODO accept work

    //TODO set work location

    
}
