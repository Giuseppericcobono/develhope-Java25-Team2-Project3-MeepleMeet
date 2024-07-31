package co.develhope.team2.meeplemeet_project_team2.DTO;

public class EventDTO {

    private Long id;
    private String descrizione;
    private Long idUtente;

    public EventDTO() {}

    public EventDTO(Long id, String descrizione, Long idUtente) {
        this.id = id;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Long getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Long idUtente) {
        this.idUtente = idUtente;
    }

}
