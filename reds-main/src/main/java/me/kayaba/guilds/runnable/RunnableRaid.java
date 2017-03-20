package me.kayaba.guilds.runnable;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.event.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.bossbar.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.bukkit.entity.*;

import java.util.*;
import java.util.concurrent.*;

public class RunnableRaid implements Runnable {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final UUID taskUUID = UUID.randomUUID();
    private static UUID scheduledUUID;

    @Override
    public void run() {
        boolean renewTask = false;

        for (Guild guildDefender : new ArrayList<>(plugin.getGuildManager().getGuilds())) {
            if (!guildDefender.isRaid()) {
                continue;
            }

            Raid raid = guildDefender.getRaid();

            LoggerUtils.debug(guildDefender.getName() + " raid scheduler working " + raid.getProgress());

            if (!raid.getPlayersOccupying().isEmpty()) {

                raid.addProgress((float) (raid.getPlayersOccupying().size() * Config.RAID_MULTIPLER.getDouble()));


                raid.updateInactiveTime();
            } else {
                raid.resetProgress();
            }


            Map<VarKey, String> vars = new HashMap<>();
            vars.put(VarKey.ATTACKER, raid.getGuildAttacker().getName());
            vars.put(VarKey.DEFENDER, guildDefender.getName());

            if (NumberUtils.systemSeconds() - raid.getInactiveTime() > Config.RAID_TIMEINACTIVE.getSeconds()) {
                raid.setResult(Raid.Result.TIMEOUT);
            }

            if (raid.isProgressFinished()) {
                if (guildDefender.getLives() > 1) {
                    raid.setResult(Raid.Result.SUCCESS);
                } else {
                    raid.setResult(Raid.Result.DESTROYED);
                }
            }


            if (raid.getResult() != Raid.Result.DURING) {
                int pointsTake = Config.RAID_POINTSTAKE.getInt();

                switch (raid.getResult()) {
                    case DESTROYED:
                        raid.getGuildAttacker().addPoints(pointsTake);

                        GuildAbandonEvent guildAbandonEvent = new GuildAbandonEvent(guildDefender, AbandonCause.RAID);
                        plugin.getServer().getPluginManager().callEvent(guildAbandonEvent);

                        if (!guildAbandonEvent.isCancelled()) {
                            vars.put(VarKey.GUILD_NAME, guildDefender.getName());
                            Message.BROADCAST_GUILD_DESTROYED.clone().vars(vars).broadcast();
                            plugin.getGuildManager().delete(guildAbandonEvent);
                        }
                        break;
                    case SUCCESS:
                        Message.BROADCAST_GUILD_RAID_FINISHED_ATTACKERWON.clone().vars(vars).broadcast();
                        guildDefender.takeLive();
                        guildDefender.updateTimeRest();
                        guildDefender.updateLostLive();
                        guildDefender.takePoints(pointsTake);
                        guildDefender.addPoints(pointsTake);
                        break;
                    case TIMEOUT:
                        Message.BROADCAST_GUILD_RAID_FINISHED_DEFENDERWON.clone().vars(vars).broadcast();
                        break;
                }
            } else if (!renewTask) {
                renewTask = true;
            }

            raidBar(raid);
        }

        if (renewTask && plugin.isEnabled()) {
            schedule();
        } else {
            scheduledUUID = null;
        }
    }


    private void raidBar(Raid raid) {
        if (raid.getResult() != Raid.Result.DURING) {
            raid.getGuildAttacker().removeRaidBar();
            raid.getGuildDefender().removeRaidBar();
        } else {
            List<Player> players = raid.getGuildAttacker().getOnlinePlayers();
            players.addAll(raid.getGuildDefender().getOnlinePlayers());

            for (Player player : players) {
                if (Config.BOSSBAR_ENABLED.getBoolean()) {
                    BossBarUtils.setMessage(player, Message.BARAPI_WARPROGRESS.clone().setVar(VarKey.DEFENDER, raid.getGuildDefender().getName()).get(), raid.getProgress());
                } else {

                    if (raid.getProgress() == 0 || raid.getProgress() % 10 == 0 || raid.getProgress() >= 90) {
                        String lines;
                        if (raid.getProgress() == 0) {
                            lines = "&f";
                        } else {
                            lines = "&4";
                        }

                        for (int i = 1; i <= 100; i++) {
                            lines += "|";
                            if (i == raid.getProgress()) {
                                lines += "&f";
                            }
                        }

                        MessageManager.sendPrefixMessage(player, lines);
                    }
                }
            }
        }
    }


    public static boolean isRaidRunnableRunning() {
        return scheduledUUID != null;
    }


    public void schedule() {
        if (scheduledUUID == null) {
            scheduledUUID = taskUUID;
        }

        if (!scheduledUUID.equals(taskUUID)) {
            return;
        }

        PracticeServer.runTaskLater(this, 1, TimeUnit.SECONDS);
    }
}
