package pt.iade.QUICKWORK.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)

public class NotavailableException extends RuntimeException{
    private static final long serialVersionUID = 1234567L;

    public NotavailableException(String id, String elemType, String Reason) {

        super(elemType+" with the id: "+id+" not available. Reason: "+ Reason);
        
    } 
    
    
}






