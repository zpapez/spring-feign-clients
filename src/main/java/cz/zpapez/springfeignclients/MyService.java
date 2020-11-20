package cz.zpapez.springfeignclients;

import org.springframework.stereotype.Service;

import cz.zpapez.springfeignclients.slack.SlackFeignClient;
import cz.zpapez.springfeignclients.slack.dto.SlackMessageRequestDto;
import cz.zpapez.springfeignclients.zephyr.ZephyrFeignClient;
import cz.zpapez.springfeignclients.zephyr.dto.ExecutionListResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyService {

    private final ZephyrFeignClient zephyrClient;
    private final SlackFeignClient slackClient;

    public void fetchData() {

        ExecutionListResponseDto executionsResponse = zephyrClient.getListOfExecutions("5112096");
        log.info("Found executions {}", executionsResponse.getExecutions());

        slackClient.postSlackMessage(new SlackMessageRequestDto("testing-channel", "test message to Slack"));

    }

}
