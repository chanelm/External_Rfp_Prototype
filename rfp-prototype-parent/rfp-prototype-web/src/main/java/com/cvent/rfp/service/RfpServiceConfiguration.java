package com.cvent.rfp.service;

import com.cvent.rfp.HelloWorld;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class RfpServiceConfiguration extends Configuration {

    @Valid
    @JsonProperty
    private HelloWorld messages;
    
    @Valid
    @JsonProperty
    private String apiVersion;
    
    @Valid
    @JsonProperty
    private String swaggerBasePath;
    
    @Valid
    @NotNull
    @JsonProperty
    private DatabaseConfiguration databaseConfig;
    
    public DatabaseConfiguration getDatabaseConfiguration()
    {
        return databaseConfig;
    }
    
    public String getApiVersion()
    {
        return apiVersion;
    }
    
    public String getSwaggerBasePath()
    {
        return swaggerBasePath;
    }
    
    public HelloWorld getMessages() {
        return messages;
    }

    public void setMessages(HelloWorld messages) {
        this.messages = messages;
    }
}
