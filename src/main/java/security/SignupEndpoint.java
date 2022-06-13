package security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dtoFacades.SignupDTOFacade;
import dtos.SignupDTO;
import entities.User;
import errorhandling.API_Exception;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

@Path("signup")
public class SignupEndpoint {
    private static final SignupDTOFacade FACADE = SignupDTOFacade.getFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(String content) throws API_Exception, JOSEException {
        // Test if the two passwords are the same.
        try {
            JsonObject json = JsonParser.parseString(content).getAsJsonObject();
            String password = json.get("password").getAsString();
            String password2 = json.get("password2").getAsString();

            if (!password.equals(password2)) {
                throw new API_Exception("Passwords not identical");
            }
        } catch (Exception e) {
            throw new API_Exception("Malformed JSON Suplied",400,e);
        }

        // Create and persist SignupDTO
        SignupDTO dto = GSON.fromJson(content, SignupDTO.class);
        SignupDTO newUser = FACADE.create(dto);

        // Create token and response.
        User user = newUser.getEntity();
        String token = LoginEndpoint.createToken(user.getUserName(), user.getRolesAsStrings());
        JsonObject responseJson = new JsonObject();
        responseJson.addProperty("username", user.getUserName());
        responseJson.addProperty("token", token);
        return Response.ok(new Gson().toJson(responseJson)).build();
    }


}
