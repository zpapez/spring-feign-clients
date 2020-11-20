package cz.zpapez.springfeignclients.slack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class SlackClientConfiguration {

    @Value("${slack.app.oauth.accessToken}")
    private String slackOauthAccessToken;

    @Bean
    public RequestInterceptor bearerTokenRequestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                template.header("Authorization",
                        String.format("Bearer %s", slackOauthAccessToken));
            }
        };
    }

}
