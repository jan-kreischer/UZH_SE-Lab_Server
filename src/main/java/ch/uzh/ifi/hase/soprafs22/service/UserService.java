package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<User> getUsers() {
      //return this.userRepository.findAll();
      //List<User> userList = new ArrayList<User>();
      //User user_1 = new User(1, "Jan Bauer", "jaba", "none");
      //userList.add(user_1);
      //User user_2 = new User(2, "Tobias Bayer", "karies_peter", "none");
      //userList.add(user_2);
      //User user_3 = new User(3, "Simon Mayer", "titsi", "none");
      //userList.add(user_3);
      //userList.add(Mockito.mock(User.class));
      //userList.add(Mockito.mock(User.class));
      //return userList;
      return userRepository.findAll();
  }

  public Optional<User> getUserById(Long userId) {
      return userRepository.findById(userId);
  }

  public Optional<User> getUserByUserName(String userName) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(userName));
        if(user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "The user does not exist!");
        }
        else {
            return user;
      }
  }

  public User update(User user) {
      User updatedUser = userRepository.save(user);
      userRepository.flush();
      return updatedUser;
  }

  public User createUser(User newUser) {
    newUser.setToken(UUID.randomUUID().toString());
    newUser.setCreationDate(LocalDate.now());
    checkIfUserExists(newUser);

    // saves the given entity but data is only persisted in the database once
    // flush() is called
    newUser = userRepository.save(newUser);
    userRepository.flush();

    System.out.println(newUser.toString());
    System.out.println("Test");
    log.debug("Created Information for User: {}", newUser);
    return newUser;
  }

  public User setUserOnline(User user) {
      user.setStatus(UserStatus.ONLINE);
      return user;
  }

  public User setUserOffline(User user) {
      user.setStatus(UserStatus.OFFLINE);
      return user;
  }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());
    //User userByName = userRepository.findByName(userToBeCreated.getName());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format(baseErrorMessage, "username", "is"));
    }
  }
}
