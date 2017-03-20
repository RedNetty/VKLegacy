package me.kayaba.guilds.impl.util.exceptionparser;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.util.exceptionparser.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;

public class BlockImpl implements Block {
    private final String name;
    private final String message;
    private final String stackTraceElement;


    public BlockImpl(Throwable throwable) {

        this(throwable.getClass().getSimpleName(), throwable.getMessage(), throwable.getStackTrace()[0].toString());
    }


    public BlockImpl(String name, String message, String stackTraceElement) {
        LoggerUtils.debug(PracticeServer.class.getPackage().getName());
        if (StringUtils.startsWith(stackTraceElement, PracticeServer.class.getPackage().getName())) {
            StringUtils.replaceOnce(stackTraceElement, PracticeServer.class.getPackage().getName() + ".", "");
        }

        this.name = name;
        this.message = message;
        this.stackTraceElement = stackTraceElement;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getStackTraceElement() {
        return stackTraceElement;
    }
}
