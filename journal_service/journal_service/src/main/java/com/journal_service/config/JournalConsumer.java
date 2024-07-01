package com.journal_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class JournalConsumer {

    private static final Logger logger = LoggerFactory.getLogger(JournalConsumer.class);

    @Autowired
    private JournalRepository journalRepository;

    @KafkaListener(topics = "user-events", groupId = "journal-service")
    public void consumeEvent(String event) {
        logger.info("Consumed event: {}", event);

        // Log the event to database
        Journal journal = new Journal();
        journal.setMessage(event);
        journalRepository.save(journal);
    }
}
