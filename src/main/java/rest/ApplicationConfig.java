package rest;

import errorhandling.EntityNotFoundExceptionMapper;

import java.util.Set;
import javax.ws.rs.core.Application;

@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */

    // TODO: Remember to add newly created ExceptionMappers and Resources here.

    private void addRestResourceClasses(Set<Class<?>> resources) {

        resources.add(cors.CorsFilter.class);
        resources.add(errorhandling.API_ExceptionMapper.class);
        resources.add(errorhandling.GenericExceptionMapper.class);
        resources.add(EntityNotFoundExceptionMapper.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
        resources.add(rest.DemoResource.class);
        resources.add(rest.RenameMeResource.class);
        resources.add(rest.UserResource.class);
        resources.add(rest.DataResource.class);
        resources.add(security.JWTAuthenticationFilter.class);
        resources.add(security.LoginEndpoint.class);
        resources.add(security.SignupEndpoint.class);
        resources.add(security.RolesAllowedFilter.class);
        resources.add(security.errorhandling.AuthenticationExceptionMapper.class);
        resources.add(security.errorhandling.NotAuthorizedExceptionMapper.class);
    }
}