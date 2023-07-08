package com.example.exileeconomics;

import com.example.exileeconomics.producer_consumer.PublicStashTabsOrchestrator;
import com.example.exileeconomics.http.RequestHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HelloWorldController {

    private RequestHandler requestHandler;
    private Properties properties;
    private PublicStashTabsOrchestrator publicStashTabsOrchestrator;

    public HelloWorldController(
            @Autowired RequestHandler requestHandler,
            @Autowired Properties properties,
            @Autowired PublicStashTabsOrchestrator publicStashTabsOrchestrator
    ) {
        this.requestHandler = requestHandler;
        this.properties = properties;
        this.publicStashTabsOrchestrator = publicStashTabsOrchestrator;
    }

    @RequestMapping("/")
    public String helloWorld() throws IOException {
//        orchestrator.execute();
//        URL url = new URL(properties.getSchema() + "://" + properties.getHost() + "/" + properties.getPsapi() + "?id="+"1996822708-1992169803-1927126708-2136375619-2073276180");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setRequestMethod("GET");
//        con.setRequestProperty("User-Agent", properties.getUserAgent());
//        con.setRequestProperty("Authorization", "Bearer "+properties.getBearerToken());
//
//
//        final InputStream istream = url.openStream();
//        final OutputStream ostream = new FileOutputStream("data.txt");
//
//        final byte[] buffer = new byte[1024*8];
//        while (true) {
//            final int len = istream.read(buffer);
//            if (len <= 0) {
//                break;
//            }
//            ostream.write(buffer, 0, len);
//        }


        return "Hello World from Spring Boot";
    }
}