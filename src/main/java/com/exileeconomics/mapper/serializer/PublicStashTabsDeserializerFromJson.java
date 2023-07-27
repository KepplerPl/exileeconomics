package com.exileeconomics.mapper.serializer;

import com.exileeconomics.mapper.ItemDTO;
import com.exileeconomics.mapper.deserializer.ApiDeserializer;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.List;

public class PublicStashTabsDeserializerFromJson implements JsonDeserializer<List<ItemDTO>> {
    private final ApiDeserializer<List<ItemDTO>> deserializer;

    public PublicStashTabsDeserializerFromJson(ApiDeserializer<List<ItemDTO>> deserializer) {
        this.deserializer = deserializer;
    }

    @Override
    public List<ItemDTO> deserialize(JsonElement jElement, Type typeOfT, JsonDeserializationContext context) {
        return deserializer.fromJson(jElement.getAsJsonObject());
    }
}
