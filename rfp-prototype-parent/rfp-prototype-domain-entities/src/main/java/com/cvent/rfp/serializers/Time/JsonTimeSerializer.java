package com.cvent.rfp.serializers.Time;

import com.cvent.rfp.util.DateFormatHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.ParseException;

public class JsonTimeSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String dateString, JsonGenerator jg, SerializerProvider sp) throws IOException, JsonProcessingException {
        try {
            jg.writeString(DateFormatHelper.timeFormat(dateString));
        } catch (ParseException ex) {
            //log exception
        }
    }
}
