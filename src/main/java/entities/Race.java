package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "races")
@NamedQuery(name = "Race.deleteAllRows", query = "DELETE from Race ")
public class Race implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "location")
    private String location;

    // TODO change to Date class.
    @Column(name = "date")
    private String date;

    @Column(name = "duration")
    private int duration;

    @JoinTable(
            name = "race_car",
            joinColumns = @JoinColumn(name = "race_id"),
            inverseJoinColumns = @JoinColumn(name = "car_id"))
    @ManyToMany
    private List<Car> carList = new ArrayList<>();

    public Race(String name, String location, String date, int duration) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.duration = duration;
    }

    public Race() {
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setCarList(List<Car> carList) {
        this.carList = carList;
    }

    public void addCar(Car car) {
        this.carList.add(car);
        if(!car.getRaceList().contains(this)){
            car.addRace(this);
        }
    }


    public void removeCar(Car car) {
        this.carList.remove(car);
        //if(!car.getRaceList().contains(this))
        car.getRaceList().remove(this);
    }
}
