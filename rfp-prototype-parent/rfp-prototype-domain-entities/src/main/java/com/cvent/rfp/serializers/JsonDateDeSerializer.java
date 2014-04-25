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
 * De-serialize DateTime in date only format.
 */
public class JsonDateDeSerializer extends JsonDeserializer<String> {

    /**
     *
     * @param jp
     * @param dc
     * @return
     * @throws IOException
     */
    @Override
    public String deserialize(JsonParser jp, DeserializationContext dc) throws IOException {

        String dateString = "";
        String dateValue = jp.getValueAsString();
        if (dateValue != null && dateValue.length() > 0) {
            try {
                String dateOnlyString = DateFormatHelper.dateOnlyFormat(dateValue);
                dateString = String.format("%s %s", dateOnlyString, DateFormatHelper.getTimePostfixForDate());
            } catch (ParseException ex) {
                //Log exception
                Logger.getLogger(JsonDateDeSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return dateString;
    }
}
