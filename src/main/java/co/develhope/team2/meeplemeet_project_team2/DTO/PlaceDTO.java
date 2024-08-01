package co.develhope.team2.meeplemeet_project_team2.DTO;

import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceStatus;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;

import java.time.LocalTime;

public class PlaceDTO {

    private Integer id;
    private String name;
    private String address;
    private PlaceType placeType;
    private Integer maxCapacity;
    private LocalTime opening;
    private LocalTime closing;
    private String description;
    private RecordStatus recordStatusPlace;

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

    public RecordStatus getRecordStatusPlace() {
        return recordStatusPlace;
    }

    public void setRecordStatusPlace(RecordStatus recordStatusPlace) {
        this.recordStatusPlace = recordStatusPlace;
    }
}
