package com.github.emm035.openapi.annotation.processor.testing;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/base")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimpleResource {

  @POST
  @Path("/path/{pathParam}")
  public List<String> getList(
    @PathParam("pathParam") int pathParam,
    @QueryParam("queryParam") long queryParam
  ) {
    return null;
  }
}
