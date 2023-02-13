package com.example.anime_downloader_bot.domain;

import com.example.anime_downloader_bot.persistance.UpdateJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestProcessor {
    private final UpdateService updateService;

    public void processRequest(String message, long chatId) {
        log.info("Processing message {} from chat {}", message, chatId);
        var paramArray = message.split(",");
        var pattern = paramArray[1].trim();
        var animeLink = paramArray[0].trim();
        updateService.updateTorrent(animeLink, pattern, chatId);
    }
}
