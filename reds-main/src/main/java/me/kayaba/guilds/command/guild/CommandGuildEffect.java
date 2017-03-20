package me.kayaba.guilds.command.guild;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.command.abstractexecutor.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.basic.GroupImpl.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.potion.*;

import java.util.*;

public class CommandGuildEffect extends AbstractCommandExecutor {
    @Override
    public void execute(CommandSender sender, String[] args) throws Exception {
        GPlayer nPlayer = PlayerManager.getPlayer(sender);
        Player player = (Player) sender;

        if (!nPlayer.hasGuild()) {
            Message.CHAT_GUILD_NOTINGUILD.send(sender);
            return;
        }

        if (!nPlayer.hasPermission(GuildPermission.EFFECT)) {
            Message.CHAT_GUILD_NOGUILDPERM.send(sender);
            return;
        }


        double money = GroupManager.getGroup("default").get(Key.EFFECT_MONEY);
        if (!nPlayer.getGuild().hasMoney(money)) {
            Message.CHAT_GUILD_NOTENOUGHMONEY.send(sender);
            return;
        }


        List<PotionEffectType> potionEffects = plugin.getConfigManager().getGuildEffects();
        int index = NumberUtils.randInt(0, potionEffects.size() - 1);
        PotionEffectType effectType = potionEffects.get(index);
        PotionEffect effect = effectType.createEffect(Config.GUILD_EFFECT_DURATION.getSeconds() * 20, 1);


        for (Player gPlayer : nPlayer.getGuild().getOnlinePlayers()) {
            if (gPlayer.hasPotionEffect(effectType)) {
                gPlayer.removePotionEffect(effectType);
            }

            gPlayer.addPotionEffect(effect);
        }


        nPlayer.getGuild().takeMoney(money);

        Message.CHAT_GUILD_EFFECT_SUCCESS.clone().setVar(VarKey.EFFECTTYPE, effectType.getName()).send(sender);
    }
}
