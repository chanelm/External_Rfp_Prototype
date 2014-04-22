package com.cvent.rfp.serializers;

import com.cvent.rfp.util.DateFormatHelper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;

public class JsonDateTimeDeSerializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        String dateString = "";
        String dateValue=jp.getValueAsString();
        if (dateValue!=null && dateValue.length()>0) {
            try {
                dateString = DateFormatHelper.dateFormat(dateValue);
            } catch (ParseException ex) {
                //Log exception
            }
        }
        return dateString;
    }

}
