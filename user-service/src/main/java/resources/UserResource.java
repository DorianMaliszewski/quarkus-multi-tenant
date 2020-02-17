package resources;

import io.quarkus.oidc.IdToken;
import io.quarkus.security.identity.SecurityIdentity;
import models.User;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.resteasy.annotations.jaxrs.PathParam;
import services.UserService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/{tenant}/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserService userService;

    @Inject
    SecurityIdentity identity;

    @Inject
    JsonWebToken jsonWebToken;


    @GET
    public Response findAll() {
        return Response.ok(userService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam Long id) {
        User user = this.userService.findById(id);
        return Response.ok(user).build();
    }


    @GET
    @Path("/me")
    @Transactional
    public Response findByUsernameOrCreate(@PathParam Long id) {
        User user = this.userService.findByUsernameOrCreate(identity);
        return Response.ok(user).build();
    }


    @POST
    public Response create(User user) {
        user = this.userService.create(user);
        return Response.created(URI.create("/users/" + user.getId())).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam Long id, User user) {
        user = this.userService.update(id, user);
        return Response.ok(user).build();
    }


    @DELETE
    @Path("/{id}")
    public Response deleteById(@PathParam Long id) {
        this.userService.deleteById(id);
        return Response.noContent().build();
    }

}
