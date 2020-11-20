package cz.zpapez.springfeignclients.zephyr;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cz.zpapez.springfeignclients.zephyr.dto.ExecutionListResponseDto;


@FeignClient(name = "Zephyr", url = "${zephyr.api.baseUrl}", configuration = ZephyrFeignClientConfiguration.class)
public interface ZephyrFeignClient {


    @RequestMapping(
            value = "/execution",
            method = RequestMethod.GET)
    ExecutionListResponseDto getListOfExecutions(
            @RequestParam(name = "issueId") String issueId);

}
