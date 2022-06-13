package dtos;

import entities.RenameMe;
import entities.Role;
import entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SignupDTO {
    private int id;
    private String userName;
    private String password;
    private String password2;
    private String firstName;
    private String lastName;
    private String email;
    private List<Role> roleList = new ArrayList<>();
    private List<RenameMeDTO> renameMeDTOS = new ArrayList<>(); // List of IDs.


    public SignupDTO(User user) {
        if (user.getId() != 0)
            this.id = user.getId();
        this.userName = user.getUserName();
        this.password = user.getUserPass();
        this.password2 = user.getUserPass();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.roleList.addAll(user.getRoleList());
        for (RenameMe renameMe : user.getRenameMesList()) {
            this.renameMeDTOS.add(new RenameMeDTO(renameMe));
        }
    }


    public User getEntity() {
        User u = new User(this.userName, password, this.firstName, this.lastName, this.email);
        if (this.roleList != null)
        this.roleList.forEach(u::addRole);

        /*
        if (this.renameMeDTOS.size() != 0)
            this.renameMeDTOS.forEach(renameMeDTO -> u.addRenameMe(renameMeDTO.getEntity()));
*/

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<RenameMeDTO> getRenameMeDTOS() {
        return renameMeDTOS;
    }


}
