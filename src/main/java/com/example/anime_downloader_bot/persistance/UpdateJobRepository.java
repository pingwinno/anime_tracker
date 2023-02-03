package com.example.anime_downloader_bot.persistance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpdateJobRepository extends JpaRepository<UpdateJob, String> {
    List<UpdateJob> getAll();
}
