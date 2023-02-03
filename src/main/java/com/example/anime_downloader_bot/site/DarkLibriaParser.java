package com.example.anime_downloader_bot.site;

import com.example.anime_downloader_bot.site.SiteParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.NoSuchElementException;

@Slf4j
@Component
public class DarkLibriaParser implements SiteParser {

    @Override
    public String getTorrentLink(String pageLink, String filterPattern) throws IOException {
        Document doc = Jsoup.connect(pageLink).get();

        var elements = doc.select("a[href^=magnet]").stream()
                .map(element -> element.attr("href"))
                .toList();
        var magnetLink = elements.stream()
                .filter(magnet -> magnet.contains(filterPattern))
                .reduce((first, second) -> second);
        return magnetLink.orElseThrow(() -> new NoSuchElementException("Can't find suitable link with pattern: " + filterPattern));
    }
}
