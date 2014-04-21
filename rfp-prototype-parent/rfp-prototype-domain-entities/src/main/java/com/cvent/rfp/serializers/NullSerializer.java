package com.cvent.rfp.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * This is a custom null serializer created to handle special types that may be coming null from
 * the data source. These values are defaulted to empty representation indicating non-availability
 * of the data. This class can be further extended to handle other data types and fields as needed
 * 
 * @author pmisra
 *
 */
public class NullSerializer extends JsonSerializer<Object> {

    /* (non-Javadoc)
     * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
     */
    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException,
            JsonProcessingException {
        if (jgen.getOutputContext().getCurrentName().contains("Date")) {
            jgen.writeString("1900-01-01 00:00:00.0");
        } else {
            jgen.writeString("");
        }
    }

}
