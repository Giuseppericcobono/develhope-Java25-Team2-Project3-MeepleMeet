package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.UserDTO;
import co.develhope.team2.meeplemeet_project_team2.DTO.ReviewDTO;
import co.develhope.team2.meeplemeet_project_team2.DTO.UserLoginDTO;
import co.develhope.team2.meeplemeet_project_team2.DTO.UserRegistrationDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.UserType;
import co.develhope.team2.meeplemeet_project_team2.repositories.EventRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.ReviewRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerNewUser(UserRegistrationDTO userRegistrationDTO) {
        // Check if the username or email already exists
        if (userRepository.existsByUsername(userRegistrationDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }
        if (userRepository.existsByEmail(userRegistrationDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already taken");
        }

        User user = new User();
        user.setUsername(userRegistrationDTO.getUsername());
        user.setFirstName(userRegistrationDTO.getFirstName());
        user.setLastName(userRegistrationDTO.getLastName());
        user.setBirth(userRegistrationDTO.getBirth());
        user.setAge(calculateAge(userRegistrationDTO.getBirth()));
        user.setEmail(userRegistrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDTO.getPassword()));
        user.setUserType(UserType.USER); // set default user type
        user.setRecordStatus(RecordStatus.ACTIVE);

        return userRepository.save(user);
    }

    public User login(UserLoginDTO userLoginDTO) {
        User user = userRepository.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Update last activity date
        user.setLastActivityDate(LocalDateTime.now());
        userRepository.save(user);

        return user;
    }
//    public User createUser(UserDTO userDTO){
//
//        User user = new User();
//        BeanUtils.copyProperties(userDTO, user);
//
//        //calculates age based on birthdate
//        user.setAge(calculateAge(user));
//        user.setRecordStatus(RecordStatus.ACTIVE);
//        user.setStarRating("No ratings found");
//        user = userRepository.save(user);
//
//        return user;
//    }

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
    public Optional<User> updateUser(Integer id, User updateUser) {
        // finds the existing user with the id.
        Optional<User> userOptional = userRepository.findById(id);

        if(userOptional.isPresent()){
            User existingUser = userOptional.get();

            // if the birthdate is provided, calculates and updates the age
            if (updateUser.getBirth() != null) {
                Integer calculatedAge = (int) ChronoUnit.YEARS.between(updateUser.getBirth(), LocalDate.now());
                existingUser.setBirth(updateUser.getBirth());
                existingUser.setAge(calculatedAge);
            }

            // if the age is provided without a birthdate, validates it against the existing birthdate
            if (updateUser.getAge() != null && updateUser.getBirth() == null) {
                Integer calculatedAge = (int) ChronoUnit.YEARS.between(existingUser.getBirth(), LocalDate.now());
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
            if (updateUser.getPassword() != null) {
                existingUser.setPassword(updateUser.getPassword());
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
            return Optional.of(existingUser);
        } else {
            // case where the user with the specified id is not found.
            return Optional.empty();
        }
    }

    //calculates age based on birthdate
    public Integer calculateAge(LocalDate birthDate) {

        LocalDate today = LocalDate.now();

        // Calculate the period between the birthdate and today
        Period period = Period.between(birthDate, today);
        // Return the number of years as age
        return period.getYears();
    }

    //updates the age of all users based on birthdate
    @Scheduled(fixedDelay = 60000)// execution every minute
    @Transactional
    public void updateAllUserAges() {
        logger.info("updating users' age...");
        List<User> users = userRepository.findAll();
        for (User user : users) {
            int newAge = calculateAge(user.getBirth());
            if (newAge != user.getAge()) {
                user.setAge(newAge);
                userRepository.save(user);
            }
        }
    }

    // controls if a user has been inactive for more than 6 months and if so sets the status to inactive
    @Scheduled(fixedDelay = 60000)// execution every minute
    @Transactional
    public void updateInactiveUsers() {
        logger.info("checking for inactive users...");
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
    public List<Event> listOfEventsParticipate(Integer userId) {
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