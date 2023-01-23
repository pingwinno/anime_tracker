package com.example.anime_downloader_bot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class UpdateProcessor {
    private final TorrentClient torrentClient;

    public String processUpdates(Update updates){
        System.out.println(updates.getMessage());
        return torrentClient.addTorrent();
    }
}
