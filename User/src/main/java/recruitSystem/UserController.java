package recruitSystem;

import org.apache.catalina.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
 public class UserController {

  @Autowired
  UserRepository userRepository;

  @PostMapping("/users/userCreation")
  User userInsert(@RequestBody UserCreated userCreated) {
   User user = new User();
   user.setUserName(userCreated.getUserName());
   return userRepository.save(user);
  }

 @RequestMapping(method= RequestMethod.PATCH, path="/users/userModify")
 public void userModify(@RequestBody UserUpdated userUpdated){
   User user = new User();
   user.setId(userUpdated.getId());
   user.setUserName(userUpdated.getUserName());
   userRepository.save(user);
 }



 }
