package com.example.exileeconomics.http;

import com.example.exileeconomics.Properties;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.antlr.v4.runtime.misc.Utils.readFile;

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
//        return Files.readString(Paths.get("C:\\Users\\Mine\\Desktop\\exileeconomics\\test.json"));

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
