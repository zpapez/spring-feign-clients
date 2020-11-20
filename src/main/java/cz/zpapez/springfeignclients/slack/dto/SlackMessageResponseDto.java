package cz.zpapez.springfeignclients.slack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackMessageResponseDto {

    boolean ok;
    String channel;
    String ts;
    String error;
    String warning;
    SubMessageDto message;
}