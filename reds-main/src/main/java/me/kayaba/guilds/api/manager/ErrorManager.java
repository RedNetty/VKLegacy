package me.kayaba.guilds.api.manager;

import me.kayaba.guilds.api.util.exceptionparser.*;

import java.util.*;

public interface ErrorManager {

    List<IError> getErrors();


    void addError(IError error);


    void clearErrors();
}
