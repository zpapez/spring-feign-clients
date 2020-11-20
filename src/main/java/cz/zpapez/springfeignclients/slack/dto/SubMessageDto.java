package cz.zpapez.springfeignclients.slack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubMessageDto {
    @JsonProperty("bot_id")
    private String botId;
    private String type;
    private String text;
    private String user;
}
