package pt.iade.QUICKWORK.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.iade.QUICKWORK.models.repositories.WorkRepository;
import pt.iade.QUICKWORK.models.views.Workmapview;
import pt.iade.QUICKWORK.models.exceptions.NotFoundException;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pt.iade.QUICKWORK.models.Work;

@RestController
@RequestMapping(path ="/api/work")
public class WorkController {
    private Logger Logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WorkRepository workrepository;

    //get all available jobs (only needs loc, type and id) 
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<Workmapview> getworks(){
        Logger.info("sending all jobs");

        return workrepository.workmapshow();
    }
    

    //get specific work
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<Work> getwork(@PathVariable("id") int id)throws NotFoundException{
        Optional<Work> _work = workrepository.findById(id);
        if( !_work.isEmpty()) {
            Logger.info("Sending "+ id);
            return _work;
        }else throw new NotFoundException(""+id, "id", "Work" ); 
    }  
    //modify work state
    

    //@GetMapping(path ="/setstate/{id}")
    
    

    //TODO GET WORK STATE
    // * /state/{id} 

    //TODO get work "owner" usr
    // find job creator  


   


    //TODO see all comments on a specified work

    //TODO see all people that worked in a job

    //TODO accept work

    //TODO set work location

    
}
