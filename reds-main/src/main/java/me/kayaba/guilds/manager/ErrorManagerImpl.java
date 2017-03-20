package me.kayaba.guilds.manager;

import me.kayaba.guilds.api.manager.*;
import me.kayaba.guilds.api.util.exceptionparser.*;

import java.util.*;

public class ErrorManagerImpl implements ErrorManager {
    private final List<IError> errorCollection = new ArrayList<>();

    @Override
    public List<IError> getErrors() {
        return errorCollection;
    }

    @Override
    public void addError(IError error) {
        errorCollection.add(error);
    }

    @Override
    public void clearErrors() {
        errorCollection.clear();
    }
}
