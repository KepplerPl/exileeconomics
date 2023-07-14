package com.exileeconomics.mapper.deserializer;

import com.google.gson.JsonObject;

public interface ApiDeserializer<T> {
    T fromJson(JsonObject jsonObject);
}
