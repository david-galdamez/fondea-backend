package com.project.fondea.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
      super("El correo " + email + " ya esta en uso.");
    }
}
