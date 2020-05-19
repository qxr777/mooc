package edu.whut.cs.jee.mooc.upms.controller;

import edu.whut.cs.jee.mooc.common.handler.NotControllerResponseAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/health")
    @NotControllerResponseAdvice
    public String health() {
        return "success";
    }
}
