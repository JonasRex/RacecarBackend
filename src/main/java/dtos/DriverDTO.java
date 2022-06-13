package dtos;

import entities.Driver;
import entities.User;

import java.util.List;
import java.util.stream.Collectors;

public class DriverDTO {
    private int id;
    private String name;
    private String birthYear;
    private String experience;
    private String gender;
    private UserDTO userDTO;
    private int carId;

    public DriverDTO(Driver driver) {
        if (driver.getId() != 0)
            this.id = driver.getId();
        this.name = driver.getName();
        this.birthYear = driver.getBirthYear();
        this.experience = driver.getExperience();
        this.gender = driver.getGender();

        this.userDTO = new UserDTO(driver.getUser());

        if (driver.getCar() != null)
            this.carId = driver.getCar().getId();
    }

    public Driver getEntity() {
        Driver driver = new Driver(this.name, this.birthYear, this.experience, this.gender);
        if (this.userDTO != null)
            driver.setUser(this.userDTO.getEntity());
        return driver;
    }

    public static List<DriverDTO> toList(List<Driver> drivers) {
        return drivers.stream().map(DriverDTO::new).collect(Collectors.toList());
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

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
