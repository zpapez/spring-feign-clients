package cz.zpapez.springfeignclients.zephyr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.auth.BasicAuthRequestInterceptor;

public class ZephyrFeignClientConfiguration {

    @Value("${zephyr.api.username}")
    private String jiraApiUsername;

    @Value("${zephyr.api.password}")
    private String jiraApiPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(jiraApiUsername, jiraApiPassword);
    }

}
