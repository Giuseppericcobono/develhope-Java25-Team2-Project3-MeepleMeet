package co.develhope.team2.meeplemeet_project_team2.controllers;

import co.develhope.team2.meeplemeet_project_team2.entities.Enrollment;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.services.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/create")
    public ResponseEntity<Enrollment> create(@RequestBody Enrollment enrollment){
        Enrollment newEnrollment = enrollmentService.createEnrollment(enrollment);
        return new ResponseEntity<>(newEnrollment, HttpStatus.CREATED);
    }

    @GetMapping("/users/{enrollment_id}")
    private List<User> usersList(@PathVariable Integer enrollmentId){
        return enrollmentService.getUsersByEnrollmentId(enrollmentId);
    }
}
