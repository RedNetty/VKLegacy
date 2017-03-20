package me.kayaba.guilds.util;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.util.exceptionparser.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.exceptionparser.*;

import java.util.*;
import java.util.logging.*;

public final class LoggerUtils {
    private static final Logger logger = Logger.getLogger("Minecraft");
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private static final String logPrefix = "[AutismRealms]";

    private LoggerUtils() {

    }


    public static void error(String error, boolean classPrefix) {
        logger.severe(StringUtils.fixColors(logPrefix + (classPrefix ? classPrefix() : "") + space(error) + error));
    }


    public static void error(Collection<String> error, boolean classPrefix) {
        for (String string : error) {
            error(string, classPrefix);
        }
    }


    public static void error(String error) {
        error(error, true);
    }


    public static void info(String msg) {
        info(msg, true);
    }


    public static void info(String msg, boolean classPrefix) {
        if (msg == null) {
            msg = "null";
        }

        logger.info(StringUtils.fixColors(logPrefix + (classPrefix ? classPrefix() : "") + space(msg) + msg));
    }


    public static void debug(String msg) {
        debug(msg, true);
    }


    public static void debug(String msg, boolean classPrefix) {
        if (plugin != null && plugin.getConfigManager() != null) {
            if (Config.DEBUG.getBoolean()) {
                info("[DEBUG] " + (classPrefix ? classPrefix() : "") + msg, false);
            }
        }
    }


    private static String classPrefix() {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        String line = ste[4].toString();
        String[] split1 = org.apache.commons.lang.StringUtils.split(line, '(');
        String[] split2 = split1[1].split(":");
        String className = org.apache.commons.lang.StringUtils.replace(split2[0], ".java", "");
        return className.equals("KayabaGuilds") ? "" : "[" + className + "]";
    }


    private static String space(String string) {
        return string.contains("Manager]") ? "" : " ";
    }


    public static void exception(Throwable exception) {
        IError error = new ErrorImpl(exception);
        error(error.getConsoleOutput(), false);
        plugin.getErrorManager().addError(error);


        Message.CHAT_ERROROCCURED.broadcast(Permission.GUILDS_ERROR);
    }
}
