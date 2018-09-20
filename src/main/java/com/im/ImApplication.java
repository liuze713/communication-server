package com.im;

import com.im.imTio.ShowcaseWebsocketStarter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ImApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ImApplication.class, args);
        ShowcaseWebsocketStarter.start();
    }
}
