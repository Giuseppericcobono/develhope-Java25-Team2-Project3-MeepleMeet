package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum Rating {

    STELLA1("☆"),
    STELLA2("☆☆"),
    STELLA3("☆☆☆"),
    STELLA4("☆☆☆☆"),
    STELLA5("☆☆☆☆☆");

    private final String stars;

    Rating(String stars) {
        this.stars = stars;
    }

    public String getStars() {
        return stars;
    }
}
