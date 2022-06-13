package dtos;

import entities.Car;
import entities.Driver;
import entities.Race;

import java.util.List;
import java.util.stream.Collectors;

public class CarDTO {
    private int id;
    private String name;
    private String brand;
    private String make;
    private String year;
    private String sponsor;
    private String color;
    private List<DriverDTO> driverDTOS;
//    private List<Integer> raceIDs;

    public CarDTO(Car car) {
        if (car.getId() != 0)
            this.id = car.getId();
        this.name = car.getName();
        this.brand = car.getBrand();
        this.make = car.getMake();
        this.year = car.getYear();
        this.sponsor = car.getSponsor();
        this.color = car.getColor();
        for (Driver driver : car.getDriverList()) {
            this.driverDTOS.add(new DriverDTO(driver));
        }
        /*
        for (Race race : car.getRaceList()) {
            this.raceIDs.add(race.getId());
        }
        */

    }


    public Car getEntity() {
        Car car = new Car(this.name, this.brand, this.make, this.year, this.sponsor, this.color);
        if (this.driverDTOS.size() != 0)
            this.driverDTOS.forEach(driverDTO -> car.addDriver(driverDTO.getEntity()));
        // TODO: May cause troubles..
//        if (this.raceIDs.size() != 0)
//            this.driverDTOS.forEach(driverDTO -> car.addDriver(driverDTO.getEntity()));
        return car;
    }

    public static List<CarDTO> toList(List<Car> cars) {
        return cars.stream().map(CarDTO::new).collect(Collectors.toList());
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

    public List<DriverDTO> getDriverDTOS() {
        return driverDTOS;
    }

    public void setDriverDTOS(List<DriverDTO> driverDTOS) {
        this.driverDTOS = driverDTOS;
    }


}
