package com.example.anime_downloader_bot.telegram;

import com.example.anime_downloader_bot.domain.RequestProcessor;
import com.example.anime_downloader_bot.event.MessageEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.abilitybots.api.objects.Ability;
import org.telegram.abilitybots.api.objects.MessageContext;
import org.telegram.abilitybots.api.toggle.BareboneToggle;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.function.Consumer;

import static org.telegram.abilitybots.api.objects.Flag.MESSAGE;
import static org.telegram.abilitybots.api.objects.Locality.USER;
import static org.telegram.abilitybots.api.objects.Privacy.CREATOR;

@Service
public class TGBot extends AbilityBot implements ApplicationRunner {

    private RequestProcessor requestProcessor;
    private Consumer<MessageContext> contextConsumer;


    @Value("${telegram.bot.creatorId}")
    private long creatorId;

    public TGBot(@Value("${telegram.bot.token}") String botToken, @Value("${telegram.bot.username}") String botUsername, RequestProcessor requestProcessor) {
        super(botToken, botUsername, new BareboneToggle());
        contextConsumer = ctx -> requestProcessor.processRequest(ctx.update().getMessage().getText(), ctx.chatId());
    }

    @Override
    public long creatorId() {
        return creatorId;
    }

    public Ability addTorrent() {
        return Ability.builder()
                .name(DEFAULT)
                .flag(MESSAGE)
                .privacy(CREATOR)
                .locality(USER)
                .input(0)
                .action(contextConsumer)
                .build();
    }

    @EventListener
    public void sendTelegramMessage(MessageEvent messageEvent) {
        silent.send(messageEvent.getMessage(), messageEvent.getChatId());
    }

    @Override
    public void run(ApplicationArguments args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
