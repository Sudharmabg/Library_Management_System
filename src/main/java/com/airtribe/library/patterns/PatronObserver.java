package com.airtribe.library.patterns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PatronObserver implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(PatronObserver.class);
    private final String patronId;
    private final String email;

    public PatronObserver(String patronId, String email) {
        this.patronId = patronId;
        this.email = email;
    }

    @Override
    public void update(String message) {
        logger.info("Notification sent to patron {} ({}): {}", patronId, email, message);
    }

    public String getPatronId() {
        return patronId;
    }
}
