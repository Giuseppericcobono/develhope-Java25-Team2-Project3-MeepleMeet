package co.develhope.team2.meeplemeet_project_team2.DTO;

import java.time.LocalTime;

public class PlaceDTO {

    private String address;
    private String info;
    private String name;
    private Integer maxCapacity;
    private LocalTime opening;
    private LocalTime closing;

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
