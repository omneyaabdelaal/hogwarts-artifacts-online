package com.omneya.hogwarts.hogwartsartifactsonline;

import com.omneya.hogwarts.hogwartsartifactsonline.utils.IdWorker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HogwartsArtifactsOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(HogwartsArtifactsOnlineApplication.class, args);
    }

    @Bean
    public IdWorker getIdWorker() {
        return new IdWorker(1,1);
    }
}
