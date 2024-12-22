package com.hsbc.trade.infrastructure;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan({"com.hsbc.trade"})
class InfrastructureApplicationTests {

    @Test
    void contextLoads() {
    }

}
