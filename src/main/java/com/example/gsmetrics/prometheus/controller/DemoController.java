package com.example.gsmetrics.prometheus.controller;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping(value = "/demo")
public class DemoController {

    @GetMapping("/random-error")
    @Timed("random_error")
    public ResponseEntity<Void> random_error() {
        try {
            TimeUnit.MILLISECONDS.sleep(RandomUtils.nextInt(0, 2000));
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }

        int randomNumber = RandomUtils.nextInt(0, 10);

        switch (randomNumber) {
            case 5:
                throw new IllegalArgumentException("Case 5");
            case 9:
                throw new RuntimeException("Case 9");
            default:
                log.debug("Case NO_ERROR");
        }

        return ResponseEntity.ok().build();
    }

}
