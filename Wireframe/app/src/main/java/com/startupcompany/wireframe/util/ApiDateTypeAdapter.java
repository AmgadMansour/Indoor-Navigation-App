package com.startupcompany.wireframe.util;

/**
 * Created by mohamedabdel-azeem on 3/16/16.
 */

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ApiDateTypeAdapter implements JsonDeserializer<Date> {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ", Locale.getDefault());

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            String dateString = json.getAsJsonPrimitive().getAsString();
            if (dateString != null && dateString.trim().length() > 0) {
                try {
                    return dateTimeFormat.parse(dateString);
                } catch (ParseException e) {
                    return dateFormat.parse(dateString);
                }
            } else {
                return null;
            }
        } catch (ParseException e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }
}
