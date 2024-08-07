package co.develhope.team2.meeplemeet_project_team2.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "PublicPlace")
public class PublicPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "public_place_id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private Integer maxCapacity;

    @Column(nullable = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime opening;

    @Column(nullable = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closing;

    public PublicPlace(){}

    public PublicPlace(Integer id, String name, Integer maxCapacity, LocalTime opening, LocalTime closing) {
        this.id = id;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.opening = opening;
        this.closing = closing;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public LocalTime getOpening() {
        return opening;
    }

    public void setOpening(LocalTime opening) {
        this.opening = opening;
    }

    public LocalTime getClosing() {
        return closing;
    }

    public void setClosing(LocalTime closing) {
        this.closing = closing;
    }
}
