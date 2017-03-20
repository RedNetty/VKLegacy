package me.kayaba.guilds.listener;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.impl.util.preparedtag.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;

import java.util.*;

public class DeathListener extends AbstractListener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player attacker = event.getEntity().getKiller();
        GPlayer nPlayer = PlayerManager.getPlayer(victim);


        if (nPlayer.isAtRegion()) {
            plugin.getRegionManager().playerExitedRegion(nPlayer.getPlayer());
        }

        if (attacker == null || attacker.equals(victim)) {
            return;
        }

        GPlayer nPlayerAttacker = PlayerManager.getPlayer(attacker);

        if (nPlayer.isPartRaid() && nPlayerAttacker.isPartRaid() && nPlayer.getPartRaid().equals(nPlayerAttacker.getPartRaid()) && !nPlayer.getGuild().isMember(nPlayerAttacker)) {
            Raid raid = nPlayer.getPartRaid();

            if (raid.getPlayersOccupying().contains(nPlayerAttacker)) {
                raid.addKillAttacker();
            } else {
                raid.addKillDefender();
            }
        }

        if (nPlayerAttacker.canGetKillPoints(victim)) {
            PreparedTag preparedTag1 = new PreparedTagChatImpl(nPlayer, false);
            PreparedTag preparedTag2 = new PreparedTagChatImpl(nPlayerAttacker, false);

            Map<VarKey, String> vars = new HashMap<>();
            vars.put(VarKey.PLAYER1, victim.getName());
            vars.put(VarKey.PLAYER2, attacker.getName());

            nPlayerAttacker.addKill();
            nPlayer.addDeath();


            if (nPlayer.hasGuild()) {
                Guild guildVictim = nPlayer.getGuild();
                guildVictim.takePoints(Config.GUILD_DEATHPOINTS.getInt());
            }

            if (nPlayerAttacker.hasGuild()) {
                Guild guildAttacker = nPlayerAttacker.getGuild();
                guildAttacker.addPoints(Config.GUILD_KILLPOINTS.getInt());
            }


            double bonusPercentMoney = 0;
            double bonusPercentPoints = 0;
            if (nPlayer.isPartRaid()) {
                bonusPercentMoney = Config.RAID_PVP_BONUSPERCENT_MONEY.getPercent();
                bonusPercentPoints = Config.RAID_PVP_BONUSPERCENT_POINTS.getPercent();
            }


            int points = (int) Math.round(nPlayer.getPoints() * (Config.KILLING_RANKPERCENT.getPercent() + bonusPercentPoints));
            nPlayer.takePoints(points);
            nPlayerAttacker.addPoints(points);
            nPlayerAttacker.addKillHistory(victim);


            vars.clear();
            vars.put(VarKey.PLAYER_NAME, victim.getName());
            double money;
            if (nPlayer.canGetKillPoints(attacker)) {
                money = NumberUtils.roundOffTo2DecPlaces((Config.KILLING_MONEYFORKILL.getPercent() + bonusPercentMoney) * nPlayer.getMoney());

                if (money > 0) {
                    vars.put(VarKey.MONEY, String.valueOf(money));
                    Message.CHAT_PLAYER_PVPMONEY_KILL.clone().vars(vars).send(attacker);
                }
            } else {
                money = NumberUtils.roundOffTo2DecPlaces((Config.KILLING_MONEYFORREVENGE.getPercent() + bonusPercentMoney) * nPlayer.getMoney());

                if (money > 0) {
                    vars.put(VarKey.MONEY, String.valueOf(money));
                    Message.CHAT_PLAYER_PVPMONEY_REVENGE.clone().vars(vars).send(attacker);
                }
            }

            if (money > 0) {
                nPlayer.takeMoney(money);
                nPlayerAttacker.addMoney(money);
            }
        }


        TabUtils.refresh(attacker);
        TabUtils.refresh(victim);
        TagUtils.refresh(attacker);
        TagUtils.refresh(victim);
    }
}
