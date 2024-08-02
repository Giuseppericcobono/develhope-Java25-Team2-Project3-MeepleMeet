package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum PlaceStatus {
    OPEN("OPEN"),
    CLOSED("CLOSED");

    private final String status;

    PlaceStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
