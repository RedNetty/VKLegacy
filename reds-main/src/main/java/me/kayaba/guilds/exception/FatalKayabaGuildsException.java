package me.kayaba.guilds.exception;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.manager.*;

public class FatalKayabaGuildsException extends Exception {
    public static boolean fatal;


    public FatalKayabaGuildsException() {
        disable();
    }


    public FatalKayabaGuildsException(String message) {
        super(message);
        disable();
    }


    public FatalKayabaGuildsException(String message, Throwable cause) {
        super(message, cause);
        disable();
    }


    private void disable() {
        fatal = true;

        if (PracticeServer.getInstance().isEnabled()) {
            PracticeServer.runTask(new Runnable() {
                @Override
                public void run() {
                    ListenerManager.getLoggedPluginManager().disablePlugin(PracticeServer.getInstance());
                }
            });
        }
    }
}
