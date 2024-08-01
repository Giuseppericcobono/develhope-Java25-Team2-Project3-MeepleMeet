package co.develhope.team2.meeplemeet_project_team2.controllers;

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

    @PostMapping("/create")
    public ResponseEntity<User> create(@RequestBody User user){
        try {
            user.setRecordStatus(RecordStatus.ACTIVE);
            User newUser = userService.createUser(user);
            return ResponseEntity.ok(newUser);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @GetMapping("/search/list")
    public ResponseEntity<List<User>> getList() {
        List<User> usersList = userService.getAllUsers();
        return ResponseEntity.ok(usersList);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<Optional<User>> getWithId(@PathVariable Integer id) {
        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody @NotNull User user) {
        User updateUser = userService.updateUser(id, user);
        return ResponseEntity.ok(updateUser);
    }

    @PutMapping("/dstatus/{id}")
    public ResponseEntity<User> deleteStatus(@PathVariable Integer id, @NotNull User user) {
        try {
            User updatedUser = userService.setDeleteStatus(id);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

//    @DeleteMapping("/delete/all")
//    public @ResponseBody ResponseEntity<User> deleteAll() {
//        userService.deleteAll();
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}