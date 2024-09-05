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
    public ResponseEntity<Optional<List<UserReturnDTO>>> usersList() {
        Optional<List<UserReturnDTO>> usersList = userService.getAllUsers();
        if(usersList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usersList);
    }

    // search specific user with id
    @GetMapping("/search/{id}")
    public ResponseEntity<UserReturnDTO> userById(@PathVariable Integer id) {
        Optional<UserReturnDTO> user = userService.getUserById(id);
        if(user.isPresent()) {
            UserReturnDTO user1 = user.get();
            return ResponseEntity.ok(user1);
        }
        return ResponseEntity.badRequest().build();
    }

    // search users by record status (active, inactive or deleted)
    @GetMapping("/search/list/{status}")
    public ResponseEntity<List<UserReturnDTO>> usersListByStatus(@PathVariable String status) {
        Optional<List<UserReturnDTO>> users = userService.listOfUsersByStatus(status);
        List<UserReturnDTO> users1 = users.get();
        return ResponseEntity.ok(users1);
    }

    // search users by username
    @GetMapping("/search/username")
    public ResponseEntity<Optional<UserReturnDTO>> usersByUsername(@RequestParam String username) {
        Optional<UserReturnDTO> user = userService.getUsersByUsername(username);
        if(user.isPresent()) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.badRequest().build();
    }

    // search users by first name
    @GetMapping("/search/firstName")
    public ResponseEntity<Optional<List<UserReturnDTO>>> usersByFirstName(@RequestParam String firstName) {
        Optional<List<UserReturnDTO>> users = userService.getUsersByFirstName(firstName);
        if(users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    // search users by last name
    @GetMapping("/search/lastName")
    public ResponseEntity<Optional<List<UserReturnDTO>>> usersByLastName(@RequestParam String lastName) {
        Optional<List<UserReturnDTO>> users = userService.getUsersByLastName(lastName);
        if(users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    // search users by full name
    @GetMapping("/search/fullName")
    public ResponseEntity<Optional<List<UserReturnDTO>>> usersByFullName(@RequestParam String firstName, String lastName) {
        Optional<List<UserReturnDTO>> users = userService.getUsersByFullName(firstName, lastName);
        if(users.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(users);
    }

    // Find the list of events a user is subscribed to
    @GetMapping("search/list/events")
    public ResponseEntity<Optional<List<Event>>> listOfEvents(@RequestParam(name = "userID") Integer id) {
        Optional<List<Event>> events = userService.listOfEventsParticipate(id);
        if(events.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(events);
    }

    // search for a user's list of reviews
    @GetMapping("search/list/reviews")
    public ResponseEntity<Optional<List<ReviewDTO>>> listOfReviews(@RequestParam(name = "userID") Integer id) {
        Optional<List<ReviewDTO>> reviews = userService.getAllReviewOfAUserById(id);
        if(reviews.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
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
        if(userReturnDTO.isPresent()) {
            User user = new User();
            BeanUtils.copyProperties(userReturnDTO, user);
            userService.reactivationOfUser(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    // logical deletion
    @DeleteMapping("delete/logical/{id}")
    public ResponseEntity<User> deletionLogical(@PathVariable Integer id) {
        Optional<UserReturnDTO> userReturnDTO = userService.getUserById(id);
        if(userReturnDTO.isPresent()) {
            userService.deleteLogical(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    // delete user with id
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<User> deletionById(@PathVariable Integer id) {
        Optional<UserReturnDTO> userReturnDTO = userService.getUserById(id);
        if(userReturnDTO.isPresent()) {
            userService.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    //delete all users
    @DeleteMapping("/delete/all")
    public ResponseEntity<User> deleteAll() {
        userService.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}