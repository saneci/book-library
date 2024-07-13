package ru.saneci.booklibrary.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

public final class BindingResultLogger {

    private final Logger log;

    private BindingResultLogger(Class<?> clazz) {
        log = LoggerFactory.getLogger(clazz);
    }

    public static BindingResultLogger getLogger(Class<?> clazz) {
        return new BindingResultLogger(clazz);
    }

    public void warn(String operation, BindingResult bindingResult) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            log.warn("{}: validation error: {}", operation, error.getDefaultMessage());
        }
        log.debug("{}: finish processing", operation);
    }
}
