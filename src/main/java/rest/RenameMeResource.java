package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtoFacades.RenameMeDTOFacade;
import dtos.RenameMeDTO;
import errorhandling.EntityNotFoundException;
import facades.IFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

//Todo Remove or change relevant parts before ACTUAL use
@Path("xxx")
public class RenameMeResource {

    private static final IFacade<RenameMeDTO> FACADE =  RenameMeDTOFacade.getFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Path("demo")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }


    @Path("data")
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String getExampleData() throws IOException {
        return RenameMeDTOFacade.getExampleData();
    }

    //TODO: Change these

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAll() {
        return Response.ok().entity(GSON.toJson(FACADE.getAll())).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
        RenameMeDTO dto = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(dto)).build();
    }
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        RenameMeDTO dto = GSON.fromJson(content, RenameMeDTO.class);
        RenameMeDTO newDto = FACADE.create(dto);
        return Response.ok().entity(GSON.toJson(newDto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        RenameMeDTO dto = GSON.fromJson(content, RenameMeDTO.class);
        dto.setId(id);
        RenameMeDTO updated = FACADE.update(dto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        RenameMeDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @GET
    @Path("count")
    @Produces({MediaType.APPLICATION_JSON})
    public String getCount()  {
        long count = FACADE.getCount();
        return "{\"count\":"+count+"}";  //Done manually so no need for a DTO
    }
}
