package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum Rating {

    STAR1(1.0, "★☆☆☆☆"),
    STAR2(2.0, "★★☆☆☆"),
    STAR3(3.0, "★★★☆☆"),
    STAR4(4.0, "★★★★☆"),
    STAR5(5.0, "★★★★★");

    private final Double value;
    private final String stars;

    Rating(Double value, String stars) {
        this.value = value;
        this.stars = stars;
    }

    public Double getValue() {
        return value;
    }

    public String getStars() {
        return stars;
    }
}
