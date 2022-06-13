package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import facades.DataFacade;
import utils.EMF_Creator;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("data")
public class DataResource {
    private static final DataFacade FACADE = DataFacade.getFacade(EMF_Creator.createEntityManagerFactory());
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    @GET
    @Path("quote")
    @Produces({MediaType.APPLICATION_JSON})
    public String getQuote() throws IOException {
        return FACADE.getRandomQuote();
    }

    @GET
    @Path("joke")
    @Produces({MediaType.APPLICATION_JSON})
    public String getJoke() throws IOException {
        return FACADE.getRandomJoke();
    }
}
