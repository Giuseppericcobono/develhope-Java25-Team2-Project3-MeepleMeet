package co.develhope.team2.meeplemeet_project_team2.controllers;

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
    public ResponseEntity<User> create(@RequestBody User user){
        User newUser = userService.createUser(user);
        return ResponseEntity.ok(newUser);
    }

    // search all users
    @GetMapping("/search/list")
    public ResponseEntity<List<User>> getList() {
        List<User> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }

    // search specific user with id
    @GetMapping("/search/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
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

    // update whatever variable of a user found by id
    @PutMapping("/update/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // reactivate user
    @PatchMapping("/reactivate/{id}")
    public ResponseEntity<Void> reactivateUser(@PathVariable Integer id) {
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
    public ResponseEntity<User> deleteLogical(@PathVariable Integer id) {
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
    public ResponseEntity<User> deleteById(@PathVariable Integer id) {
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