package gateway.service.dto;

public class PassengerDTO {

    private String username;
    private String name;
    private String surname;
    private String email;

    public PassengerDTO(UserDTO userDTO) {
        this.username = userDTO.getLogin();
        this.name = userDTO.getFirstName();
        this.surname = userDTO.getLastName();
        this.email = userDTO.getEmail();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
