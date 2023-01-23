package com.example.anime_downloader_bot;

import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TorrentClient {
    public static final String ADD_REQUEST_TEMPLATE = """
            {
               "arguments": {
                   "filename": "%s"},
               "method": "torrent-add",
               "tag": 39693
            }""";
    private final OkHttpClient client = new OkHttpClient();
    @Value("${transmission.url}")
    private String transmissionUrl;
    @Value("${transmission.login}")
    private String transmissionLogin;
    @Value("${transmission.password}")
    private String transmissionPassword;
    private String CSRFHeader = "invalid token";

    @SneakyThrows
    public String addTorrent(String torrentLink) {
        var json = String.format(ADD_REQUEST_TEMPLATE,torrentLink);
        Response response = client.newCall(buildRequest(json)).execute();

        if (response.code() == 409) {
            CSRFHeader = response.header("X-Transmission-Session-Id");
            response = client.newCall(buildRequest(json)).execute();
        }

        var responseString = response.body().string();
        response.body().close();
        return responseString;
    }

    private Request buildRequest(String json) {
        var body = RequestBody.create(json,
                MediaType.parse("application/json"));
        return new Request.Builder()
                .url(transmissionUrl + "/transmission/rpc")
                .addHeader("Authorization", Credentials.basic(transmissionLogin, transmissionPassword))
                .addHeader("X-Transmission-Session-Id", CSRFHeader)
                .post(body)
                .build();
    }
}
