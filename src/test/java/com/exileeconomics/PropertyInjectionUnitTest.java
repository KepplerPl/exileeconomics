package com.exileeconomics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@TestPropertySource("classpath:application.properties")
public class PropertyInjectionUnitTest {

    @Value("${exileeconomics.bearer_token}")
    private String bearerToken;
    @Value("${exileeconomics.api.host}")
    private String host;
    @Value("${exileeconomics.api.schema}")
    private String schema;
    @Value("${exileeconomics.api.scope.service.psapi}")
    private String psapi;
    @Value("${exileeconomics.api.user_agent}")
    private String userAgent;

    @Test
    public void whenPropertyProvided_bearer_token_thenProperlyInjected() {
        assertEquals("d7633296c73b6689155cc7a67ec4e47ad6094fd2", bearerToken);
    }
    @Test
    public void whenPropertyProvided_exileeconomics_api_host_thenProperlyInjected() {
        assertEquals("api.pathofexile.com", host);
    }
    @Test
    public void whenPropertyProvided_exileeconomics_api_schema_thenProperlyInjected() {
        assertEquals("https", schema);
    }
    @Test
    public void whenPropertyProvided_exileeconomics_api_scope_service_psapi_thenProperlyInjected() {
        assertEquals("public-stash-tabs", psapi);
    }
    @Test
    public void whenPropertyProvided_exileeconomics_api_user_agent_thenProperlyInjected() {
        assertEquals("OAuth exileeconomics/1.0.0 (contact: junkypic@gmail.com) StrictMode", userAgent);
    }
}