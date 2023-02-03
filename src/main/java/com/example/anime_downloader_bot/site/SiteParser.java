package com.example.anime_downloader_bot.site;

public interface SiteParser {

    String getTorrentLink(String pageLink, String filterPattern) throws Exception;
}
