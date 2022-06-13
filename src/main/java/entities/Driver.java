package entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "drivers")
@NamedQuery(name = "Driver.deleteAllRows", query = "DELETE from Driver ")
public class Driver implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthYear")
    private String birthYear;

    @Column(name = "experience")
    private String experience;

    @Column(name = "gender")
    private String gender;

    @JoinColumn(name = "car_id", referencedColumnName = "id")//, nullable = false)
    private Car car;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Driver(String name, String birthYear, String experience, String gender) {
        this.name = name;
        this.birthYear = birthYear;
        this.experience = experience;
        this.gender = gender;
    }

    public Driver() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
