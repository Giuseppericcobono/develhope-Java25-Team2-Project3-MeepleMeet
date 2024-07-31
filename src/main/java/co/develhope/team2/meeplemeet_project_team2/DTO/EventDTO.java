package co.develhope.team2.meeplemeet_project_team2.DTO;

public class EventDTO {

    private Integer id;
    private String descrizione;
    private Integer idUtente;

    public EventDTO() {}

    public EventDTO(Integer id, String descrizione, Integer idUtente) {
        this.id = id;
        this.descrizione = descrizione;
        this.idUtente = idUtente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

}
