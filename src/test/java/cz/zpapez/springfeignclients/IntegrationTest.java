package cz.zpapez.springfeignclients;


import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.anyRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.TestPropertySourceUtils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.BasicCredentials;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestListener;
import com.github.tomakehurst.wiremock.http.Response;
import com.google.common.io.Resources;

import cz.zpapez.springfeignclients.testutil.WireMockExtension;
import cz.zpapez.springfeignclients.testutil.WireMockInstance;

@ExtendWith({WireMockExtension.class, SpringExtension.class})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "logging.level.root=INFO",
                "zephyr.api.username=zephyrTestUser",
                "zephyr.api.password=zephyrTestPassword",
                "slack.app.oauth.accessToken=testFakeToken"
        })
@ContextConfiguration(initializers = AppIntegrationTestPropertyInitializer.class)
public class IntegrationTest {

    public static WireMockServer zephyrWiremockServer;
    public static WireMockServer slackWiremockServer;

    private static boolean isZephyrAuthSingleValued;
    private static boolean isSlackAuthSingleValued;

    @Autowired
    private Controller controller;


    @BeforeAll
    public static void beforeAll(@WireMockInstance WireMockServer server1, @WireMockInstance WireMockServer server2) {
        zephyrWiremockServer = server1;
        slackWiremockServer = server2;

        zephyrWiremockServer.addMockServiceRequestListener(new RequestListener() {
            @Override
            public void requestReceived(Request request, Response response) {
                // if we would fail directly here in listener, JUnit error would be really hard to understand (as no response would be generated)
                // therefore saving the value to assert it later in main test flow
                isZephyrAuthSingleValued = request.getHeaders().getHeader("Authorization").isSingleValued();
            }
        });

        slackWiremockServer.addMockServiceRequestListener(new RequestListener() {
            @Override
            public void requestReceived(Request request, Response response) {
                // if we would fail directly here in listener, JUnit error would be really hard to understand (as no response would be generated)
                // therefore saving the value to assert it later in main test flow
                isSlackAuthSingleValued = request.getHeaders().getHeader("Authorization").isSingleValued();
            }
        });
    }

    @AfterEach
    public void afterEach() {
        assertTrue(isZephyrAuthSingleValued,
                "There must be only one 'Authorization' header in all Zephyr requests");
        assertTrue(isSlackAuthSingleValued,
                "There must be only one 'Authorization' header in all Slack requests");

        zephyrWiremockServer.verify(anyRequestedFor(anyUrl())
                .withBasicAuth(new BasicCredentials("zephyrTestUser", "zephyrTestPassword")));

        slackWiremockServer.verify(anyRequestedFor(anyUrl())
                .withHeader("Authorization", equalTo("Bearer testFakeToken")));

    }

    @Test
    public void testAuthenticatedCallToJiraAndSlack() throws IOException {
        // mock Zephyr API response:
        String zephyrResponse = getResourceFileContent("IntegrationTest/testAuthenticatedCallToJiraAndSlack/zephyr_list_executions.json");
        zephyrWiremockServer.stubFor(get(urlEqualTo("/jira/rest/zapi/latest/execution?issueId=5112096"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(zephyrResponse)));

        // mock Slack API response:
        String slackResponse = getResourceFileContent("IntegrationTest/testAuthenticatedCallToJiraAndSlack/slack_response.json");
        slackWiremockServer.stubFor(post(urlEqualTo("/api/chat.postMessage"))
                .withRequestBody(matchingJsonPath("$[?(@.channel == 'testing-channel')]"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json;charset=utf-8")
                        .withBody(slackResponse)));


        // test
        String result = controller.getResource();

        // verify
        assertEquals("OK result", result);
    }

    private String getResourceFileContent(String resourceName) throws IOException {
        URL url = Resources.getResource(resourceName);
        return Resources.toString(url, StandardCharsets.UTF_8);
    }

}

class AppIntegrationTestPropertyInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                "zephyr.api.baseUrl="
                        + String.format("%s/jira/rest/zapi/latest",
                                IntegrationTest.zephyrWiremockServer.baseUrl()));

        TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext,
                "slack.baseUrl="
                        + String.format("%s/api",
                                IntegrationTest.slackWiremockServer.baseUrl()));
    }
}