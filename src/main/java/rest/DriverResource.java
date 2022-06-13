package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtoFacades.DriverDTOFacade;
import dtos.DriverDTO;
import errorhandling.EntityNotFoundException;
import facades.IFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("driver")
public class DriverResource {
    private static final IFacade<DriverDTO> FACADE =  DriverDTOFacade.getFacade();
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
        DriverDTO dto = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(dto)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        DriverDTO dto = GSON.fromJson(content, DriverDTO.class);
        DriverDTO newDto = FACADE.create(dto);
        return Response.ok().entity(GSON.toJson(newDto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        DriverDTO dto = GSON.fromJson(content, DriverDTO.class);
        dto.setId(id);
        DriverDTO updated = FACADE.update(dto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        DriverDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @PUT
    @Path("/add/{driverid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addRelation(@PathParam("driverid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        DriverDTO updated = FACADE.addRelation(id1, id2);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("/remove/{driverid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeRelation(@PathParam("driverid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        DriverDTO updated = FACADE.removeRelation(id1, id2);
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

