package com.journal_service.controller;

import com.journal_service.entity.Journal;
import com.journal_service.repository.JournalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/journals")
public class JournalController {

    @Autowired
    private JournalRepository journalRepository;

    @GetMapping
    public List<Journal> getAllJournals() {
        return journalRepository.findAll();
    }
}