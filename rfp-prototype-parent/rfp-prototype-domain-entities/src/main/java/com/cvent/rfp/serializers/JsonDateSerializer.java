package com.cvent.rfp.serializers;

import com.cvent.rfp.util.DateFormatHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yxie
 * Serialize DateTime in date only format.
 */
public class JsonDateSerializer extends JsonSerializer<String> {

    @Override
    public void serialize(String dateString, JsonGenerator jg, SerializerProvider sp) throws IOException {
        try {
            jg.writeString(DateFormatHelper.dateOnlyFormat(dateString));
        } catch (ParseException ex) {
            //log exception
            Logger.getLogger(JsonDateSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
