package com.example.exileeconomics.producer_consumer;

import com.example.exileeconomics.entity.NextIdEntity;
import com.example.exileeconomics.http.ApiHeaderBag;
import com.example.exileeconomics.http.RequestHandler;
import com.example.exileeconomics.http.Throttler;
import com.example.exileeconomics.producer_consumer.producer.PublicStashTabsProducer;
import com.example.exileeconomics.repository.NextIdRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

@ExtendWith(SpringExtension.class)
public class PublicStashTabsProducerTest {

    private final RequestHandler requestHandler = Mockito.mock(RequestHandler.class);
    private final NextIdRepository nextIdRepository = Mockito.mock(NextIdRepository.class);
    private final CountDownLatch countDownLatch = new CountDownLatch(0);
    private final BlockingQueue<String> jsonResponses = new ArrayBlockingQueue<>(10);
    private final Throttler throttler = new Throttler();
    private final ApiHeaderBag apiHeaderBag = new ApiHeaderBag();

    @Test
    public void testDoRun() throws IOException, InterruptedException {
        NextIdEntity nextIdEntity = new NextIdEntity();
        nextIdEntity.setNextId("2000655901-1996622915-1930774387-2140863898-2077103178");
        nextIdEntity.setId(1L);
        Collection<NextIdEntity> nextIdEntityCollection = new ArrayList<>(List.of(nextIdEntity));
        Mockito.when(nextIdRepository.mostCurrentNextId()).thenReturn(nextIdEntityCollection);

        HttpURLConnection connection = Mockito.mock(HttpURLConnection.class);

        Mockito.when(requestHandler.getPublicStashTabs(Mockito.anyString())).thenReturn(connection);

        Mockito.when(connection.getHeaderFields()).thenReturn(getMockHeaders());
        Mockito.when(connection.getResponseCode()).thenReturn(200);
        Mockito.when(requestHandler.getResponseAsString(connection)).thenReturn(
                Files.readString(Paths.get("src/test/java/com/example/exileeconomics/producer_consumer/test.json"))
        );

        Mockito.when(nextIdRepository.save(Mockito.any())).thenAnswer(i -> i.getArguments()[0]);

        ExecutorService service = Executors.newSingleThreadExecutor();

        PublicStashTabsProducer publicStashTabsProducer = new PublicStashTabsProducer(
                jsonResponses,
                throttler,
                requestHandler,
                apiHeaderBag,
                nextIdRepository,
                countDownLatch
        );

        service.execute(publicStashTabsProducer);

        service.shutdown();
        service.awaitTermination(1L, TimeUnit.SECONDS);

        Mockito.verify(requestHandler, Mockito.times(3)).getPublicStashTabs(Mockito.anyString());
        Mockito.verify(requestHandler, Mockito.times(1)).getResponseAsString(connection);
    }

    private Map<String, List<String>> getMockHeaders() {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("X-Rate-Limit-Ip", new ArrayList<>(List.of(
                "2:1:60"
        )));
        headers.put("X-Next-Change-Id", new ArrayList<>(List.of(
                "2000655906-1996622915-1930774387-2140863898-2077103178"
        )));
        headers.put("X-Rate-Limit-Rules", new ArrayList<>(List.of(
                "Ip"
        )));
        headers.put("X-Rate-Limit-Policy", new ArrayList<>(List.of(
                "public-stash-tabs-request-limit-pc"
        )));
        headers.put("X-Rate-Limit-Ip-State", new ArrayList<>(List.of(
                "1:1:0"
        )));
        return headers;
    }

}
