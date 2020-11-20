package cz.zpapez.springfeignclients.zephyr.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExecutionListResponseDto {

    private List<ExecutionDto> executions;
}
