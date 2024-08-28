package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.DTO.UserDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    // create new user
    @PostMapping("/create")
    public ResponseEntity<User> newUser(@RequestBody UserDTO userDTO){
        User newUser = userService.createUser(userDTO);
        return ResponseEntity.ok(newUser);
    }

    // search all users
    @GetMapping("/search/list")
    public ResponseEntity<List<User>> usersList() {
        List<User> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }

    // search specific user with id
    @GetMapping("/search/{id}")
    public ResponseEntity<User> userById(@PathVariable Integer id) {
        Optional<User> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // search users by record status (active, inactive or deleted)
    @GetMapping("/search/list/{status}")
    public ResponseEntity<List<User>> usersListByStatus(@PathVariable String status) {
        List<User> users = userService.listOfUsersByStatus(status);
        return ResponseEntity.ok(users);
    }

    // search users by username
    @GetMapping("/search/username")
    public ResponseEntity<List<User>> usersByUsername(@RequestParam String username){
        List<User> users = userService.getUsersByUsername(username);
        return ResponseEntity.ok(users);
    }

    // search users by first name
    @GetMapping("/search/firstName")
    public ResponseEntity<List<User>> usersByFirstName(@RequestParam String firstName){
        List<User> users = userService.getUsersByFirstName(firstName);
        return ResponseEntity.ok(users);
    }

    // search users by last name
    @GetMapping("/search/lastName")
    public ResponseEntity<List<User>> usersByLastName(@RequestParam String lastName){
        List<User> users = userService.getUsersByLastName(lastName);
        return ResponseEntity.ok(users);
    }

    // search users by full name
    @GetMapping("/search/fullName")
    public ResponseEntity<List<User>> usersByFullName(@RequestParam String firstName, String lastName){
        List<User> users = userService.getUsersByFullName(firstName, lastName);
        return ResponseEntity.ok(users);
    }

    @GetMapping("search/list/events")
    public ResponseEntity<List<Event>> listOfEvents (@RequestParam(name = "userID") Integer id) {
        List<Event> events = userService.listOfEventsPartecipate(id);
        return ResponseEntity.ok(events);
    }

    // update whatever variable of a user found by id
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
            Optional<User> updatedUser = userService.updateUser(id, user);
            if(updatedUser.isPresent()){
                return ResponseEntity.ok(updatedUser.get());
            } else {
                return ResponseEntity.notFound().build();
            }
    }

    // reactivate user
    @PatchMapping("/reactivate/{id}")
    public ResponseEntity<Void> reactivationOfUser(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isPresent()) {
            userService.reactivationOfUser(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // logical deletion
    @DeleteMapping("delete/logical/{id}")
    public ResponseEntity<User> deletionLogical(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isPresent()) {
            userService.deleteLogical(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // delete user with id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deletionById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        if(user.isPresent()){
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    //delete all users
    @DeleteMapping("/delete/all")
    public ResponseEntity<User> deleteAll() {
        userService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }
}