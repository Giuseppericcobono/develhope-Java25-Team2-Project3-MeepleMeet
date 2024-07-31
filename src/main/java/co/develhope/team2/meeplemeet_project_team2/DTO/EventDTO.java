package co.develhope.team2.meeplemeet_project_team2.DTO;

import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.EventStatusEnum;

import java.time.LocalDateTime;

public class EventDTO {

    private Integer id;
    private Integer idUtente;
    private String name;
    private String nameGame;
    private String descriptionGame;
    private LocalDateTime dateTimeEvent;
    private Integer maxCapacity;
    private String location;
    private EventStatusEnum eventStatusEnum;

    public EventDTO() {}

    public EventDTO(Integer id, Integer idUtente, String name, String nameGame, String descriptionGame, LocalDateTime dateTimeEvent, Integer maxCapacity, String location, EventStatusEnum eventStatusEnum) {
        this.id = id;
        this.idUtente = idUtente;
        this.name = name;
        this.nameGame = nameGame;
        this.descriptionGame = descriptionGame;
        this.dateTimeEvent = dateTimeEvent;
        this.maxCapacity = maxCapacity;
        this.location = location;
        this.eventStatusEnum = eventStatusEnum;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
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

    public EventStatusEnum getEventStatusEnum() {
        return eventStatusEnum;
    }

    public void setEventStatusEnum(EventStatusEnum eventStatusEnum) {
        this.eventStatusEnum = eventStatusEnum;
    }
}
