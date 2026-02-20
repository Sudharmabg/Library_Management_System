package com.airtribe.library.patterns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class NotificationService implements Subject {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        logger.info("Notifying {} observers: {}", observers.size(), message);
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}
