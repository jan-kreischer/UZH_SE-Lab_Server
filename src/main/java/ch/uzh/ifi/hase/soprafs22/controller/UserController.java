package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO login(@RequestBody UserPostDTO userPostDTO) {
        // convert API user to internal representation
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        System.out.println("Getting user by username");
        Optional<User> user = userService.getUserByUserName(userInput.getUsername());
        if(user.isPresent()) {
            // convert internal representation of user back to API
            User existingUser = user.get();
            existingUser = userService.setUserOnline(existingUser);
            System.out.println("Given password: " + userInput.getPassword());
            System.out.println("Correct password: " + existingUser.getPassword());
            if(existingUser.getPassword().equals(userInput.getPassword())) {
                existingUser.setStatus(UserStatus.ONLINE);
                existingUser.setLoggedIn(true);
                existingUser = userService.update(existingUser);
                return DTOMapper.INSTANCE.convertEntityToUserGetDTO(existingUser);
            }
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username and password do not match!");
            }

        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not exist!");
        }
    }

    @PutMapping(path = "/{userId}/logout")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO logout(@PathVariable long userId) {
      System.out.println("UserId:" + userId);
      Optional<User> user = userService.getUserById(userId);
      if(user.isPresent()){
          user.get().setLoggedIn(false);
          user.get().setStatus(UserStatus.OFFLINE);
          User updatedUser = userService.update(user.get());
          return DTOMapper.INSTANCE.convertEntityToUserGetDTO(updatedUser);
      }
      else {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not exists");
      }
  }

  @GetMapping(path = "/{userIdentifier}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO getUser(@PathVariable String userIdentifier) {
      UserGetDTO userGetDTO;
      Optional<User> user;
      try {
          long userId = Long.parseLong(userIdentifier);
          user = userService.getUserById(userId);
      } catch (NumberFormatException e) {
          System.out.println("Input String cannot be parsed to Integer.");
          String userName = userIdentifier;
          user = userService.getUserByUserName(userName);
      }
      if(user.isPresent()){
          return DTOMapper.INSTANCE.convertEntityToUserGetDTO(user.get());
      }
      else {
          return new UserGetDTO();
      }
    }

  @GetMapping()
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

  @PostMapping(consumes="application/json", produces="application/json")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
    userInput.setStatus(UserStatus.ONLINE);
    userInput.setLoggedIn(true);

    // create user
    User createdUser = userService.createUser(userInput);


    LocalDate date = LocalDate.now();
    System.out.println(date);

    System.out.println("Creating new user");
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }
}
