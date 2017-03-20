package me.kayaba.guilds.impl.util.signgui;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.manager.*;

import java.util.*;

public class SignGUIPatternImpl implements SignGUI.SignGUIPattern {
    private final MessageWrapper message;
    private int inputLine;


    public SignGUIPatternImpl(MessageWrapper message) {
        this.message = message;
    }

    @Override
    public String[] get() {
        return fromList(message.getList());
    }

    @Override
    public int getInputLine() {
        return inputLine;
    }


    protected String[] fromList(List<String> list) {
        String[] lines = new String[4];

        List<String> rawList = MessageManager.getMessages().getStringList(message.getPath());
        for (int index = 0; index < rawList.size(); index++) {
            if (rawList.get(index).contains("{INPUT}")) {
                inputLine = index;
                break;
            }
        }

        for (int index = 0; index < list.size(); index++) {
            lines[index] = list.get(index);
        }

        return lines;
    }
}
