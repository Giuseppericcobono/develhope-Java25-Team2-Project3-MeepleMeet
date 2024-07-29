package co.develhope.team2.meeplemeet_project_team2.services;

import co.develhope.team2.meeplemeet_project_team2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
}
