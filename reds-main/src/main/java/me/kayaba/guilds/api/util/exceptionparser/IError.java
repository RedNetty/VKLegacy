package me.kayaba.guilds.api.util.exceptionparser;

import java.util.*;

public interface IError {

    ErrorSignature getSignature();


    Throwable getException();


    Collection<Throwable> getCauses();


    Collection<String> getConsoleOutput();
}
