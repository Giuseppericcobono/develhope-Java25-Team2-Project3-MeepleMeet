package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.DTO.UserDTO;
import co.develhope.team2.meeplemeet_project_team2.DTO.ReviewDTO;
import co.develhope.team2.meeplemeet_project_team2.DTO.UserReturnDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.services.UserService;
import org.springframework.beans.BeanUtils;
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
    public ResponseEntity<Optional<User>> newUser(@RequestBody UserDTO userDTO) {
        Optional<User> newUser = userService.createUser(userDTO);
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
        Optional<User> user = userService.getUserById(id);
        User user1 = user.get();
        return ResponseEntity.ok(user1);
    }

    // search users by record status (active, inactive or deleted)
    @GetMapping("/search/list/{status}")
    public ResponseEntity<List<UserReturnDTO>> usersListByStatus(@PathVariable String status) {
        List<User> users = userService.listOfUsersByStatus(status);
        List<UserReturnDTO> users1 = users.get();
        return ResponseEntity.ok(users1);
    }

    // search users by username
    @GetMapping("/search/username")
    public ResponseEntity<List<UserReturnDTO>> usersByUsername(@RequestParam String username) {
        List<User> users = userService.getUsersByUsername(username);
        List<UserReturnDTO> users1 = users.get();
        return ResponseEntity.ok(users1);
    }

    // search users by first name
    @GetMapping("/search/firstName")
    public ResponseEntity<List<UserReturnDTO>> usersByFirstName(@RequestParam String firstName) {
        Optional<List<UserReturnDTO>> users = userService.getUsersByFirstName(firstName);
        List<UserReturnDTO> users1 = users.get();
        return ResponseEntity.ok(users1);
    }

    // search users by last name
    @GetMapping("/search/lastName")
    public ResponseEntity<List<UserReturnDTO>> usersByLastName(@RequestParam String lastName) {
        Optional<List<UserReturnDTO>> users = userService.getUsersByLastName(lastName);
        List<UserReturnDTO> users1 = users.get();
        return ResponseEntity.ok(users1);
    }

    // search users by full name
    @GetMapping("/search/fullName")
    public ResponseEntity<List<UserReturnDTO>> usersByFullName(@RequestParam String firstName, String lastName) {
        Optional<List<UserReturnDTO>> users = userService.getUsersByFullName(firstName, lastName);
        List<UserReturnDTO> users1 = users.get();
        return ResponseEntity.ok(users1);
    }

    // Find the list of events a user is subscribed to
    @GetMapping("search/list/events")
    public ResponseEntity<List<Event>> listOfEvents(@RequestParam(name = "userID") Integer id) {
        Optional<List<Event>> events = userService.listOfEventsParticipate(id);
        List<Event> events1 = events.get();
        return ResponseEntity.ok(events1);
    }

    // search for a user's list of reviews
    @GetMapping("search/list/reviews")
    public ResponseEntity<List<ReviewDTO>> listOfReviews(@RequestParam(name = "userID") Integer id) {
        List<ReviewDTO> reviews = userService.getAllReviewOfAUserById(id);
        return ResponseEntity.ok(reviews);
    }

    // update whatever variable of a user found by id
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        Optional<User> updatedUser = userService.updateUser(id, user);
        if (updatedUser.isPresent()) {
            return ResponseEntity.ok(updatedUser.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // reactivate user
    @PatchMapping("/reactivate/{id}")
    public ResponseEntity<Void> reactivationOfUser(@PathVariable Integer id) {
        Optional<UserReturnDTO> userReturnDTO = userService.getUserById(id);
        User user = new User();
        BeanUtils.copyProperties(userReturnDTO, user);
        userService.reactivationOfUser(id);
        return ResponseEntity.ok().build();
    }

    // logical deletion
    @DeleteMapping("delete/logical/{id}")
    public ResponseEntity<User> deletionLogical(@PathVariable Integer id) {
        Optional<UserReturnDTO> userReturnDTO = userService.getUserById(id);
        userService.deleteLogical(id);
        return ResponseEntity.ok().build();
    }

    // delete user with id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deletionById(@PathVariable Integer id) {
        Optional<UserReturnDTO> userReturnDTO = userService.getUserById(id);
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    //delete all users
    @DeleteMapping("/delete/all")
    public ResponseEntity<User> deleteAll() {
        userService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}