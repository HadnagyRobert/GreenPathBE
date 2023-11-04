package fontys.group.greenpath.greenpaths.business.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameExistsException extends ResponseStatusException{
    public UsernameExistsException(){
        super(HttpStatus.BAD_REQUEST, "USERNAME_ALREADY_EXISTS");
    }
}
