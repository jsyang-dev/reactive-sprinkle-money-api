package com.kakaopay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SprinkleMoneyApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(SprinkleMoneyApiApplication.class, args);
  }
}
