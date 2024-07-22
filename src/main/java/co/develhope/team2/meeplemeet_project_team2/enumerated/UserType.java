package co.develhope.team2.meeplemeet_project_team2.enumerated;

public enum UserType {
    ADMIN ("Admin"),
    OWNER ("Location Owner"),
    USER ("User");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
