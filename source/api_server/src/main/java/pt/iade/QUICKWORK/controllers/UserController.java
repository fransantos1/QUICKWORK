package pt.iade.QUICKWORK.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

import pt.iade.QUICKWORK.models.Response;
import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.repositories.UserRepository;
import pt.iade.QUICKWORK.models.views.UsrJobsView;
import pt.iade.QUICKWORK.models.views.getownerview;
import pt.iade.QUICKWORK.models.exceptions.NotFoundException;
@RestController
@RequestMapping(path ="/api/users")
public class UserController {
    private Logger Logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserRepository userRepository;
    // for debug porpuses, show all users
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<User> getUsers() {
        Logger.info("Sending all units");
        return userRepository.findAll();
    }
    //add user
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public User saveUsr(@RequestBody User usr) {
        Logger.info("User named: "+usr.getName()+" saved");
        return userRepository.save(usr);
    }   



    //remove a user 
    //TODO verify the foreign key constrains, and either delete comments/reports/jobs or create a user to do that 
    @DeleteMapping(path="/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response delete(@PathVariable("usrid") int usrid) throws NotFoundException{
        Optional<User> user1 = userRepository.findById(usrid);
        if(!user1.isEmpty()){
            User user = user1.get();
            String name = user.getName();
            Logger.info("Delete user "+name);
            userRepository.deleteById(usrid);
            return new Response(usrid+"was deleted", null);
        }else throw new NotFoundException(""+usrid, "User", "id");
    }

    //get specific user info 
    @GetMapping(path = "/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getUser(@PathVariable("usrid") int usrid) throws NotFoundException{
        Optional<User> _user = userRepository.findById(usrid);
        if(_user.isPresent()){
            Logger.info("sending User: "+usrid);
            return _user;
        }else throw new NotFoundException(""+usrid, "user", "id");
    }

    // sends the owner of a specific job

    @GetMapping(path = "/owner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getowner(@PathVariable("id") int id) throws NotFoundException{
        Logger.info(id+ "");
        Optional<getownerview> _ownerid = userRepository.getownerid1(id);  
        if(_ownerid.isEmpty()){
            throw new NotFoundException(""+id, "user", "id");
        }else{
            getownerview _owner = _ownerid.get();
             return userRepository.findById(_owner.getownerid());
        }

    } 
    //get a users history of work
      @GetMapping(path = "/jobs/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
      public Iterable<UsrJobsView> getJobs(@PathVariable("usrid") int usrid) throws NotFoundException{
            Optional<User> _user = userRepository.findById(usrid);
            if(!_user.isEmpty()){
            Iterable<UsrJobsView> jobs = userRepository.findusrjobs(usrid);
            Logger.info("Sending All users "+ usrid+" jobs");
            return jobs;
            }else throw new NotFoundException(""+usrid, "User", "Id");
      }    

      //set user location*/-+


    


}   

