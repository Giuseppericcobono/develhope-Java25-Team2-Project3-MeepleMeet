package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
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

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user){
        User newUser = userService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/search/list")
    public @ResponseBody ResponseEntity<List<User>> getList() {
        List<User> usersList = userService.getAllUsers();
        return new ResponseEntity<>(usersList, HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public @ResponseBody ResponseEntity<Optional<User>> getWithId(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public @ResponseBody ResponseEntity<User> update(@PathVariable Integer id, @RequestBody @NotNull User user) {
        User updateUser = userService.updateUser(id,user);
        return new ResponseEntity<>(updateUser,HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public @ResponseBody ResponseEntity<User> delete(@PathVariable Integer id, @RequestBody @NotNull User user) {
        if(userService.getUserById(id).isPresent()){
            User deletedUser = userService.updateUser(id, user);
            deletedUser.setRecordStatus(RecordStatus.DELETED);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

//    @DeleteMapping("/delete/all")
//    public @ResponseBody ResponseEntity<User> deleteAll() {
//        userService.deleteAll();
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}