package com.foodfinder.container.exceptions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodfinder.container.exceptions.StashingException;
import com.foodfinder.diagnostic.repository.DiagnosticRepository;
import com.foodfinder.diagnostic.domain.entity.Diagnostic;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExceptionStasher {

    private final DiagnosticRepository diagnosticRepository;

    private static final String EXCEPTION_FILEPATH = "exception.json";
    private static final Integer MAX_STACK_TRACE_LENGTH = 200;

    public void stash(Exception exception, String comments) {
        if(exception == null || exception.getCause() == null) {
            return;
        }

        Diagnostic documentedException = Diagnostic.builder()
                .message(exception.getMessage() + ": " + exception.getCause().getMessage())
                .exception(getStacktrace(exception).substring(0, MAX_STACK_TRACE_LENGTH))
                .comments(comments)
                .date(new Date())
                .build();

        saveToDatabase(documentedException);
        saveToFile(exception);
    }

    private void saveToDatabase(Diagnostic documentedException) {
        if(documentedException == null) {
            return;
        }
        diagnosticRepository.save(documentedException);
    }

    private void saveToFile(Exception exception) {
        if(exception == null) {
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(new File(EXCEPTION_FILEPATH), exception);
        }
        catch (IOException newException) {
            throw new StashingException("Could not save file with exception", newException);
        }
    }

    private String getStacktrace(Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();

        for (StackTraceElement element : exception.getStackTrace()) {
            stringBuilder.append(element.toString());
        }

        return stringBuilder.toString();
    }
}
