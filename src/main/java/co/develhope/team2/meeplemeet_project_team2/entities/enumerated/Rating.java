package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum Rating {

    ZERO(0.0, "☆☆☆☆☆"),
    ONE(1.0, "★☆☆☆☆"),
    TWO(2.0, "★★☆☆☆"),
    THREE(3.0, "★★★☆☆"),
    FOUR(4.0, "★★★★☆"),
    FIVE(5.0, "★★★★★");

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
