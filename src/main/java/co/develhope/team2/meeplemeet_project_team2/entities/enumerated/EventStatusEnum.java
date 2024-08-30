package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum EventStatusEnum {
    //created event status
    NOT_STARTED ("Event not yet started"),
    IN_PROGRESS ("Event in progress"),
    FINISHED ("Event finished");

    private final String description;

    EventStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
