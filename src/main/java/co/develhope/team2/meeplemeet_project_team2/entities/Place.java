package co.develhope.team2.meeplemeet_project_team2.entities;

import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "Place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private PlaceType placeType;

    @Column(nullable = true)
    private Integer maxCapacity;

    @Column(nullable = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime opening;

    @Column(nullable = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closing;

    @Column(nullable = true)
    private String description;

    public Place() {}

    public Place(Integer id, String name, String address, PlaceType placeType, Integer maxCapacity, LocalTime opening, LocalTime closing, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.placeType = placeType;
        this.maxCapacity = maxCapacity;
        this.opening = opening;
        this.closing = closing;
        this.description = description;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
