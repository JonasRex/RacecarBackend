package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtoFacades.CarDTOFacade;
import dtos.CarDTO;
import errorhandling.EntityNotFoundException;
import facades.IFacade;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("car")
public class CarResource {
    private static final IFacade<CarDTO> FACADE =  CarDTOFacade.getFacade();
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
        CarDTO dto = FACADE.getById(id);
        return Response.ok().entity(GSON.toJson(dto)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response create(String content) {
        CarDTO dto = GSON.fromJson(content, CarDTO.class);
        CarDTO newDto = FACADE.create(dto);
        return Response.ok().entity(GSON.toJson(newDto)).build();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response update(@PathParam("id") int id, String content) throws EntityNotFoundException {
        CarDTO dto = GSON.fromJson(content, CarDTO.class);
        dto.setId(id);
        CarDTO updated = FACADE.update(dto);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response delete(@PathParam("id") int id) throws EntityNotFoundException {
        CarDTO deleted = FACADE.delete(id);
        return Response.ok().entity(GSON.toJson(deleted)).build();
    }

    @PUT
    @Path("/add/{carid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response addRelation(@PathParam("carid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        CarDTO updated = FACADE.addRelation(id1, id2);
        return Response.ok().entity(GSON.toJson(updated)).build();
    }

    @DELETE
    @Path("/remove/{carid}/{entityid}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response removeRelation(@PathParam("carid") int id1, @PathParam("entityid") int id2) throws EntityNotFoundException {
        CarDTO updated = FACADE.removeRelation(id1, id2);
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
