package me.kayaba.guilds.exception;

public class AdditionalTaskException extends Exception {

    public AdditionalTaskException() {

    }


    public AdditionalTaskException(String message) {
        super(message);
    }


    public AdditionalTaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
