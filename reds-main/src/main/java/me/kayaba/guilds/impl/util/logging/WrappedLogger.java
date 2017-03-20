package me.kayaba.guilds.impl.util.logging;

import me.kayaba.guilds.util.*;
import org.bukkit.plugin.*;

import java.util.logging.*;

public class WrappedLogger extends PluginLogger {

    public WrappedLogger(Plugin context) {
        super(context);
    }

    @Override
    public void log(Level level, String msg, Throwable thrown) {
        if (level == Level.WARNING) {
            StackTraceElement[] ste = Thread.currentThread().getStackTrace();
            if (ste[2].getClassName().endsWith("CraftScheduler")) {
                LoggerUtils.exception(thrown);
            }
        } else {
            super.log(level, msg, thrown);
        }
    }
}
