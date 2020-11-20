package cz.zpapez.springfeignclients;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import cz.zpapez.springfeignclients.slack.SlackFeignClient;
import cz.zpapez.springfeignclients.zephyr.ZephyrFeignClient;

@SpringBootApplication
@EnableFeignClients(clients = {
        SlackFeignClient.class,
        ZephyrFeignClient.class
})
public class SpringFeignClientsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringFeignClientsApplication.class, args);
    }

}
