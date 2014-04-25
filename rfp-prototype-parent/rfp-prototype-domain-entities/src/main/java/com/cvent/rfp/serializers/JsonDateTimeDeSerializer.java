package com.cvent.rfp.serializers;

import com.cvent.rfp.util.DateFormatHelper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yxie
 * De-serialize DateTime in date time format.
 */
public class JsonDateTimeDeSerializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jp, DeserializationContext dc) throws IOException {
        String dateString = "";
        String dateValue = jp.getValueAsString();
        if (dateValue != null && dateValue.length() > 0) {
            try {
                dateString = DateFormatHelper.dateFormat(dateValue);
            } catch (ParseException ex) {
                //Log exception
                Logger.getLogger(JsonDateTimeDeSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dateString;
    }

}
