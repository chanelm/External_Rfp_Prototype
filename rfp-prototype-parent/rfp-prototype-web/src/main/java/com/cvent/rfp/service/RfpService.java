/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cvent.rfp.service;


import com.cvent.auth.AuthenticatorMethod;
import com.cvent.auth.FakeBearerAuthorizationFilter;
import com.cvent.auth.GrantedAPIKey;
import com.cvent.auth.MultiPlexingAuthProvider;
import com.cvent.auth.NoOpAuthenticator;
import com.cvent.auth.RemoteAPIKeyAuthenticator;
import com.cvent.auth.RemoteBearerAuthenticator;
import com.cvent.auth.SecurityContext;
import com.cvent.dropwizard.mybatis.MyBatisFactory;
import com.cvent.filters.CORSFilter;
import com.cvent.filters.SwaggerInternalFilter;
import com.cvent.rfp.dao.LuDAO;
import com.cvent.rfp.dao.RfpDAO;
import com.cvent.rfp.health.RfpHealthCheck;
import com.cvent.rfp.mapper.RfpMapper;
import com.cvent.rfp.resources.HelloWorldResource;
import com.cvent.rfp.resources.RfpResource;
import com.wordnik.swagger.config.ConfigFactory;
import com.wordnik.swagger.config.FilterFactory;
import com.wordnik.swagger.config.ScannerFactory;
import com.wordnik.swagger.config.SwaggerConfig;
import com.wordnik.swagger.converter.ModelConverters;
import com.wordnik.swagger.converter.OverrideConverter;
import com.wordnik.swagger.jaxrs.config.DefaultJaxrsScanner;
import com.wordnik.swagger.jaxrs.listing.ApiDeclarationProvider;
import com.wordnik.swagger.jaxrs.listing.ApiListingResourceJSON;
import com.wordnik.swagger.jaxrs.listing.ResourceListingProvider;
import com.wordnik.swagger.jaxrs.reader.DefaultJaxrsApiReader;
import com.wordnik.swagger.reader.ClassReaders;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.ManagedDataSource;
import com.yammer.dropwizard.db.ManagedDataSourceFactory;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author yxie
 */
public class RfpService extends Service<RfpServiceConfiguration> {
    
    /**
     * A constant representing the environment dev.json configuration
     */
    private static final String ENV_DEV = "dev";

    @Override
    public void initialize(Bootstrap<RfpServiceConfiguration> btstrp) {
        btstrp.setName("rfp-prototype");
    }

    @Override
    public void run(RfpServiceConfiguration t, Environment e) throws Exception {
        e.addResource(new HelloWorldResource(t.getMessages()));
        e.addHealthCheck(new RfpHealthCheck());
        e.addFilter(new CORSFilter(), "/*");        
        
        
        initializeMyBatis(t, e);
        initializeAuthentication(t, e);
        initializeSwagger(t, e);

    }
    
//    public void setMapperDateFormat(Environment e)
//    {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.s");
//        e.getObjectMapperFactory().setDateFormat(dateFormat);
//    }
    
/**
     * Initialize authentication for this service
     *
     * @param authClientConfiguration
     * @param environment
     */
    @SuppressWarnings("unchecked") //supressed because of filters.add() annotation and not ready for jdk8 yet
    private void initializeAuthentication(RfpServiceConfiguration configuration, Environment environment) {
        Map<AuthenticatorMethod, Authenticator<SecurityContext, ? extends GrantedAPIKey>> authenticators
                = new HashMap<>();

        if (!configuration.getAuthRequired()) {
            environment.getJerseyResourceConfig().getContainerRequestFilters().add(new FakeBearerAuthorizationFilter());
            authenticators.put(AuthenticatorMethod.BEARER, new NoOpAuthenticator(configuration.getNoOpAccessToken()));
            authenticators.put(AuthenticatorMethod.API_KEY, new NoOpAuthenticator(configuration.getNoOpAccessToken()));
        } else {
            RemoteBearerAuthenticator remoteBearerAuthenticator
                    = new RemoteBearerAuthenticator(configuration.getAuthClientConfiguration(),
                            environment, configuration.getApiKey());
            RemoteAPIKeyAuthenticator remoteAPIKeyAuthenticator
                    = new RemoteAPIKeyAuthenticator(configuration.getAuthClientConfiguration(),
                            environment, configuration.getApiKey());
            authenticators.put(AuthenticatorMethod.BEARER, remoteBearerAuthenticator);
            authenticators.put(AuthenticatorMethod.API_KEY, remoteAPIKeyAuthenticator);
        }

        environment.addProvider(new MultiPlexingAuthProvider<>(authenticators, "api.cvent.com"));
    }
    
    public void initializeMyBatis(RfpServiceConfiguration t, Environment e) throws Exception
    {
        //setup myBatis
        
        final ManagedDataSourceFactory dataSourceFactory = new ManagedDataSourceFactory();
        final ManagedDataSource ds = dataSourceFactory.build(t.getDatabaseConfiguration());

        MyBatisFactory factory = new MyBatisFactory();
        SqlSessionFactory sessionFactory = factory.build(e, t.getDatabaseConfiguration(), ds, "sqlserver");
        sessionFactory.getConfiguration().addMapper(RfpMapper.class);
        
        RfpDAO rfpDAO = new RfpDAO(sessionFactory);
        LuDAO luDAO = new LuDAO(sessionFactory);
        e.addResource(new RfpResource(rfpDAO, luDAO));
        
        

    }
    
    public void initializeSwagger(RfpServiceConfiguration t, Environment e)
    {
        // Swagger Resource
        e.addResource(new ApiListingResourceJSON());

        // Swagger providers
        e.addProvider(new ApiDeclarationProvider());
        e.addProvider(new ResourceListingProvider());

        // Swagger Scanner, which finds all the resources for @Api Annotations
        ScannerFactory.setScanner(new DefaultJaxrsScanner());

        // Add the reader, which scans the resources and extracts the resource information
        ClassReaders.setReader(new DefaultJaxrsApiReader());

        // Set the swagger config options
        SwaggerConfig config = ConfigFactory.config();
        config.setApiVersion(t.getApiVersion());
        config.setBasePath(t.getSwaggerBasePath());
        
        FilterFactory.filter_$eq(new SwaggerInternalFilter());
        overrideUUIDObjectForSwagger();        
    }

    /**
     * Forces Swagger to render the model of the UUID class into a more user-friendly String format as opposed to the
     * default, which would represent the UUID class as an object containing leastSignificantBits & mostSignificantBits
     */
    private void overrideUUIDObjectForSwagger() {
        String jsonUUID = "{\"id\": \"UUID\", "
                + "\"properties\": {"
                + "   \"value\": {"
                + "       \"required\": false,"
                + "       \"description\": \"UUID in string format, i.e 00000000-0000-0000-0000-000000000000\","
                + "       \"notes\": \"Add any notes you like here\","
                + "       \"type\": \"string\","
                + "       \"format\": \"UUID\" "
                + "             }"
                + "       }"
                + "}";
        OverrideConverter converter = new OverrideConverter();
        converter.add("java.util.UUID", jsonUUID);
        ModelConverters.addConverter(converter, true);
    }

    public static void main(String[] args) throws Exception {
        new RfpService().run(args);
    }
    
}
