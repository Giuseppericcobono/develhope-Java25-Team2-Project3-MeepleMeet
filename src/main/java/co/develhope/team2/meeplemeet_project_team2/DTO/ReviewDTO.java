package co.develhope.team2.meeplemeet_project_team2.DTO;

import co.develhope.team2.meeplemeet_project_team2.entities.enumerated.Rating;

public class ReviewDTO {

    private Integer id;
    private String description;
    private Rating rating;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }
}
