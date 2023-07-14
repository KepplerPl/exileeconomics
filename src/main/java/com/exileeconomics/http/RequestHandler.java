package com.exileeconomics.http;

import com.exileeconomics.Properties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public final class RequestHandler {
    private final Properties properties;

    public RequestHandler(Properties properties) {
        this.properties = properties;
    }

    public HttpURLConnection getPublicStashTabs(String id) throws IOException {
        URL url = new URL(properties.getSchema() + "://" + properties.getHost() + "/" + properties.getPsapi() + "?id=" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", properties.getUserAgent());
        con.setRequestProperty("Authorization", "Bearer " + properties.getBearerToken());

        return con;
    }

    public String getResponseAsString(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

}
