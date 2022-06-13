package dtoFacades;

import dtos.RenameMeDTO;
import entities.RenameMe;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.RenameMeFacade;
import utils.EMF_Creator;
import utils.Utility;
import java.io.IOException;
import java.util.List;

public class RenameMeDTOFacade implements IFacade<RenameMeDTO> {
    private static IFacade<RenameMeDTO> instance;
    private static IFacade<RenameMe> renameMeFacade;


    public RenameMeDTOFacade() {
    }

    public static IFacade<RenameMeDTO> getFacade() {
        if (instance == null) {
            renameMeFacade = RenameMeFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new RenameMeDTOFacade();
        }
        return instance;
    }

    //TODO Remove/Change this before use
    public static String getExampleData() throws IOException {

        String data1 = Utility.fetchData("https://api.punkapi.com/v2/beers/1");
        String data2 = Utility.fetchData("https://icanhazdadjoke.com");

        return data1 + "\n" + data2;
    }

    @Override
    public RenameMeDTO create(RenameMeDTO renameMeDTO) {
        RenameMe r = renameMeDTO.getEntity();
        r = renameMeFacade.create(r);
        return new RenameMeDTO(r);
    }

    @Override
    public RenameMeDTO getById(int id) throws EntityNotFoundException {
        return new RenameMeDTO(renameMeFacade.getById(id));
    }

    @Override
    public List<RenameMeDTO> getAll() {
        return RenameMeDTO.toList(renameMeFacade.getAll());
    }

    @Override
    public RenameMeDTO update(RenameMeDTO renameMeDTO) throws EntityNotFoundException {
        RenameMe renameMe = new RenameMe(renameMeDTO.getDummyStr1(), renameMeDTO.getDummyStr2());
        renameMe.setId(renameMeDTO.getId());
        RenameMe r = renameMeFacade.update(renameMe);
        return new RenameMeDTO(r);
    }

    @Override
    public RenameMeDTO delete(int id) throws EntityNotFoundException {
        return new RenameMeDTO(renameMeFacade.delete(id));
    }

    @Override
    public RenameMeDTO addRelation(int id1, int id2) throws EntityNotFoundException {
        return null;
    }

    @Override
    public RenameMeDTO removeRelation(int id1, int id2) throws EntityNotFoundException {
        return null;
    }

    @Override
    public long getCount() {
        return renameMeFacade.getCount();
    }
}