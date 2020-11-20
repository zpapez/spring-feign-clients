package cz.zpapez.springfeignclients;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class Controller {

    private final MyService service;

    @GetMapping("/resource")
    public String getResource() {

        service.fetchData();

        return "OK result";
    }

}
