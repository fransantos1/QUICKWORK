package pt.iade.QUICKWORK.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.repositories.UserRepository;
import pt.iade.QUICKWORK.models.views.UsrJobsView;
import pt.iade.QUICKWORK.models.views.getownerview;

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
    @DeleteMapping(path="/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable("usrid") int usrid){
        User user;
        user = userRepository.findById(usrid).get();
        String name = user.getName();

        Logger.info("Delete user "+name);
        userRepository.deleteById(usrid);
    }

    //get specific user info 
    @GetMapping(path = "/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getUser(@PathVariable("usrid") int usrid){
        Logger.info("User with id: "+ usrid + " given");
        return userRepository.findById(usrid);
    }
    @GetMapping(path = "/owner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Optional<User> getowner(@PathVariable("id") int id) throws NotFoundException{
        Logger.info(id+ "");
        Optional<getownerview> _ownerid = userRepository.getownerid1(id);  
        if(_ownerid.isEmpty()){
            throw new NotFoundException();
        }else{
            getownerview _owner = _ownerid.get();
             return userRepository.findById(_owner.getownerid());
        }

    } 






    //get a users history of work
      @GetMapping(path = "/jobs/{usrid}", produces = MediaType.APPLICATION_JSON_VALUE)
      public Iterable<UsrJobsView> getJobs(@PathVariable("usrid") int usrid) {
          Logger.info("Sending all units");
          return userRepository.findusrjobs(usrid);
      }    

      //user comment on a userwork
      


      //set user location*/-+


    


}   

