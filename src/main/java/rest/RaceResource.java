package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtoFacades.RaceDTOFacade;
import dtos.CarDTO;
import dtos.RaceDTO;
import errorhandling.EntityNotFoundException;
import facades.IFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("race")
public class RaceResource {
    private static final RaceDTOFacade FACADE = (RaceDTOFacade) RaceDTOFacade.getFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        RaceDTO dto = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(dto)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        RaceDTO dto = GSON.fromJson(content, RaceDTO.class);
        RaceDTO newDto = FACADE.create(dto);
        return Response.ok().entity(GSON.toJson(newDto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        RaceDTO dto = GSON.fromJson(content, RaceDTO.class);
        dto.setId(id);
        RaceDTO updated = FACADE.update(dto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        RaceDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @PUT
    @Path("/add/{raceid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addRelation(@PathParam("raceid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        RaceDTO updated = FACADE.addRelation(id1, id2);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("/remove/{raceid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeRelation(@PathParam("raceid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        RaceDTO updated = FACADE.removeRelation(id1, id2);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getCount()  {
        long count = FACADE.getCount();
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }


    @GET
    @Path("/driver/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getRacesByDriverUsername(@PathParam("username") String username) throws EntityNotFoundException {
        return Response.ok().entity(GSON.toJson(FACADE.getRacesByDriverUsername(username))).build();
    }

}
