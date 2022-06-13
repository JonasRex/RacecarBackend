package dtos;

import entities.Car;
import entities.Race;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RaceDTO {
    private int id;
    private String name;
    private String location;
    private String date;
    private int duration;
    private List<CarDTO> carDTOs = new ArrayList<>();

    public RaceDTO(Race race) {
        if (race.getId() != 0)
            this.id = race.getId();
        this.name = race.getName();
        this.location = race.getLocation();
        this.date = race.getDate();
        this.duration = race.getDuration();
        for(Car car : race.getCarList()) {
            this.carDTOs.add(new CarDTO(car));
        }
    }

    public Race getEntity() {
        Race race = new Race(this.name, this.location, this.date, this.duration);
        this.carDTOs.forEach(carDTO -> race.addCar(carDTO.getEntity()));
        return race;
    }

    public static List<RaceDTO> toList(List<Race> races) {
        return races.stream().map(RaceDTO::new).collect(Collectors.toList());
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

    public List<CarDTO> getCarDTOs() {
        return carDTOs;
    }

    public void setCarDTOs(List<CarDTO> carDTOs) {
        this.carDTOs = carDTOs;
    }
}
