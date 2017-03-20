package me.kayaba.guilds.exception;

public class MissingDependencyException extends Exception {

    public MissingDependencyException() {

    }


    public MissingDependencyException(String message) {
        super(message);
    }


    public MissingDependencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
