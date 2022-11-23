package pt.iade.QUICKWORK.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import pt.iade.QUICKWORK.models.User;
import pt.iade.QUICKWORK.models.repositories.UserRepository;


@RestController
@RequestMapping(path ="/api/users")
public class UserController {
    private Logger Logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    
     //get all Users
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<User> getUsers() {
    Logger.info("Sending all units");
    return userRepository.findAll();
    }
    //add user
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody User user){
        Logger.info("deleting user");
        
    

        
    }
    

    //remove a user 

    
}
