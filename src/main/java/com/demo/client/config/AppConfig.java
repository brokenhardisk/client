package com.demo.client.config;

import com.demo.client.service.ApiConnectorService;
import com.demo.client.service.impl.ApiConnectorServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Value("${tt.app.uri}")
    private String timeTrackerApplicationUri;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        RestTemplate restTemp = builder.build();
        restTemp.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
         return restTemp;
    }

    @Bean
    public ApiConnectorService apiConnectorService(RestTemplate restTemplate){
        return new ApiConnectorServiceImpl(restTemplate,timeTrackerApplicationUri);
    }
}
