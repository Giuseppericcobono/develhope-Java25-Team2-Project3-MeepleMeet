package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.ReviewDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.Rating;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.EventRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.ReviewRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public User createUser(User user){
        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public List<User> getUsersByUsername(String username){
        List<User> users = userRepository.findUsersByUsername(username);
        if(users.isEmpty()){
            throw new EntityNotFoundException("The user with username: " + username + " doesn't exist, or is deleted or inactive");
        }
        return users;
    }

    public List<User> getUsersByFirstName(String firstName){
        List<User> users = userRepository.findUsersByFirstName(firstName);
        if(users.isEmpty()){
            throw new EntityNotFoundException("The user with first name: " + firstName + " doesn't exist, or is deleted or inactive");
        }
        return users;
    }

    public List<User> getUsersByLastName(String lastName){
        List<User> users = userRepository.findUsersByLastName(lastName);
        if(users.isEmpty()){
            throw new EntityNotFoundException("The user with last name: " + lastName + " doesn't exist, or is deleted or inactive");
        }
        return users;
    }

    public List<User> getUsersByFullName(String firstName, String lastName){
        List<User> users = userRepository.findUsersByFirstAndLastName(firstName, lastName);
        if(users.isEmpty()){
            throw new EntityNotFoundException("The user: " + firstName + " " + lastName + " doesn't exist, or is deleted or inactive");
        }
        return users;
    }

    // returns users based on their status
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

            // if the birthdate is provided, calculates and updates the age
            if (updateUser.getBirth() != null) {
                Byte calculatedAge = (byte) ChronoUnit.YEARS.between(updateUser.getBirth(), LocalDate.now());
                existingUser.setBirth(updateUser.getBirth());
                existingUser.setAge(calculatedAge);
            }

            // if the age is provided without a birthdate, validates it against the existing birthdate
            if (updateUser.getAge() != null && updateUser.getBirth() == null) {
                Byte calculatedAge = (byte) ChronoUnit.YEARS.between(existingUser.getBirth(), LocalDate.now());
                if (!calculatedAge.equals(updateUser.getAge())) {
                    throw new IllegalArgumentException("The age provided does not correspond to birthdate");
                }
                existingUser.setAge(updateUser.getAge());
            }

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
            //sets the last activity to now
            existingUser.setLastActivityDate(LocalDateTime.now());

            // saves the updated user in the db.
            userRepository.save(existingUser);
            return existingUser;
        } else {
            // case where the user with the specified id is not found.
            throw new EntityNotFoundException("User with id " + id + " not found");
        }
    }

    //updates the age of all users based on birthdate
    @Scheduled(cron = "0 0 0 * * ?") // daily execution at midnight
    @Transactional
    public void updateAllUserAge() {
        List<User> users = userRepository.findAll();
        LocalDate today = LocalDate.now();

        for (User user : users) {
            Byte newAge = (byte) ChronoUnit.YEARS.between(user.getBirth(), today);
            user.setAge(newAge);
            userRepository.save(user);
        }
    }

    // sets the star rating of an existing user
    @Scheduled(fixedRate = 60000) // execution every minute
    @Transactional
    public void updateAllUserRatings() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            user.setStarRating(averageStarRating(user.getUserId()));
            userRepository.save(user);
        }
    }

    // calculates average rating of a user
    public String averageStarRating(Integer userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            User user1 = user.get();
            Double sumRating = 0.0;
            List<Review> reviews = reviewRepository.reviewsOfOneUser(user1);
            for (Review review : reviews) {
                sumRating += review.getRating().getValue();
            }

            if (!reviews.isEmpty()) {
                sumRating /= reviews.size();
            } else {
                return "No ratings found";
            }

            String starRating = Rating.STAR0.getStars();
            for (Rating rating : Rating.values()) {
                if (rating.getValue().equals(sumRating)) {
                    starRating = rating.getStars();
                } else if (sumRating <= 0.5) {
                    starRating = Rating.STAR0.getStars();
                } else if (sumRating > 0.5 && sumRating <= 1.5) {
                    starRating = Rating.STAR1.getStars();
                } else if (sumRating > 1.5 && sumRating <= 2.5) {
                    starRating = Rating.STAR2.getStars();
                } else if (sumRating > 2.5 && sumRating <= 3.5) {
                    starRating = Rating.STAR3.getStars();
                } else if (sumRating > 3.5 && sumRating <= 4.5) {
                    starRating = Rating.STAR4.getStars();
                } else if (sumRating > 4.5) {
                    starRating = Rating.STAR5.getStars();
                }
            }
            return starRating;
        } else {
            return null;
        }
    }

    // controls if a user has been inactive for more than 6 months and if so sets the status to inactive
    @Scheduled(cron = "0 0 0 * * ?") // daily execution at midnight
    @Transactional
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

    public List<ReviewDTO> getAllReviewOfAUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            List<Review> reviews = reviewRepository.findAll();
            List<ReviewDTO> reviewsDTO = new ArrayList<>();
            for(Review r : reviews) {
                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setId(r.getId());
                reviewDTO.setDescription(r.getDescription());
                reviewDTO.setRating(r.getRating());
                reviewsDTO.add(reviewDTO);
            }
            return reviewsDTO;
        } else {
            throw new EntityNotFoundException("User with id: " + id + " not found");
        }
    }

    // reactivate user
    public void reactivationOfUser(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent() && userOptional.get().getRecordStatus() != RecordStatus.ACTIVE) {
            User user = userOptional.get();
            user.setRecordStatus(RecordStatus.ACTIVE);
            user.setLastActivityDate(LocalDateTime.now());
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

    // list of events in which a user participates
    public List<Event> listOfEventsPartecipate (Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new ArrayList<>(user.getEvent());
        } else {
            throw new EntityNotFoundException("User with id: " + userId + " doesn't exist");
        }
    }

    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }
}