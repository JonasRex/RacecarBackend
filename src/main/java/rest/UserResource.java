package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtoFacades.UserDTOFacade;
import dtos.UserDTO;
import errorhandling.EntityNotFoundException;
import facades.IFacade;
import facades.UserFacade;
import utils.EMF_Creator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("user")
public class UserResource {
    private static final IFacade<UserDTO> FACADE =  UserDTOFacade.getFacade();
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
        UserDTO dto = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(dto)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        UserDTO dto = GSON.fromJson(content, UserDTO.class);
        UserDTO newDto = FACADE.create(dto);
        return Response.ok().entity(GSON.toJson(newDto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        UserDTO dto = GSON.fromJson(content, UserDTO.class);
        dto.setId(id);
        UserDTO updated = FACADE.update(dto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        UserDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @PUT
    @Path("/add/{userid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addRelation(@PathParam("userid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        UserDTO updated = FACADE.addRelation(id1, id2);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("/remove/{userid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeRelation(@PathParam("userid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        UserDTO updated = FACADE.removeRelation(id1, id2);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }


    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getCount()  {
        long count = FACADE.getCount();
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
}