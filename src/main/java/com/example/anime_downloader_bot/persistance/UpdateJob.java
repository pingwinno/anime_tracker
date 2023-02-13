package com.example.anime_downloader_bot.persistance;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.Transient;

import static jakarta.persistence.GenerationType.TABLE;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class UpdateJob {
    @Id
    @GeneratedValue(strategy = TABLE)
    private Long id;
    private String animePage;
    private String pattern;
    private Integer torrentId;
    private Long chatId;
    @Transient
    private String transmissionResponse;
}
