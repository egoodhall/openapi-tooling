package com.github.emm035.openapi.annotation.processor.testing;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

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
