package me.kayaba.guilds.impl.util.exceptionparser;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.util.exceptionparser.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.*;

import java.util.*;

public class ErrorImpl implements IError {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final Collection<String> consoleOutput = new ArrayList<>();
    private final Collection<Throwable> causes = new LinkedHashSet<>();
    private final Throwable exception;
    private ErrorSignature signature;


    public ErrorImpl(Throwable exception) {
        this.exception = exception;

        Throwable cause = exception.getCause();
        while (cause != null) {
            causes.add(cause);
            cause = cause.getCause();
        }
    }


    protected void generateSignature() {
        signature = new ErrorSignatureImpl(this);
    }

    @Override
    public ErrorSignature getSignature() {
        if (signature == null) {
            generateSignature();
        }

        return signature;
    }

    @Override
    public Throwable getException() {
        return exception;
    }

    @Override
    public Collection<Throwable> getCauses() {
        return causes;
    }

    @Override
    public Collection<String> getConsoleOutput() {
        if (consoleOutput.isEmpty()) {
            generateConsoleOutput();
        }

        return consoleOutput;
    }


    protected void generateConsoleOutput() {
        consoleOutput.add("");
        consoleOutput.add("Severe error: " + exception.getClass().getSimpleName());
        consoleOutput.add(" Please send this whole message to Kayaba");
        consoleOutput.add("");
        consoleOutput.add("Server Information:");
        consoleOutput.add(" Storage Type: " + (plugin.getConfigManager() == null || plugin.getConfigManager().getDataStorageType() == null ? "null" : plugin.getConfigManager().getDataStorageType().name()));
        consoleOutput.add(" Bukkit: " + Bukkit.getBukkitVersion());
        consoleOutput.add(" Version Implementation: " + ConfigManager.getServerVersion().name());
        consoleOutput.add(" Java: " + System.getProperty("java.version"));
        consoleOutput.add(" Thread: " + Thread.currentThread());
        consoleOutput.add(" Running CraftBukkit: " + Bukkit.getServer().getClass().getSimpleName().equals("CraftServer"));
        consoleOutput.add("");
        consoleOutput.add("Exception Message: ");
        consoleOutput.add(" " + exception.getMessage());

        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            consoleOutput.add("  at " + stackTraceElement.toString());
        }

        consoleOutput.add("");

        for (Throwable cause : causes) {
            consoleOutput.add("Caused by: " + cause.getClass().getName());
            consoleOutput.add(" " + cause.getMessage());

            for (StackTraceElement stackTraceElement : cause.getStackTrace()) {
                consoleOutput.add("  at " + stackTraceElement.toString());
            }

            consoleOutput.add("");
        }

        consoleOutput.add("End of Error.");
        consoleOutput.add("");
    }
}
