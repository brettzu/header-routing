package io.harness.multiservice;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("rpc")
@Produces(MediaType.APPLICATION_JSON)
public class PayloadResource {

  private PayloadService payloadService = new PayloadService();

  @GET
  public Response getPayloads(@HeaderParam("track") String track) {
    return Response.ok(payloadService.getPayloads(track)).build();
  }
}
