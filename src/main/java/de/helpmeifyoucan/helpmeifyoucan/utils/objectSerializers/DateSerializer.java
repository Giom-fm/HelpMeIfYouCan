package de.helpmeifyoucan.helpmeifyoucan.utils.objectSerializers;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateSerializer extends StdConverter<Date, String> {


    @Override
    public String convert(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"); // Quoted "Z" to indicate
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        var dateAsString = df.format(date);

        return dateAsString;
    }
}
