package co.develhope.team2.meeplemeet_project_team2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MeepleMeetApplication {

	public static void main(String[] args) {
		SpringApplication.run(MeepleMeetApplication.class, args);
	}
}
