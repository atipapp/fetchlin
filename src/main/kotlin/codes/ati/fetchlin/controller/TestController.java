package codes.ati.fetchlin.controller;

import codes.ati.fetchlin.service.ClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private ClientService clientService;

    public TestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("hello")
    public String helloWorld() {
        return clientService.fetch("https://www.aut.bme.hu/Course/VIAUMA03");
    }

}
