package com.project.fondea.exception;

import java.util.UUID;

public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, UUID id) {
        super(entityName + " not found with id: " + id);
    }
}
