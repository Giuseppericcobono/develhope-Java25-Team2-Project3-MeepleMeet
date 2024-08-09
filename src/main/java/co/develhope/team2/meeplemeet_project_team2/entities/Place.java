package co.develhope.team2.meeplemeet_project_team2.entities;

import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "Place")
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private PlaceType placeType;

    @Column(nullable = true)
    private String info;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "public_place_id")
    private PublicPlace publicPlace;

    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatusPlace;

    public Place() {}

    public Place(Integer id, String address, PlaceType placeType, String info, PublicPlace publicPlace, RecordStatus recordStatusPlace) {
        this.id = id;
        this.address = address;
        this.placeType = placeType;
        this.info = info;
        this.publicPlace = publicPlace;
        this.recordStatusPlace = recordStatusPlace;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PlaceType getPlaceType() {
        return placeType;
    }

    public void setPlaceType(PlaceType placeType) {
        this.placeType = placeType;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public PublicPlace getPublicPlace() {
        return publicPlace;
    }

    public void setPublicPlace(PublicPlace publicPlace) {
        this.publicPlace = publicPlace;
    }

    public RecordStatus getRecordStatusPlace() {
        return recordStatusPlace;
    }

    public void setRecordStatusPlace(RecordStatus recordStatusPlace) {
        this.recordStatusPlace = recordStatusPlace;
    }
}
