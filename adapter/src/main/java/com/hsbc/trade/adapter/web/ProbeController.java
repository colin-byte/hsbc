package com.hsbc.trade.adapter.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProbeController {
    @RequestMapping(value = "/health", method = {RequestMethod.GET, RequestMethod.POST})
    public String health() {
        return "ok";
    }

    @RequestMapping(value = "/ready", method = {RequestMethod.GET, RequestMethod.POST})
    public String ready() {
        return "ok";
    }
}
