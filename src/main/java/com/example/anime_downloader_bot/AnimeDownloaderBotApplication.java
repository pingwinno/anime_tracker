package com.example.anime_downloader_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AnimeDownloaderBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimeDownloaderBotApplication.class, args);
    }

}
