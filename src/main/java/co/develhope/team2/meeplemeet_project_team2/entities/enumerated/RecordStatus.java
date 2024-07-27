package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum RecordStatus {

    A ("This record is active"),
    I("This record is inactive"),
    D ("This record is deleted");

    /**
     * "A" when the user is active,
     * "I" when the user is idle,
     * "D" when the user was deleted via logical deletion
     */
    private String descrizione;

    RecordStatus(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getDescrizione() {
        return descrizione;
    }

}