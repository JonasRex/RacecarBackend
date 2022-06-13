package dtoFacades;

import dtos.SignupDTO;
import entities.User;
import errorhandling.API_Exception;
import facades.UserFacade;
import utils.EMF_Creator;

public class SignupDTOFacade {
    private static SignupDTOFacade instance;
    private static UserFacade userFacade;

    public static SignupDTOFacade getFacade() {
        if (instance == null) {
            userFacade = UserFacade.getFacade(EMF_Creator.createEntityManagerFactory());
            instance = new SignupDTOFacade();
        }
        return instance;
    }


    public SignupDTO create(SignupDTO signupDTO) throws API_Exception {


        if (userFacade.usernameExists(signupDTO.getUserName())) {
            throw new API_Exception("Username already exists");
        }
        if (signupDTO.getUserName().length() < 5) {
            throw new API_Exception("Username must be longer than 5 characters");
        }
        if (signupDTO.getPassword().length() < 5) {
            throw new API_Exception("Password must be longer than 5 characters");
        }
        if (signupDTO.getUserName().contains(" ")) {
            throw new API_Exception("Username cannot have spaces");
        }
        if (signupDTO.getPassword().contains(" ")) {
            throw new API_Exception("Password cannot have spaces");
        }


        User user = signupDTO.getEntity();
        User newUser = userFacade.create(user);

        return new SignupDTO(newUser);
    }
}