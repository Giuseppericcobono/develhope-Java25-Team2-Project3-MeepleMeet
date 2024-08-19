package co.develhope.team2.meeplemeet_project_team2.entities;

import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "Place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id", insertable = false)
    private Integer id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private PlaceType placeType;

    @Column(nullable = true)
    private String info;

    @Column(nullable = true)
    private String name;

    @Column(nullable = true)
    private Integer maxCapacity;

    @Column(nullable = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime opening;

    @Column(nullable = true)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime closing;

    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatusPlace;

    public Place() {}

    public Place(Integer id, String address, PlaceType placeType, String info, String name, Integer maxCapacity, LocalTime opening, LocalTime closing, RecordStatus recordStatusPlace) {
        this.id = id;
        this.address = address;
        this.placeType = placeType;
        this.info = info;
        this.name = name;
        this.maxCapacity = maxCapacity;
        this.opening = opening;
        this.closing = closing;
        this.recordStatusPlace = recordStatusPlace;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public RecordStatus getRecordStatusPlace() {
        return recordStatusPlace;
    }

    public void setRecordStatusPlace(RecordStatus recordStatusPlace) {
        this.recordStatusPlace = recordStatusPlace;
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
