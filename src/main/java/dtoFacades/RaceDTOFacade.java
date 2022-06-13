package dtoFacades;

import dtos.RaceDTO;
import entities.Car;
import entities.Driver;
import entities.Race;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.RaceFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import java.util.List;

public class RaceDTOFacade implements IFacade<RaceDTO> {
    private static IFacade<RaceDTO> instance;
    private static RaceFacade raceFacade;

    public RaceDTOFacade() {
    }

    public static IFacade<RaceDTO> getFacade() {
        if (instance == null) {
            raceFacade = (RaceFacade) RaceFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new RaceDTOFacade();
        }
        return instance;
    }

    @Override
    public RaceDTO create(RaceDTO raceDTO) {
        Race race = raceDTO.getEntity();
        race = raceFacade.create(race);
        return new RaceDTO(race);
    }

    @Override
    public RaceDTO getById(int id) throws EntityNotFoundException {
        return new RaceDTO(raceFacade.getById(id));
    }

    @Override
    public List<RaceDTO> getAll() {
        return RaceDTO.toList(raceFacade.getAll());
    }

    @Override
    public RaceDTO update(RaceDTO raceDTO) throws EntityNotFoundException {
        Race race = raceDTO.getEntity();
        race.setId(raceDTO.getId());
        Race updated = raceFacade.update(race);
        return new RaceDTO(updated);
    }

    @Override
    public RaceDTO delete(int id) throws EntityNotFoundException {
        return new RaceDTO(raceFacade.delete(id));
    }

    @Override
    public RaceDTO addRelation(int id1, int id2) throws EntityNotFoundException {
        return new RaceDTO(raceFacade.addRelation(id1, id2));
    }

    @Override
    public RaceDTO removeRelation(int id1, int id2) throws EntityNotFoundException {
        return new RaceDTO(raceFacade.removeRelation(id1, id2));
    }

    @Override
    public long getCount() {
        return raceFacade.getCount();
    }

    public List<RaceDTO> getRacesByDriverUsername(String username) throws EntityNotFoundException {
        return RaceDTO.toList(raceFacade.getRacesByDriverUsername(username));
    }
}