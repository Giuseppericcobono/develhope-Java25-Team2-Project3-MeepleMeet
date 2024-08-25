package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.Rating;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.ReviewRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    //todo: sistemare logica per il rating (da aggiungere anche al get all?)
    public Optional<User> getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            user.setLastActivityDate(LocalDateTime.now());
            userRepository.save(user);

            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }

    public List<User> listOfUsersByStatus(String status) {
        List<User> users;
        switch (status) {
            case "active" -> {
                users = userRepository.recordStatusEntity(RecordStatus.ACTIVE);
                return users;
            }
            case "inactive" -> {
                users = userRepository.recordStatusEntity(RecordStatus.INACTIVE);
                return users;
            }
            case "deleted" -> {
                users = userRepository.recordStatusEntity(RecordStatus.DELETED);
                return users;
            }
            default -> {
                return null;
            }
        }
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
            if (updateUser.getBirth() != null) {
                existingUser.setBirth(updateUser.getBirth());
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
            if (updateUser.getBiography() != null) {
                existingUser.setBiography(updateUser.getBiography());
            }
            if (updateUser.getLastActivityDate() != null) {
                existingUser.setLastActivityDate(updateUser.getLastActivityDate());
            }

            // saves the updated user in the db.
            userRepository.save(existingUser);
            return existingUser;
        } else {
            // case where the user with the specified id is not found.
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    // updates the age of a user based on birth
    public void updateAgeBasedOnBirth(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setAge(calculateAge(user.getBirth()));
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
    }

    // calculates age of a user
    private Byte calculateAge(LocalDate birth) {
        if (birth == null) {
            return null;
        }
        return (byte) Period.between(birth, LocalDate.now()).getYears();
    }

    // sets the star rating of an existing user when you call the get mapping with id
    public void averageRating(Integer userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setStarRating(averageStarRating(userId));
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User with id " + userId + " not found");
        }
    }

    // calculates the average rating for the specified user
    public String averageStarRating(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if(user.isPresent()) {
            User user1 = user.get();
            Double sumRating = 0.0;
            for (Review review : reviewRepository.reviewsOfOneUser(user1)) {
                sumRating += review.getRating().getValue();
            }
            sumRating /= reviewRepository.reviewsOfOneUser(user1).size();

            String starRating = "No ratings found";
            for (Rating rating : Rating.values()) {
                if (rating.getValue().equals(sumRating)) {
                    starRating = rating.getStars();
                } else if(sumRating <= 0.5) {
                    starRating = Rating.STAR0.getStars();
                } else if(sumRating > 0.5 && sumRating <= 1.5) {
                    starRating = Rating.STAR1.getStars();
                } else if(sumRating > 1.5 && sumRating <= 2.5) {
                    starRating = Rating.STAR2.getStars();
                } else if(sumRating > 2.5 && sumRating <= 3.5) {
                    starRating = Rating.STAR3.getStars();
                } else if(sumRating > 3.5 && sumRating <= 4.5) {
                    starRating = Rating.STAR4.getStars();
                } else if(sumRating > 4.5) {
                    starRating = Rating.STAR5.getStars();
                }
            } return starRating;
        } else {
            return null;
        }
    }

    // controls if a user has been inactive for more than 6 months and if so sets the status to inactive
    @Scheduled(cron = "0 0 0 * * ?") // Daily execution at midnight
    public void updateInactiveUsers() {
        List<User> users = userRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (User user : users) {
            if (user.getLastActivityDate() != null &&
                    ChronoUnit.MONTHS.between(user.getLastActivityDate(), now) >= 6 &&
                    user.getRecordStatus() == RecordStatus.ACTIVE) {

                user.setRecordStatus(RecordStatus.INACTIVE);
                userRepository.save(user);
            }
        }
    }

    // reactivate user
    public void reactivationOfUser(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent() && userOptional.get().getRecordStatus() != RecordStatus.ACTIVE) {
            User user = userOptional.get();
            user.setRecordStatus(RecordStatus.ACTIVE);
            userRepository.saveAndFlush(user);
        } else if (userOptional.isPresent() && userOptional.get().getRecordStatus().equals(RecordStatus.ACTIVE)){
            throw new IllegalArgumentException("User with id: " + id + " is already active");
        } else {
            throw new EntityNotFoundException("User with id: " + id + " doesn't exist");
        }
    }

    // logical deletion
    public void deleteLogical(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setRecordStatus(RecordStatus.DELETED);
            userRepository.save(user);
        } else {
            throw new EntityNotFoundException("User with id: " + id + " doesn't exist");
        }
    }

    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }
}