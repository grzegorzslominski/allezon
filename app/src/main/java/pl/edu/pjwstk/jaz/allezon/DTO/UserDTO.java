package pl.edu.pjwstk.jaz.allezon.DTO;

public class UserDTO {
    private String email;
    private String password;

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
