package nl.salp.warcraft4j.webapp.rest;

import com.google.inject.Singleton;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * TODO Document class.
 */
@Provider
@Singleton
public class MyExceptionMapper  implements
        ExceptionMapper<WebApplicationException> {
    @Override
    public Response toResponse(WebApplicationException ex) {
        return Response.status(500).entity(ex.toString()).type("text/plain")
                .build();
    }
}