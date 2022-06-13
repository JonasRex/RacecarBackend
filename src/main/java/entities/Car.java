package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
@NamedQuery(name = "Car.deleteAllRows", query = "DELETE from Car ")
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "brand")
    private String brand;

    @Column(name = "make")
    private String make;

    @Column(name = "year")
    private String year;

    @Column(name = "sponsor")
    private String sponsor;

    @Column(name = "color")
    private String color;

    @ManyToMany(mappedBy="carList")
    private List<Race> raceList = new ArrayList<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.PERSIST) //, fetch = FetchType.EAGER)
    private List<Driver> driverList = new ArrayList<>();

    public Car(String name, String brand, String make, String year, String sponsor, String color) {
        this.name = name;
        this.brand = brand;
        this.make = make;
        this.year = year;
        this.sponsor = sponsor;
        this.color = color;
    }

    public Car() {
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<Race> getRaceList() {
        return raceList;
    }

    public void setRaceList(List<Race> raceList) {
        this.raceList = raceList;
    }

    public void addRace(Race race) {
        this.raceList.add(race);
    }

    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }

    public void addDriver(Driver driver) {
        this.driverList.add(driver);
        driver.setCar(this);
    }

    public void removeDriver(Driver driver) {
        this.driverList.remove(driver);
        driver.setCar(null);
    }
}
