package pt.iade.QUICKWORK.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.iade.QUICKWORK.models.WorkType;
import pt.iade.QUICKWORK.models.repositories.WorkTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(path ="/api/worktype")
public class WorkTypeController {
    private Logger Logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WorkTypeRepository worktyperepository;


    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<WorkType> getTypes() {
        Logger.info("Sending all Types");
        return worktyperepository.findAll();
    }
}
