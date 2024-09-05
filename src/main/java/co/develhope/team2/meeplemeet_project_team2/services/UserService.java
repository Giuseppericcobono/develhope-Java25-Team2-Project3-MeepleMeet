package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.DTO.UserDTO;
import co.develhope.team2.meeplemeet_project_team2.DTO.ReviewDTO;
import co.develhope.team2.meeplemeet_project_team2.DTO.UserReturnDTO;
import co.develhope.team2.meeplemeet_project_team2.entities.Event;
import co.develhope.team2.meeplemeet_project_team2.entities.Review;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.repositories.EventRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.ReviewRepository;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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

    //method to copy the information from user to userReturnDTO
    public UserReturnDTO fromUserToDTO(User user){
        UserReturnDTO userReturnDTO = new UserReturnDTO();
        userReturnDTO.setUsername(user.getUsername());
        userReturnDTO.setFirstName(user.getFirstName());
        userReturnDTO.setLastName(user.getLastName());
        userReturnDTO.setAge(user.getAge());
        userReturnDTO.setBiography(user.getBiography());
        userReturnDTO.setStarRating(user.getStarRating());
        return userReturnDTO;
    }

    public Optional<User> createUser(UserDTO userDTO){

        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        //calculates age based on birthdate
        user.setAge(calculateAge(user));
        user.setRecordStatus(RecordStatus.ACTIVE);
        user.setStarRating("No ratings found");
        user = userRepository.save(user);

        return Optional.of(user);
    }

    public Optional<List<UserReturnDTO>> getAllUsers(){
        List<User> userList = userRepository.findAll();
        if(userList.isEmpty()) {
            return Optional.empty();
        }
        List<UserReturnDTO> userReturnDTOList = new ArrayList<>();
        for (User user : userList) {
            UserReturnDTO userReturnDTO = new UserReturnDTO();
            fromUserToDTO(user);
            userReturnDTOList.add(userReturnDTO);
        } return Optional.of(userReturnDTOList);
    }

    public Optional<UserReturnDTO> getUserById(Integer id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            UserReturnDTO userReturnDTO = fromUserToDTO(user.get());
            return Optional.of(userReturnDTO);
        }
        return Optional.empty();
    }

    public Optional<UserReturnDTO> getUsersByUsername(String username){
        User user = userRepository.findUsersByUsername(username);
        UserReturnDTO userReturnDTO = fromUserToDTO(user);
        return Optional.of(userReturnDTO);
    }

    public Optional<List<UserReturnDTO>> getUsersByFirstName(String firstName){
        List<User> users = userRepository.findUsersByFirstName(firstName);
        List<UserReturnDTO> userReturnDTOList = new ArrayList<>();
        if(users.isEmpty()){
            logger.info("The user with first name: {} doesn't exist, or is deleted or inactive", firstName);
            return Optional.empty();
        } else {
            for (User user : users) {
                UserReturnDTO userReturnDTO = fromUserToDTO(user);
                userReturnDTOList.add(userReturnDTO);
            }
        } return Optional.of(userReturnDTOList);
    }

    public Optional<List<UserReturnDTO>> getUsersByLastName(String lastName){
        List<User> users = userRepository.findUsersByLastName(lastName);
        List<UserReturnDTO> userReturnDTOList = new ArrayList<>();
        if(users.isEmpty()){
            logger.info("The user with last name: {} doesn't exist, or is deleted or inactive", lastName);
            return Optional.empty();
        } else {
            for (User user : users) {
                UserReturnDTO userReturnDTO = fromUserToDTO(user);
                userReturnDTOList.add(userReturnDTO);
            }
        } return Optional.of(userReturnDTOList);
    }

    public Optional<List<UserReturnDTO>> getUsersByFullName(String firstName, String lastName){
        List<User> users = userRepository.findUsersByLastName(lastName);
        List<UserReturnDTO> userReturnDTOList = new ArrayList<>();
        if(users.isEmpty()){
            logger.info("The user: {} {} doesn't exist, or is deleted or inactive", firstName, lastName);
            return Optional.empty();
        } else {
            for (User user : users) {
                UserReturnDTO userReturnDTO = fromUserToDTO(user);
                userReturnDTOList.add(userReturnDTO);
            }
        } return Optional.of(userReturnDTOList);
    }

    // returns users based on their status
    public Optional<List<UserReturnDTO>> listOfUsersByStatus(String status) {
        List<User> users;
        List<UserReturnDTO> userReturnDTOList = new ArrayList<>();
        switch (status) {
            case "active" -> {
                users = userRepository.recordStatusEntity(RecordStatus.ACTIVE);
                for (User user : users) {
                    UserReturnDTO userReturnDTO = fromUserToDTO(user);
                    userReturnDTOList.add(userReturnDTO);
                }
                return Optional.of(userReturnDTOList);
            }
            case "inactive" -> {
                users = userRepository.recordStatusEntity(RecordStatus.INACTIVE);
                for (User user : users) {
                    UserReturnDTO userReturnDTO = fromUserToDTO(user);
                    userReturnDTOList.add(userReturnDTO);
                }
                return Optional.of(userReturnDTOList);
            }
            case "deleted" -> {
                users = userRepository.recordStatusEntity(RecordStatus.DELETED);
                for (User user : users) {
                    UserReturnDTO userReturnDTO = fromUserToDTO(user);
                    userReturnDTOList.add(userReturnDTO);
                }
                return Optional.of(userReturnDTOList);
            }
            default -> {
                return Optional.empty();
            }
        }
    }

    //updates the user with id
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
                    logger.info("The age provided does not correspond to birthdate");
                    return Optional.empty();
                }
                existingUser.setAge(updateUser.getAge());
            }

            existingUser.setUsername(updateUser.getUsername());
            existingUser.setFirstName(updateUser.getFirstName());
            existingUser.setLastName(updateUser.getLastName());
            existingUser.setEmail(updateUser.getEmail());
            existingUser.setPassword(updateUser.getPassword());
            existingUser.setBiography(updateUser.getBiography());
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
    public Integer calculateAge(User user) {
        LocalDate birthDate = user.getBirth();
        LocalDate today = LocalDate.now();

        return Period.between(birthDate, today).getYears();
    }

    //updates the age of all users based on birthdate
    @Scheduled(fixedDelay = 60000)// execution every minute
    @Transactional
    public void updateAllUserAges() {
        logger.info("updating users' age...");
        List<User> users = userRepository.findAll();
        for (User user : users) {
            int newAge = calculateAge(user);
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

    public Optional<List<ReviewDTO>> getAllReviewOfAUserById(Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isPresent()) {
            List<Review> reviews = reviewRepository.findAll();
            List<ReviewDTO> reviewsDTO = new ArrayList<>();
            for(Review r : reviews) {
                ReviewDTO reviewDTO = new ReviewDTO();
                reviewDTO.setDescription(r.getDescription());
                reviewDTO.setRating(r.getRating().getStars());
                reviewsDTO.add(reviewDTO);
            }
            return Optional.of(reviewsDTO);
        } else {
            logger.info("User with id: {} not found", id);
            return Optional.empty();
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
            logger.info("User with id: {} is already active", id);
        } else {
            logger.info("User with id: {} doesn't exist", id);
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
            logger.info("User with id: {} doesn't exist", id);
        }
    }

    // list of events in which a user participates
    public Optional<List<Event>> listOfEventsParticipate(Integer userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return Optional.of(new ArrayList<>(user.getEvent()));
        } else {
            logger.info("User with id: " + userId + " doesn't exist");
            return Optional.empty();
        }
    }

    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }

    public void deleteAll(){
        userRepository.deleteAll();
    }
}