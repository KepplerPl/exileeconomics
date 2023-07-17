package com.exileeconomics.http;

import com.exileeconomics.AppProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class RequestHandler {
    private final AppProperties appProperties;

    public RequestHandler(@Autowired AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public HttpURLConnection getPublicStashTabs(String id) throws IOException {
        URL url = new URL(appProperties.getSchema() + "://" + appProperties.getHost() + "/" + appProperties.getPsapi() + "?id=" + id);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        con.setRequestProperty("User-Agent", appProperties.getUserAgent());
        con.setRequestProperty("Authorization", "Bearer " + appProperties.getBearerToken());

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
