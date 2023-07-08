package com.example.exileeconomics.mapper.serializer;

import com.example.exileeconomics.mapper.PublicStashTabsDao;
import com.example.exileeconomics.mapper.deserializer.ApiDeserializer;
import com.google.gson.*;

import java.lang.reflect.Type;

public class PublicStashTabsDeserializerFromJson implements JsonDeserializer<PublicStashTabsDao> {

    private final ApiDeserializer<PublicStashTabsDao> deserializer;

    public PublicStashTabsDeserializerFromJson(ApiDeserializer<PublicStashTabsDao> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public PublicStashTabsDao deserialize(JsonElement jElement, Type typeOfT, JsonDeserializationContext context) {
        return deserializer.fromJson(jElement.getAsJsonObject());
    }
}
