package cz.zpapez.springfeignclients.slack;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.zpapez.springfeignclients.slack.dto.SlackMessageRequestDto;
import cz.zpapez.springfeignclients.slack.dto.SlackMessageResponseDto;


@FeignClient(name = "Slack", url = "${slack.baseUrl}", configuration = SlackClientConfiguration.class)
public interface SlackFeignClient {

    @RequestMapping(
            value = "/chat.postMessage",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    SlackMessageResponseDto postSlackMessage(
            @RequestBody(required = true) SlackMessageRequestDto messageRequest);
}
