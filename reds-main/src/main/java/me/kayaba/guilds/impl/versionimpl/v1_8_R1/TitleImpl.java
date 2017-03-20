package me.kayaba.guilds.impl.versionimpl.v1_8_R1;

import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.versionimpl.v1_8_R1.packet.*;
import me.kayaba.guilds.util.*;
import org.bukkit.entity.*;

public class TitleImpl extends AbstractTitle {
    public TitleImpl() {
        super("");
    }

    public TitleImpl(String title) {
        super(title);
    }

    public TitleImpl(String title, String subtitle) {
        super(title, subtitle);
    }

    public TitleImpl(Title title) {
        super(title);
    }

    public TitleImpl(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
        super(title, subtitle, fadeInTime, stayTime, fadeOutTime);
    }

    @Override
    public void send(Player player) {
        resetTitle(player);

        try {
            if (fadeInTime != -1 && fadeOutTime != -1 && stayTime != -1) {
                new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeInTime * (ticks ? 1 : 20), stayTime * (ticks ? 1 : 20), fadeOutTime * (ticks ? 1 : 20)).send(player);
            }

            String titleJson = "{text:\"" + StringUtils.fixColors(title) + "\",color:" + titleColor.name().toLowerCase() + "}";

            new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleJson).send(player);

            if (subtitle != null) {
                String subTitleJson = "{text:\"" + StringUtils.fixColors(subtitle) + "\",color:" + subtitleColor.name().toLowerCase() + "}";
                new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, subTitleJson).send(player);
            }
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }

    @Override
    public void clearTitle(Player player) {
        try {
            new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.CLEAR, null).send(player);
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }

    @Override
    public void resetTitle(Player player) {
        try {
            new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.RESET, null).send(player);
        } catch (Exception e) {
            LoggerUtils.exception(e);
        }
    }
}
