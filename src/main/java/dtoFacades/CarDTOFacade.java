package dtoFacades;

import dtos.CarDTO;
import entities.Car;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.CarFacade;
import utils.EMF_Creator;

import java.util.List;

public class CarDTOFacade implements IFacade<CarDTO> {
    private static IFacade<CarDTO> instance;
    private static IFacade<Car> carFacade;

    public CarDTOFacade() {
    }

    public static IFacade<CarDTO> getFacade() {
        if (instance == null) {
            carFacade = CarFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new CarDTOFacade();
        }
        return instance;
    }

    @Override
    public CarDTO create(CarDTO carDTO) {
        Car car = carDTO.getEntity();
        car = carFacade.create(car);
        return new CarDTO(car);
    }

    @Override
    public CarDTO getById(int id) throws EntityNotFoundException {
        return new CarDTO(carFacade.getById(id));
    }

    @Override
    public List<CarDTO> getAll() {
        return CarDTO.toList(carFacade.getAll());
    }

    @Override
    public CarDTO update(CarDTO carDTO) throws EntityNotFoundException {
        Car car = carDTO.getEntity();
        car.setId(carDTO.getId());
        Car updated = carFacade.update(car);
        return new CarDTO(updated);
    }

    @Override
    public CarDTO delete(int id) throws EntityNotFoundException {
        return new CarDTO(carFacade.delete(id));
    }

    @Override
    public CarDTO addRelation(int id1, int id2) throws EntityNotFoundException {
        return new CarDTO(carFacade.addRelation(id1, id2));
    }

    @Override
    public CarDTO removeRelation(int id1, int id2) throws EntityNotFoundException {
        return new CarDTO(carFacade.removeRelation(id1, id2));
    }

    @Override
    public long getCount() {
        return carFacade.getCount();
    }
}