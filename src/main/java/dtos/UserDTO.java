package dtos;

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



    public UserDTO(User user) {
        if (user.getId() != 0)
            this.id = user.getId();
        this.userName = user.getUserName();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();

    }


    public User getEntity() {
        User u = new User(this.userName, null, this.firstName, this.lastName, this.email);

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


}
