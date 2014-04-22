package com.cvent.rfp.service;

import com.cvent.auth.GrantedAccessToken;
import com.cvent.client.ClientConfiguration;
import com.cvent.rfp.HelloWorld;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.db.DatabaseConfiguration;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

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
    
    @JsonProperty
    @Valid
    private GrantedAccessToken noOpAccessToken;
    
    @NotEmpty
    @JsonProperty
    private String environmentName;
    
    @Valid
    @NotNull
    @JsonProperty
    private ClientConfiguration authClient;  
    
    @JsonProperty
    @NotNull
    @NotEmpty
    private String apiKey;
    
    @JsonProperty
    private boolean authRequired = true;
    
    public boolean getAuthRequired() {
        return authRequired;
    }
    
    
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
    
    public String getEnvironmentName() {
        return environmentName;
    }
    
    public GrantedAccessToken getNoOpAccessToken() {
        return noOpAccessToken;
    }
    
    public ClientConfiguration getAuthClientConfiguration() {
        return authClient;
    }    
    
    public String getApiKey()
    {
        return apiKey;
    }
}
