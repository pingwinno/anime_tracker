package com.example.anime_downloader_bot.event;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {

    private String message;
    private long chatId;

    public MessageEvent(String message, long chatId) {
        super(message);
        this.message = message;
        this.chatId = chatId;
    }


    public String getMessage() {
        return message;
    }

    public long getChatId() {
        return chatId;
    }
}
