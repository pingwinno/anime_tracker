package com.example.anime_downloader_bot;

import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class TorrentClient {
    private final OkHttpClient client = new OkHttpClient();
    private String CSRFHeader = "invalid token";
    @SneakyThrows
    public String addTorrent() {
        var json = "{\n" +
                "   \"arguments\": {\n" +
                "       \"filename\": \"https://tv3.darklibria.it/upload/torrents/22222.torrent\"" +
                "   },\n" +
                "   \"method\": \"torrent-add\",\n" +
                "   \"tag\": 39693\n" +
                "}";
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);

        Request request = new Request.Builder()
                .url("http://192.168.50.100:9091/transmission/rpc")
                .addHeader("Authorization", Credentials.basic("transmission", "4b06a499ef180c1ed352f7yOSpysqi"))
                .addHeader("X-Transmission-Session-Id", CSRFHeader)
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        var responseString = response.body().string();
        if (response.code() == 409){
          CSRFHeader = response.header("X-Transmission-Session-Id");
            System.out.println("CSRF is " + CSRFHeader);
        }

        System.out.println(responseString);
        return responseString;
    }
}
