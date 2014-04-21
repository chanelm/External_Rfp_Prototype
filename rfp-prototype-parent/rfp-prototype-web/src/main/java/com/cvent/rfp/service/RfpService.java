/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cvent.rfp.service;


import com.cvent.dropwizard.mybatis.MyBatisFactory;
import com.cvent.filters.CORSFilter;
import com.cvent.rfp.dao.RfpDAO;
import com.cvent.rfp.health.RfpHealthCheck;
import com.cvent.rfp.mapper.RfpMapper;
import com.cvent.rfp.resources.HelloWorldResource;
import com.cvent.rfp.resources.RfpResource;
import com.wordnik.swagger.config.ConfigFactory;
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
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.db.ManagedDataSource;
import com.yammer.dropwizard.db.ManagedDataSourceFactory;
import java.text.SimpleDateFormat;
import org.apache.ibatis.session.SqlSessionFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

/**
 *
 * @author yxie
 */
public class RfpService extends Service<RfpServiceConfiguration> {

    @Override
    public void initialize(Bootstrap<RfpServiceConfiguration> btstrp) {
        btstrp.setName("rfp-prototype");
    }

    @Override
    public void run(RfpServiceConfiguration t, Environment e) throws Exception {
        e.addResource(new HelloWorldResource(t.getMessages()));
        e.addHealthCheck(new RfpHealthCheck());
        
        setMapperDateFormat(e);
        initializeMyBatis(t, e);
        initializeSwagger(t, e);
        
        e.addFilter(new CORSFilter(), "/*");

    }
    
    public void setMapperDateFormat(Environment e)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.s");
        e.getObjectMapperFactory().setDateFormat(dateFormat);
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
        e.addResource(new RfpResource(rfpDAO));
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
