package pt.iade.QUICKWORK.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.iade.QUICKWORK.models.repositories.WorkRepository;
import pt.iade.QUICKWORK.models.views.Workmapview;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import pt.iade.QUICKWORK.models.Work;

@RestController
@RequestMapping(path ="/api/work")
public class WorkController {
    private Logger Logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WorkRepository workrepository;

    //get all available jobs (only needs loc and type of job) 
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


    //modify work state
    @PostMapping(path = "/setstate/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void setworkstate(@PathVariable("id") Integer id ,@RequestBody Integer state){
        Logger.info("work "+ id + " set too: "+ state );//! need to send the correct work state

        workrepository.setstate(state, id);//? cant make this work


    }
    //see "owners" info


    //see all comments on a specified work

    //see all people that worked in a job

    //accept work
    //set work location

    
}
