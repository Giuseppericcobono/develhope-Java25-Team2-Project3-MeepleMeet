package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id){
        return userRepository.findById(id);
    }

    // updates a user without re-writing the whole body.
    public User updateUser(Integer id, User updateUser) {
        // finds the existing user with the id.
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()){
            User existingUser = userOptional.get();

            // updates only the variables given in the body on postman.
            if (updateUser.getUsername() != null) {
                existingUser.setUsername(updateUser.getUsername());
            }
            if (updateUser.getFirstName() != null) {
                existingUser.setFirstName(updateUser.getFirstName());
            }
            if (updateUser.getLastName() != null) {
                existingUser.setLastName(updateUser.getLastName());
            }
            if (updateUser.getAge() != null) {
                existingUser.setAge(updateUser.getAge());
            }
            if (updateUser.getEmail() != null) {
                existingUser.setEmail(updateUser.getEmail());
            }
            if (updateUser.getUserType() != null) {
                existingUser.setUserType(updateUser.getUserType());
            }
            if (updateUser.getRecordStatus() != null) {
                existingUser.setRecordStatus(updateUser.getRecordStatus());
            }

            // saves the updated user in the db.
            userRepository.save(existingUser);
            return existingUser;
        } else {
            // case where the user with the specified id is not found.
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }
}