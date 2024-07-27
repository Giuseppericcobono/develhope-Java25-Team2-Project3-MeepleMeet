package co.develhope.team2.meeplemeet_project_team2.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String nameGame;

    @Column(nullable = false)
    private String descriptionGame;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateTimeEvent;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(nullable = false)
    private String location;

    public Event(){}

    public Event(Integer id, String name, String nameGame, String descriptionGame, LocalDateTime dateTimeEvent, Integer maxCapacity, String location) {
        this.id = id;
        this.name = name;
        this.nameGame = nameGame;
        this.descriptionGame = descriptionGame;
        this.dateTimeEvent = dateTimeEvent;
        this.maxCapacity = maxCapacity;
        this.location = location;
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

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public String getDescriptionGame() {
        return descriptionGame;
    }

    public void setDescriptionGame(String descriptionGame) {
        this.descriptionGame = descriptionGame;
    }

    public LocalDateTime getDateTimeEvent() {
        return dateTimeEvent;
    }

    public void setDateTimeEvent(LocalDateTime dateTimeEvent) {
        this.dateTimeEvent = dateTimeEvent;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
