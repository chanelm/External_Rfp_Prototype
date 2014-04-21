/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cvent.rfp.resources;

import com.cvent.rfp.HelloWorld;
import com.wordnik.swagger.annotations.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path(value = "/hello")
@Api(value = "/hello", description = "Test Hello World with swagger stuff.")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private final HelloWorld conf;

    public HelloWorldResource(HelloWorld conf) {
        this.conf = conf;
    }

    @GET
    @Path("/{helloId}")
    @ApiOperation(value = "Get Hello World Content", notes = "This method gets Hello World version information.", response = HelloWorld.class)
    @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Bad Request"),
      @ApiResponse(code = 404, message = "Not Found"),
      @ApiResponse(code = 500, message = "Internal Server Error")
    })
    public Response sayHello( 
            @ApiParam(value="ID for retrieve hello world", required = false) 
            @PathParam("helloId") int helloId) throws Exception {
        return Response.ok(conf).build();
    }
}