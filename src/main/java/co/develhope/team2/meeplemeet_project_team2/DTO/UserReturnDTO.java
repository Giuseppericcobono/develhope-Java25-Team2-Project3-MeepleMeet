package co.develhope.team2.meeplemeet_project_team2.DTO;

public class UserReturnDTO {

    private String username;
    private String firstName;
    private String lastName;
    private Integer age;
    private String biography;
    private String starRating;

    public UserReturnDTO(){}

    public UserReturnDTO(String username, String firstName, String lastName, Integer age, String biography, String starRating) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.biography = biography;
        this.starRating = starRating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getStarRating() {
        return starRating;
    }

    public void setStarRating(String starRating) {
        this.starRating = starRating;
    }
}
