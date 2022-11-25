package pt.iade.QUICKWORK.models.repositories;

import org.springframework.data.repository.CrudRepository;
import pt.iade.QUICKWORK.models.Work;

public interface WorkRepository extends CrudRepository<Work,Integer> {
  
}
