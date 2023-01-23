package com.example.anime_downloader_bot;

import lombok.SneakyThrows;

import java.io.IOException;

public interface SiteParser {

    String getTorrentLink(String pageLink, String filterPattern) throws Exception;
}
