package co.develhope.team2.meeplemeet_project_team2.DTO;

import co.develhope.team2.meeplemeet_project_team2.entities.Place;
import co.develhope.team2.meeplemeet_project_team2.entities.User;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.EventStatusEnum;

import java.time.LocalDateTime;
import java.util.List;

public class EventDTO {

    private String Creator;
    private String name;
    private String nameGame;
    private String descriptionGame;
    private LocalDateTime dateTimeEvent;
    private Integer maxCapacityEvent;
    private String address;
    private String placeName;
    private EventStatusEnum eventStatusEnum;
    private List<String> partecipants;

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        Creator = creator;
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

    public Integer getMaxCapacityEvent() {
        return maxCapacityEvent;
    }

    public void setMaxCapacityEvent(Integer maxCapacityEvent) {
        this.maxCapacityEvent = maxCapacityEvent;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public EventStatusEnum getEventStatusEnum() {
        return eventStatusEnum;
    }

    public void setEventStatusEnum(EventStatusEnum eventStatusEnum) {
        this.eventStatusEnum = eventStatusEnum;
    }

    public List<String> getPartecipants() {
        return partecipants;
    }

    public void setPartecipants(List<String> partecipants) {
        this.partecipants = partecipants;
    }
}
