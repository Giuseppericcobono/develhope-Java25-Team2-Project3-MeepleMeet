package co.develhope.team2.meeplemeet_project_team2.DTO;

import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;

import java.time.LocalTime;

public class PlaceDTO {

    //Place field.

    private PlaceType placeType;
    private String address;
    private String info;
    private RecordStatus recordStatus;

    //PublicPlace field.

    private String name;
    private Integer maxCapacity;
    private LocalTime opening;
    private LocalTime closing;

    public PlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public RecordStatus getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(RecordStatus recordStatus) {
        this.recordStatus = recordStatus;
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
