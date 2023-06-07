package co.edu.uniandes.dse.culturasgastronomicas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("*")
public class DefaultController {
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Map<String, String> welcome() {
        HashMap<String, String> map = new HashMap<>();
        map.put("status", "OK");
        map.put("message", "REST API for culturas gastronómicas is running");
        return map;
    }
}
