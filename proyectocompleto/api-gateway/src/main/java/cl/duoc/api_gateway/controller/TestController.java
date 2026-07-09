package cl.duoc.api_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/gateway")
public class TestController {

    @GetMapping("/status")
    public Map<String, String> obtenerEstado() {
        Map<String, String> response = new HashMap<>();
        response.put("nombre", "api-gateway");
        response.put("estado", "operativo");
        return response;
    }
}