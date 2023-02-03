package com.example.anime_downloader_bot.telegram;

import com.example.anime_downloader_bot.domain.RequestProcessor;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
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
public class TGBot extends AbilityBot {
    private final RequestProcessor requestProcessor;

    Consumer<MessageContext> contextConsumer;


    @Value("${telegram.bot.creatorId}")
    private long creatorId;

    public TGBot(@Value("${telegram.bot.token}") String botToken, @Value("${telegram.bot.username}") String botUsername, RequestProcessor requestProcessor) {
        super(botToken, botUsername, new BareboneToggle());
        this.requestProcessor = requestProcessor;
        contextConsumer = ctx -> silent.send(requestProcessor.processRequest(ctx.update().getMessage().getText()).getTransmissionResponse(), ctx.chatId());
    }

    @PostConstruct
    void initBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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
}
