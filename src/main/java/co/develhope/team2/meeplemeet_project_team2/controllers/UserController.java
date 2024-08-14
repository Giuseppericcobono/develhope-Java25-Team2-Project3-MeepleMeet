package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.services.UserService;
import org.antlr.v4.runtime.misc.NotNull;
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
    public ResponseEntity<Optional<User>> getById(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // update whatever variable of a user found by id (also for logical deletion)
    @PutMapping("/update/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updatedUser);
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