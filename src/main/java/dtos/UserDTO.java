package dtos;

import entities.RenameMe;
import entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDTO {
    private int id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private List<RenameMeDTO> renameMeDTOS = new ArrayList<>(); // List of IDs.


    public UserDTO(User user) {
        if (user.getId() != 0)
            this.id = user.getId();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        for (RenameMe renameMe : user.getRenameMesList()) {
            this.renameMeDTOS.add(new RenameMeDTO(renameMe));
        }
    }


    public User getEntity() {
        User u = new User(this.userName, null, this.firstName, this.lastName, this.email);
        this.renameMeDTOS.forEach(renameMeDTO -> u.addRenameMe(renameMeDTO.getEntity()));
        return u;
    }

    public static List<UserDTO> toList(List<User> users) {
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RenameMeDTO> getRenameMeDTOS() {
        return renameMeDTOS;
    }

    @Override
    public String toString() {
        return "ProfileDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", renameMeDTOS=" + renameMeDTOS +
                '}';
    }
}
