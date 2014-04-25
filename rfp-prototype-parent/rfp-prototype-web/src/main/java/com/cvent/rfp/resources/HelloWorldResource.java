package com.cvent.rfp.resources;

import com.cvent.auth.AuthenticatorMethod;
import com.cvent.auth.Authority;
import com.cvent.auth.GrantedAPIKey;
import com.cvent.filters.SwaggerInternalFilter;
import com.cvent.rfp.HelloWorld;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.eclipse.jetty.http.HttpStatus;

/**
 * @author yxie
 */
@Path(value = "/hello")
@Api(value = "/hello", description = "Test Hello World with swagger stuff.")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    private final HelloWorld conf;

    public HelloWorldResource(HelloWorld conf) {
        this.conf = conf;
    }

    /**
     *
     * @param grantedAPIKey
     * @param helloId
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{helloId}")
    @ApiOperation(value = "Get Hello World Content", notes = "This method gets Hello World version information.",
            response = HelloWorld.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.BAD_REQUEST_400, message = "Bad Request"),
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = "Not Found"),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = "Internal Server Error")
    })
    public Response sayHello(
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "ID for retrieve hello world", required = false)
            @PathParam("helloId") int helloId) throws Exception {
        return Response.ok(conf).build();
    }
}
