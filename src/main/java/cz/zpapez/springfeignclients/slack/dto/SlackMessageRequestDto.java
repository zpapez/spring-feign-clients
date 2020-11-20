package cz.zpapez.springfeignclients.slack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SlackMessageRequestDto {
    private String channel;
    private String text;
}
