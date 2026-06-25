package cl.duoc.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gateway")
public class TestController {

    @GetMapping("/status")
    public String status(){
        return "API Gateway Veranum operativo al 100%";
    }
}
