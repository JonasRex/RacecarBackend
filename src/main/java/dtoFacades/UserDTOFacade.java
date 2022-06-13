package dtoFacades;

import dtos.UserDTO;
import entities.User;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.UserFacade;
import utils.EMF_Creator;

import java.util.List;

public class UserDTOFacade implements IFacade<UserDTO> {
    private static IFacade<UserDTO> instance;
    private static IFacade<User> userFacade;

    public UserDTOFacade() {
    }

    public static IFacade<UserDTO> getFacade() {
        if (instance == null) {
            userFacade = UserFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new UserDTOFacade();
        }
        return instance;
    }

    @Override
    public UserDTO create(UserDTO userDTO) {
        User u = userDTO.getEntity();
        u = userFacade.create(u);
        return new UserDTO(u);
    }

    @Override
    public UserDTO getById(int id) throws EntityNotFoundException {
        return new UserDTO(userFacade.getById(id));
    }

    @Override
    public List<UserDTO> getAll() {
        return UserDTO.toList(userFacade.getAll());
    }

    @Override
    public UserDTO update(UserDTO userDTO) throws EntityNotFoundException {
        User user = userDTO.getEntity();
        user.setId(userDTO.getId());
        User updated = userFacade.update(user);
        return new UserDTO(updated);
    }

    @Override
    public UserDTO delete(int id) throws EntityNotFoundException {
        return new UserDTO(userFacade.delete(id));
    }

    public UserDTO addRelation(int id1, int id2) throws EntityNotFoundException {
        return new UserDTO(userFacade.addRelation(id1, id2));
    }

    @Override
    public UserDTO removeRelation(int id1, int id2) throws EntityNotFoundException {
        return new UserDTO(userFacade.removeRelation(id1, id2));
    }

    @Override
    public long getCount() {
        return userFacade.getCount();
    }
}
