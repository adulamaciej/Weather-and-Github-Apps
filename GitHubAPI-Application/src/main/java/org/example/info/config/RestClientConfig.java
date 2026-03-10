package org.example.info.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {


    @Value("${github.api.url}")
    private String githubApiUrl;

    @Bean
    public RestClient githubRestClient() {
        return RestClient.builder()
                .baseUrl(githubApiUrl)
                .defaultHeader(HttpHeaders.USER_AGENT, "GitHub-Client-Information")
                .build();
    }
}