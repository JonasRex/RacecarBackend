package dtoFacades;

import dtos.DriverDTO;
import entities.Driver;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.DriverFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class DriverDTOFacade implements IFacade<DriverDTO> {
    private static IFacade<DriverDTO> instance;
    private static DriverFacade driverFacade;

    public DriverDTOFacade() {
    }

    public static IFacade<DriverDTO> getFacade() {
        if (instance == null) {
            driverFacade = (DriverFacade) DriverFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new DriverDTOFacade();
        }
        return instance;
    }

    @Override
    public DriverDTO create(DriverDTO driverDTO) {
        Driver driver = driverDTO.getEntity();
        driver = driverFacade.create(driver);
        return new DriverDTO(driver);
    }

    @Override
    public DriverDTO getById(int id) throws EntityNotFoundException {
        return new DriverDTO(driverFacade.getById(id));
    }

    @Override
    public List<DriverDTO> getAll() {
        return DriverDTO.toList(driverFacade.getAll());
    }

    @Override
    public DriverDTO update(DriverDTO driverDTO) throws EntityNotFoundException {
        Driver driver = driverDTO.getEntity();
        driver.setId(driverDTO.getId());
        Driver updated = driverFacade.update(driver);
        return new DriverDTO(updated);
    }

    @Override
    public DriverDTO delete(int id) throws EntityNotFoundException {
        return new DriverDTO(driverFacade.delete(id));
    }

    @Override
    public DriverDTO addRelation(int id1, int id2) throws EntityNotFoundException {
        return new DriverDTO(driverFacade.addRelation(id1, id2));
    }

    @Override
    public DriverDTO removeRelation(int id1, int id2) throws EntityNotFoundException {
        return new DriverDTO(driverFacade.removeRelation(id1, id2));
    }

    @Override
    public long getCount() {
        return driverFacade.getCount();
    }

    public List<DriverDTO> getAllDriversByCarID(int id) {
        return DriverDTO.toList(driverFacade.getAllDriversByCarID(id));
    }
}
