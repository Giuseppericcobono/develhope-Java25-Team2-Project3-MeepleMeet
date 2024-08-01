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

    public User updateUser(Integer id, User updatedUsers) {

        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()){
            userRepository.save(updatedUsers);
        } else {
            // Handle the case where the user with the given id is not found
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
        return updatedUsers;
    }
    public User setDeleteStatus(Integer id) {
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRecordStatus(RecordStatus.DELETED);
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("User with ID " + id + " not found.");
        }
    }

}
