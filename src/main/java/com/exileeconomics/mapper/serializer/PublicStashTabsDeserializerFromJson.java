package com.exileeconomics.mapper.serializer;

import com.exileeconomics.mapper.PublicStashTabsDTO;
import com.exileeconomics.mapper.deserializer.ApiDeserializer;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PublicStashTabsDeserializerFromJson implements JsonDeserializer<PublicStashTabsDTO> {
    private final ApiDeserializer<PublicStashTabsDTO> deserializer;

    public PublicStashTabsDeserializerFromJson(ApiDeserializer<PublicStashTabsDTO> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public PublicStashTabsDTO deserialize(JsonElement jElement, Type typeOfT, JsonDeserializationContext context) {
        return deserializer.fromJson(jElement.getAsJsonObject());
    }
}
