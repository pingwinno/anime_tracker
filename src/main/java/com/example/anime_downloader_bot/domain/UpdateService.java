package com.example.anime_downloader_bot.domain;

import com.example.anime_downloader_bot.event.MessageEvent;
import com.example.anime_downloader_bot.persistance.UpdateJob;
import com.example.anime_downloader_bot.persistance.UpdateJobRepository;
import com.example.anime_downloader_bot.site.SiteParser;
import com.example.anime_downloader_bot.transmission.TorrentClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateService {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TorrentClient torrentClient;
    private final SiteParser darkLibriaParser;

    private final UpdateJobRepository updateJobRepository;

    public void updateTorrent(String animePage, String pattern, long chatId) {
        UpdateJob updateJob = null;
        try {
            var torrentLink = darkLibriaParser.getTorrentLink(animePage, pattern);
            var transmissionResponse = torrentClient.addTorrent(torrentLink);
            var responseTree = objectMapper.readTree(transmissionResponse).get("arguments");
            if (responseTree.has("torrent-added")) {
                var torrentId = responseTree.get("torrent-added").get("id").asInt();
                updateJob = UpdateJob.builder()
                        .pattern(pattern)
                        .animePage(animePage)
                        .torrentId(torrentId)
                        .transmissionResponse(transmissionResponse)
                        .chatId(chatId)
                        .build();
                updateJobRepository.save(updateJob);
            }
            sendUpdateMessage(transmissionResponse, chatId);
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            sendUpdateMessage(e.toString(), chatId);
        }
    }

    private void sendUpdateMessage(String message, Long chatId) {
        applicationEventPublisher.publishEvent(new MessageEvent(message, chatId));
    }

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1)
    public void updateTorrents() {
        log.info("Updating torrents...");
        updateJobRepository.findAll().forEach(updateJob -> updateTorrent(updateJob.getAnimePage(), updateJob.getPattern(), updateJob.getChatId()));
    }
}
