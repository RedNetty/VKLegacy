package me.bpweber.practiceserver.player.Tutorial;

import me.bpweber.practiceserver.*;
import me.bpweber.practiceserver.player.GamePlayer.*;
import me.bpweber.practiceserver.profession.*;
import me.bpweber.practiceserver.teleport.*;
import me.bpweber.practiceserver.utils.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.*;

import java.util.*;

/**
 * Created by jaxon on 3/23/2017.
 */
public class TutorialMain implements Listener {


    public ArrayList<Player> inTutorial = new ArrayList<Player>();
    private GamePlayer gp;

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
    }


    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
            if (!e.getPlayer().hasPlayedBefore()) {
                Player p = e.getPlayer();
                inTutorial.add(p);
                Location l = new Location(Bukkit.getWorlds().get(0), -823.0, 47.0, -102.0);
                p.teleport(l);
                String prefix = ChatColor.GRAY + "[" + ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + "AR" + ChatColor.RESET.toString() + ChatColor.GRAY + "]";
                p.sendMessage(ChatColor.GRAY + "Island Greeter: " + ChatColor.YELLOW + "If you would like to skip the tutorial please use /skip!");
                p.sendMessage(ChatColor.GRAY + "Island Greeter: " + ChatColor.YELLOW + "Otherwise follow the path and learn the server!");
                p.sendMessage(ChatColor.GRAY + "Island Greeter: " + ChatColor.YELLOW + "If you would like a interactive tutorial watch the following video, WIP");
                nextNPCMsg(p, "Island Greeter", "Interface Guide", "the servers interfaces");
            }
        }


    public void nextNPCMsg(Player p, String npc, String nextnpc, String text) {
        StringUtil.sendCenteredMessage(p, ChatColor.GRAY + "===========================================");
        StringUtil.sendCenteredMessage(p, ChatColor.YELLOW + "Task 'talk to " + npc + " Completed");
        StringUtil.sendCenteredMessage(p, ChatColor.GOLD + "Now go talk to " + nextnpc + " to learn more about " + text + "!");
        StringUtil.sendCenteredMessage(p, ChatColor.GRAY + "===========================================");
    }

    @EventHandler
    public void onClickTuts(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof NPC) {
            NPC n = (NPC) e.getRightClicked();
            Player p = e.getPlayer();
            if (e.getRightClicked() == null) {
                return;
            }
            if (e.getRightClicked().getName().equalsIgnoreCase("")) {
                return;
            }
            if (n.getName().equalsIgnoreCase("Interface Guide")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        nextNPCMsg(p, "Interface Guide", "Equipment Master", "equipment");
                    }
                }.runTaskLaterAsynchronously(PracticeServer.plugin, 80);
            }
            if (n.getName().equalsIgnoreCase("Equipment Master")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        nextNPCMsg(p, "Equipment Master", "Master Miner", "how mining works!");
                    }
                }.runTaskLaterAsynchronously(PracticeServer.plugin, 80);
            }
            if (n.getName() == "Master Miner") {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Mining.ore(p, 1);
                    }
                }.runTaskLaterAsynchronously(PracticeServer.plugin, 80);
            }
            if (n.getName() == "Ship Captain") {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.teleport(TeleportBooks.Cyrennica);
                    }
                }.runTaskLaterAsynchronously(PracticeServer.plugin, 80);
            }
        }
    }


}
