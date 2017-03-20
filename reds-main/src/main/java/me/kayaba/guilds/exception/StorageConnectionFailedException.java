package me.kayaba.guilds.exception;

public class StorageConnectionFailedException extends Exception {

    public StorageConnectionFailedException() {

    }


    public StorageConnectionFailedException(String message) {
        super(message);
    }


    public StorageConnectionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
