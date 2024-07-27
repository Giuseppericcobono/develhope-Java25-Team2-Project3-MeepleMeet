package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum PlaceType {
    PUBLIC("Public place"),
    PRIVATE("Private place");

    private final String description;

    PlaceType(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}