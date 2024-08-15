package co.develhope.team2.meeplemeet_project_team2.entities.enumerated;

public enum Rating {

    STAR1("★☆☆☆☆"),
    STAR2("★★☆☆☆"),
    STAR3("★★★☆☆"),
    STAR4("★★★★☆"),
    STAR5("★★★★★");

    private final String stars;

    Rating(String stars) {
        this.stars = stars;
    }

    public String getStars() {
        return stars;
    }
}
