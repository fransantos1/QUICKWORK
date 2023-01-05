package pt.iade.QUICKWORK.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pt.iade.QUICKWORK.models.Response;
import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.usrloc;
import pt.iade.QUICKWORK.models.usrworkinfo;
import pt.iade.QUICKWORK.models.repositories.UserRepository;
import pt.iade.QUICKWORK.models.views.UsrJobsView;
import pt.iade.QUICKWORK.models.views.UsrLocview;
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
        Logger.info("Sending all usrs");
        return userRepository.findAll();
    }

    
    //get user location
    @GetMapping(path = "/{usrid}/location", produces =  MediaType.APPLICATION_JSON_VALUE)
    public UsrLocview getLocation(@PathVariable("usrid") int id) throws NotFoundException{   
        Optional<User> _user = userRepository.findById(id);
        if(!_user.isEmpty()){
            return userRepository.getlocation(id);
        }else throw new NotFoundException(""+id, "User", "id");
    }
    //update user location
    @PatchMapping(path = "/{usrid}/location", produces =  MediaType.APPLICATION_JSON_VALUE)
    public void setLocation(@PathVariable("usrid") int id, @RequestBody usrloc loc){
            userRepository.setlocation(id, loc.getLat(), loc.getLon());
    }
    //add a job
    @PatchMapping(path = "/{usrid}/addjob", produces =  MediaType.APPLICATION_JSON_VALUE)
    public void addnjob(@PathVariable("usrid") int id){
        User user = userRepository.findById(id).get();
        int njob = user.getJobnumber();
        userRepository.setnjob(id, njob+1);
    }




    //add user
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public User saveUsr(@RequestBody User usr) {
        User _user = usr;
        _user.setJobnumber(0);

        Logger.info("User named: "+usr.getName()+" saved");
        return userRepository.save(_user);
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

    //getuser
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
            throw new NotFoundException(""+id, "work", "id");
        }else{
            getownerview _owner = _ownerid.get();
             return userRepository.findById(_owner.getownerid());
        }

    } 
    //is user occupied
    @GetMapping(path ="/{usrid}/occupied", produces = MediaType.APPLICATION_JSON_VALUE)
    public usrworkinfo getBoolean(@PathVariable("usrid") int usrid){
        usrworkinfo response = null;
        if( userRepository.isUserOccupied(usrid)){
            response = new usrworkinfo(true, userRepository.isUserowner(usrid));
        }else{ response = new usrworkinfo(false);}
        return response;
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
    
    


}   

