package com.example.anime_downloader_bot.domain;

import com.example.anime_downloader_bot.persistance.UpdateJob;
import com.example.anime_downloader_bot.persistance.UpdateJobRepository;
import com.example.anime_downloader_bot.site.SiteParser;
import com.example.anime_downloader_bot.transmission.TorrentClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UpdateService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TorrentClient torrentClient;
    private final SiteParser darkLibriaParser;

    private final UpdateJobRepository updateJobRepository;

    public UpdateJob updateTorrent(String animePage, String pattern) {
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
                        .build();
                updateJobRepository.save(updateJob);
            }
        } catch (Exception e) {
            log.error(e.toString());
        }
        return updateJob;
    }

    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1)
    public void updateTorrents() {
        updateJobRepository.getAll().forEach(updateJob -> updateTorrent(updateJob.getAnimePage(), updateJob.getPattern()));
    }
}
