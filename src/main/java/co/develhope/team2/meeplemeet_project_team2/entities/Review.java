package co.develhope.team2.meeplemeet_project_team2.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "events_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name = "User_id")
    private User user;

    @Column(nullable = true)
    private String description;



}
