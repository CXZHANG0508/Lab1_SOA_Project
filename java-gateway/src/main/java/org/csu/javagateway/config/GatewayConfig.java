package org.csu.javagateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GatewayConfig {
    //可以在其他地方 @Autowired 来注入它
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}