package pt.iade.QUICKWORK.models.repositories;

import org.springframework.data.repository.CrudRepository;
import pt.iade.QUICKWORK.models.User;


public interface UserRepository extends CrudRepository<User,Integer> {
  
}
