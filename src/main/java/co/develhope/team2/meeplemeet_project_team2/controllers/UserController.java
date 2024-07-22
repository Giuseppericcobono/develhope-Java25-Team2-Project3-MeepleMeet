package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/create")
    public @ResponseBody User createUser(@RequestBody User user){
        return userRepository.save(user);
    }

    @GetMapping("/list")
    public @ResponseBody List<User> getList() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public @ResponseBody User getWithId(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @PutMapping("/{id}")
    public @ResponseBody User update(@PathVariable Integer id, @RequestBody @NotNull User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }
}
