package co.develhope.team2.meeplemeet_project_team2.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Public_place")
public class PublicPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "public_place_id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;
}
