package co.develhope.team2.meeplemeet_project_team2.configuration;

import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.UserType;
import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername("admin1")) {
            User user1 = new User();
            user1.setUsername("giuseppe.riccobono");
            user1.setFirstName("Giuseppe");
            user1.setLastName("Riccobono");
            user1.setBirth(LocalDate.of(1999, 1, 1));
            user1.setAge(38);
            user1.setEmail("admin1@example.com");
            user1.setPassword(passwordEncoder.encode("admin"));
            user1.setBiography("Administrator of the system");
            user1.setUserType(UserType.ADMIN);
            user1.setRecordStatus(RecordStatus.ACTIVE);
            userRepository.save(user1);
        }

        if (!userRepository.existsByUsername("admin2")) {
            User user2 = new User();
            user2.setUsername("erika.longo");
            user2.setFirstName("Erika");
            user2.setLastName("Longo");
            user2.setBirth(LocalDate.of(1999, 1, 1));
            user2.setAge(38);
            user2.setEmail("admin2@example.com");
            user2.setPassword(passwordEncoder.encode("admin"));
            user2.setBiography("Administrator of the system");
            user2.setUserType(UserType.ADMIN);
            user2.setRecordStatus(RecordStatus.ACTIVE);
            userRepository.save(user2);
        }

        if (!userRepository.existsByUsername("admin3")) {
            User user3 = new User();
            user3.setUsername("giuseppe.favara");
            user3.setFirstName("Giuseppe");
            user3.setLastName("Favara");
            user3.setBirth(LocalDate.of(1999, 1, 1));
            user3.setAge(38);
            user3.setEmail("admin3@example.com");
            user3.setPassword(passwordEncoder.encode("admin"));
            user3.setBiography("Administrator of the system");
            user3.setUserType(UserType.ADMIN);
            user3.setRecordStatus(RecordStatus.ACTIVE);
            userRepository.save(user3);
        }
    }
}
