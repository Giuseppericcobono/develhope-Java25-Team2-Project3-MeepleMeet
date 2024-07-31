package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum RecordStatus {

    ACTIVE ("This record is active"),
    INACTIVE("This record is active"),
    DELETED ("This record is deleted");

    private String descrizione;

    RecordStatus(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

}