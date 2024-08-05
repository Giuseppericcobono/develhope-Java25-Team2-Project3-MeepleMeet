package co.develhope.team2.meeplemeet_project_team2.DTO;

import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.PlaceType;
import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.RecordStatus;

import java.time.LocalTime;

public class PlaceRequest {

    //Place field.

    private PlaceType placeType;
    private String address;
    private String info;
    private RecordStatus recordStatus;

    //PublicPlace field.

    private String name;
    private Integer maxCapacity;
    private LocalTime opening;
    private LocalTime closing;
}
