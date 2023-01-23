package com.example.anime_downloader_bot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestProcessor {
    private final TorrentClient torrentClient;
    private final SiteParser darkLibriaParser;

    public String processRequest(Update updates) {
        System.out.println(updates.getMessage());
        String transmissionResponse;
        var paramArray = updates.getMessage().getText().split(",");
        try {
            var torrentLink = darkLibriaParser.getTorrentLink(paramArray[0].trim(), paramArray[1].trim());
            transmissionResponse = torrentClient.addTorrent(torrentLink);
        } catch (Exception e) {
            log.error(e.toString());
            return e.getMessage();
        }
        return transmissionResponse;
    }
}
