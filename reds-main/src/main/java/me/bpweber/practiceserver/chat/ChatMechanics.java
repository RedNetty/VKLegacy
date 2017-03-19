/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.v1_7_R4.ChatSerializer
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.IChatBaseComponent
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutChat
 *  net.minecraft.server.v1_7_R4.PlayerConnection
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.AsyncPlayerChatEvent
 *  org.bukkit.event.player.PlayerChatTabCompleteEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package me.bpweber.practiceserver.chat;

import me.bpweber.practiceserver.ModerationMechanics.Commands.Mute;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Setrank;
import me.bpweber.practiceserver.ModerationMechanics.Commands.Vanish;
import me.bpweber.practiceserver.ModerationMechanics.ModerationMechanics;
import me.bpweber.practiceserver.PracticeServer;
import me.bpweber.practiceserver.utils.JSONMessage;
import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class ChatMechanics
        implements Listener,
        CommandExecutor {
    private static HashMap<Player, Player> reply = new HashMap<Player, Player>();
    public static List<String> bad_words;

    static {
        bad_words = new ArrayList<String>(Arrays.asList("shit", "fuck", "cunt", "bitch", "whore", "slut", "wank", "asshole", "cock", "dick", "clit", "homo", "fag", "queer", "nigger", "dike", "dyke", "retard", "motherfucker", "vagina", "boob", "pussy", "rape", "gay", "penis", "cunt", "titty", "anus", "faggot", "xFinity", "destiny"));
    }
    public void onEnable() {
        PracticeServer.log.info("[ChatMechanics] has been enabled.");
        Bukkit.getServer().getPluginManager().registerEvents(this, PracticeServer.plugin);
    }

    public void onDisable() {
        PracticeServer.log.info("[ChatMechanics] has been disabled.");
    }

    public static String censorMessage(String msg) {
        String personal_msg = "";
        if (msg == null) {
            return "";
        }
        if (!msg.contains(" ")) {
            msg = String.valueOf(msg) + " ";
        }
        String[] split;
        for (int length = (split = msg.split(" ")).length, i = 0; i < length; ++i) {
            String s = split[i];
            for (final String bad : bad_words) {
                if (s.toLowerCase().contains(bad.toLowerCase())) {
                    int letters = bad.length();
                    String replace_char = "";
                    while (letters > 0) {
                        replace_char = String.valueOf(replace_char) + "*";
                        --letters;
                    }
                    int censor_start = 0;
                    int censor_end = 1;
                    censor_start = s.toLowerCase().indexOf(bad);
                    censor_end = censor_start + bad.length();
                    final String real_bad_word = s.substring(censor_start, censor_end);
                    s = s.replaceAll(real_bad_word, replace_char);
                }
            }
            personal_msg = String.valueOf(personal_msg) + s + " ";
        }
        if (personal_msg.endsWith(" ")) {
            personal_msg = personal_msg.substring(0, personal_msg.lastIndexOf(" "));
        }
        return personal_msg;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            int hours2;
            int n;
            String[] arrstring;
            Player p = (Player) sender;
            if (cmd.getName().equalsIgnoreCase("gl")) {
                if (Mute.muted.containsKey(p.getName().toLowerCase())) {
                    int seconds = Mute.muted.get(p.getName().toLowerCase());
                    int minutes = 0;
                    hours2 = 0;
                    minutes = seconds / 60;
                    hours2 = minutes / 60;
                    if (hours2 >= 1) {
                        p.sendMessage(ChatColor.RED + "You are currently " + ChatColor.BOLD + "GLOBALLY MUTED" + ChatColor.RED + ". You will be unmuted in " + hours2 + " hours(s).");
                        return false;
                    }
                    if (minutes >= 1) {
                        p.sendMessage(ChatColor.RED + "You are currently " + ChatColor.BOLD + "GLOBALLY MUTED" + ChatColor.RED + ". You will be unmuted in " + minutes + " minute(s).");
                        return false;
                    }
                    p.sendMessage(ChatColor.RED + "You are currently " + ChatColor.BOLD + "GLOBALLY MUTED" + ChatColor.RED + ". You will be unmuted in " + seconds + " seconds(s).");
                } else if (args.length == 0) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Incorrect Syntax. You must supply a message!" + ChatColor.RED + " /gl <MESSAGE>");
                } else {
                    String message = "";
                    arrstring = args;
                    n = arrstring.length;
                    int hours21 = 0;
                    while (hours21 < n) {
                        String s = arrstring[hours21];
                        message = String.valueOf(message) + s + " ";
                        ++hours21;
                    }
                    if (message.toLowerCase().startsWith("wtb") || message.toLowerCase().startsWith("wts") || message.toLowerCase().startsWith("wtt") || message.toLowerCase().startsWith("buying") || message.toLowerCase().startsWith("selling") || message.toLowerCase().startsWith("trading")) {
                        if (message.contains("@i@") && p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                                this.sendShowString(p, p.getInventory().getItemInMainHand(), ChatColor.GREEN + "<" + ChatColor.BOLD + "T" + ChatColor.GREEN + "> " + ChatColor.RESET, message, pl);
                            }
                        } else {
                            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                                pl.sendMessage(ChatColor.GREEN + "<" + ChatColor.BOLD + "T" + ChatColor.GREEN + "> " + ChatColor.RESET + p.getDisplayName() + ": " + ChatColor.WHITE + message);
                            }
                        }
                        PracticeServer.log.info(ChatColor.GREEN + "<" + ChatColor.BOLD + "T" + ChatColor.GREEN + "> " + ChatColor.RESET + p.getDisplayName() + ": " + ChatColor.WHITE + message);
                    } else {
                        if (message.contains("@i@") && p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                                this.sendShowString(p, p.getInventory().getItemInMainHand(), ChatColor.AQUA + "<" + ChatColor.BOLD + "G" + ChatColor.AQUA + "> " + ChatColor.RESET, message, pl);
                            }
                        } else {
                            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                                pl.sendMessage(ChatColor.AQUA + "<" + ChatColor.BOLD + "G" + ChatColor.AQUA + "> " + ChatColor.RESET + p.getDisplayName() + ": " + ChatColor.WHITE + message);
                            }
                        }
                        PracticeServer.log.info(ChatColor.AQUA + "<" + ChatColor.BOLD + "G" + ChatColor.AQUA + "> " + ChatColor.RESET + p.getDisplayName() + ": " + ChatColor.WHITE + message);
                    }
                }
            }
            if (cmd.getName().equalsIgnoreCase("br") && p.isOp()) {
                if (args.length == 0) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Incorrect Syntax. You must supply a message!" + ChatColor.RED + " /br <MESSAGE>");
                } else {
                    String message = "";
                    arrstring = args;
                    n = arrstring.length;
                    hours2 = 0;
                    while (hours2 < n) {
                        String s = arrstring[hours2];
                        message = String.valueOf(message) + s + " ";
                        ++hours2;
                    }
                    for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                        pl.sendMessage(ChatColor.AQUA.toString() + ChatColor.BOLD + ">> " + ChatColor.AQUA + message);
                    }
                    PracticeServer.log.info(ChatColor.AQUA.toString() + ChatColor.BOLD + ">> " + ChatColor.AQUA + message);
                }
            }
            if (cmd.getName().equalsIgnoreCase("message")) {
                if (args.length == 1) {
                    Player sendee = Bukkit.getServer().getPlayer(args[0]);
                    if (sendee == null || sendee.isOp() && !p.isOp()) {
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + args[0] + ChatColor.RED + " is OFFLINE.");
                    } else {
                        sendee.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "FROM " + p.getDisplayName() + ": " + ChatColor.WHITE + "/" + label + " " + args[0]);
                        p.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "TO " + sendee.getDisplayName() + ": " + ChatColor.WHITE + "/" + label + " " + args[0]);
                        sendee.playSound(sendee.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
                        reply.put(sendee, p);
                    }
                } else if (args.length >= 2) {
                    Player sendee = Bukkit.getServer().getPlayer(args[0]);
                    if (sendee == null || Vanish.vanished.contains(sendee.getName().toLowerCase()) && !p.isOp()) {
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + args[0] + ChatColor.RED + " is OFFLINE.");
                    } else {
                        String message = "";
                        int i = 1;
                        while (i < args.length) {
                            message = String.valueOf(message) + args[i] + " ";
                            ++i;
                        }
                        if (message.contains("@i@") && p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            this.sendShowString(p, p.getInventory().getItemInMainHand(), ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "FROM ", message, sendee);
                            this.sendShowString(sendee, p.getInventory().getItemInMainHand(), ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "TO ", message, p);
                        } else {
                            sendee.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "FROM " + p.getDisplayName() + ": " + ChatColor.WHITE + message);
                            p.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "TO " + sendee.getDisplayName() + ": " + ChatColor.WHITE + message);
                        }
                        sendee.playSound(sendee.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
                        reply.put(sendee, p);
                    }
                } else {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Incorrect syntax. " + "/" + label + " <PLAYER> <MESSAGE>");
                }
            }
            if (cmd.getName().equalsIgnoreCase("reply")) {
                if (reply.containsKey(p)) {
                    Player sendee = reply.get(p);
                    if (sendee == null || Vanish.vanished.contains(sendee.getName().toLowerCase()) && !p.isOp()) {
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + reply.get(p).getName() + ChatColor.RED + " is OFFLINE.");
                    } else if (args.length == 0) {
                        sendee.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "FROM " + p.getDisplayName() + ": " + ChatColor.WHITE + "/" + label + " " + sendee.getName());
                        p.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "TO " + sendee.getDisplayName() + ": " + ChatColor.WHITE + "/" + label + " " + sendee.getName());
                        sendee.playSound(sendee.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
                        reply.put(sendee, p);
                    } else if (args.length >= 1) {
                        String message = "";
                        String[] iterator = args;
                        int n2 = iterator.length;
                        n = 0;
                        while (n < n2) {
                            String s = iterator[n];
                            message = String.valueOf(message) + s + " ";
                            ++n;
                        }
                        if (message.contains("@i@") && p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            this.sendShowString(p, p.getInventory().getItemInMainHand(), ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "FROM ", message, sendee);
                            this.sendShowString(sendee, p.getInventory().getItemInMainHand(), ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "TO ", message, p);
                        } else {
                            sendee.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "FROM " + p.getDisplayName() + ": " + ChatColor.WHITE + message);
                            p.sendMessage(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "TO " + sendee.getDisplayName() + ": " + ChatColor.WHITE + message);
                            sendee.playSound(sendee.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);
                            reply.put(sendee, p);
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "ERROR: " + ChatColor.RED + "You have no conversation to respond to!");
                }
            }

            if (cmd.getName().equalsIgnoreCase("roll")) {
                if (args.length < 1 || args.length > 1) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Incorrect Syntax." + ChatColor.GRAY + " /roll <1 - 10000>");
                } else if (args.length == 1) {
                    int max = 0;
                    try {
                        max = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Non-Numeric Max Number. /roll <1 - 10000>");
                        return true;
                    }
                    if (max < 1 || max > 10000) {
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "Incorrect Syntax." + ChatColor.GRAY + " /roll <1 - 10000>");
                    } else {
                        Random random = new Random();
                        int roll = random.nextInt(max + 1);
                        p.sendMessage(String.valueOf(p.getDisplayName()) + ChatColor.GRAY + " has rolled a " + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + roll + ChatColor.GRAY + " out of " + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + max + ".");
                        ArrayList<Player> to_send = new ArrayList<Player>();
                        for (Player pl2 : Bukkit.getServer().getOnlinePlayers()) {
                            if (pl2 == null || pl2 == p || pl2.getLocation().distance(p.getLocation()) >= 50.0)
                                continue;
                            to_send.add(pl2);
                        }
                        if (to_send.size() > 0) {
                            for (Player pl2 : to_send) {
                                pl2.sendMessage(String.valueOf(p.getDisplayName()) + ChatColor.GRAY + " has rolled a " + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + roll + ChatColor.GRAY + " out of " + ChatColor.GRAY + ChatColor.BOLD + ChatColor.UNDERLINE + max + ".");
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChat(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String s = e.getMessage().toLowerCase();
        if (s.startsWith("/")) {
            s = s.replace("/", "");
        }
        if (s.contains(" ")) {
            s = s.split(" ")[0];
        }
        if (s.equals("i") || s.equals("give")) {
            e.setCancelled(true);
            if (p.isOp()) {
                p.getInventory().addItem(new ItemStack(Material.MOB_SPAWNER));
            }
            p.sendMessage(ChatColor.WHITE + "Unknown command. View your Character Journal's Index for a list of commands.");
            return;
        }
        if (s.equals("save-all") || s.equalsIgnoreCase("more") || s.equalsIgnoreCase("stack") || s.equals("stop") || s.equals("restart") || s.equals("reload") || s.equals("ban") || s.equals("tpall") || s.equals("kill") || s.equals("vanish") || s.equals("mute") || s.equals("more")) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.WHITE + "Unknown command. View your Character Journal's Index for a list of commands.");
            return;
        }
        if (!p.isOp()) {
            String rank = "";
            if (Setrank.ranks.containsKey(p.getName())) {
                rank = Setrank.ranks.get(p.getName());
            }
            if (rank.equals("pmod")) {
                if (!(s.equals("roll") || s.equals("sc") ||  s.equals("gl") || s.equals("toggle") || s.equals("toggles") || s.equals("togglepvp") || s.equals("togglechaos") || s.equals("toggledebug") || s.equals("debug") || s.equals("toggleff") || s.equals("add") || s.equals("del") || s.equals("delete") || s.equals("message") || s.equals("msg") || s.equals("m") || s.equals("whisper") || s.equals("w") || s.equals("tell") || s.equals("t") || s.equals("reply") || s.equals("r") || s.equals("logout") || s.equals("sync") || s.equals("reboot") || s.equals("pinvite") || s.equals("paccept") || s.equals("pquit") || s.equals("pkick") || s.equals("pdecline") || s.equals("p") || s.equals("psban") || s.equals("psunban") || s.equals("psmute") || s.equals("psunmute"))) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.WHITE + "Unknown command. View your Character Journal's Index for a list of commands.");
                }
            } else if (ModerationMechanics.isSub(p)) {
                if (!(s.equals("roll") || s.equals("gl") || s.equals("toggle") || s.equals("toggles") || s.equals("togglepvp") || s.equals("togglechaos") || s.equals("toggledebug") || s.equals("debug") || s.equals("toggleff") || s.equals("add") || s.equals("del") || s.equals("delete") || s.equals("message") || s.equals("msg") || s.equals("m") || s.equals("whisper") || s.equals("w") || s.equals("tell") || s.equals("t") || s.equals("reply") || s.equals("r") || s.equals("logout") || s.equals("sync") || s.equals("reboot") || s.equals("pinvite") || s.equals("paccept") || s.equals("pquit") || s.equals("pkick") || s.equals("pdecline") || s.equals("p") || s.equals("toggletrail"))) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.WHITE + "Unknown command. View your Character Journal's Index for a list of commands.");
                }
            } else if (!(s.equals("roll") || s.equals("gl") || s.equals("toggle") || s.equals("toggles") || s.equals("togglepvp") || s.equals("togglechaos") || s.equals("toggledebug") || s.equals("debug") || s.equals("toggleff") || s.equals("add") || s.equals("del") || s.equals("delete") || s.equals("message") || s.equals("msg") || s.equals("m") || s.equals("whisper") || s.equals("w") || s.equals("tell") || s.equals("t") || s.equals("reply") || s.equals("r") || s.equals("logout") || s.equals("sync") || s.equals("reboot") || s.equals("pinvite") || s.equals("paccept") || s.equals("pquit") || s.equals("pkick") || s.equals("pdecline") || s.equals("p"))) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.WHITE + "Unknown command. View your Character Journal's Index for a list of commands.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatTabComplete(PlayerChatTabCompleteEvent e) {
        Player p = e.getPlayer();
        if (e.getChatMessage() != null) {
            p.closeInventory();
            p.performCommand("gl " + e.getChatMessage());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (!e.isCancelled()) {
            Player p = e.getPlayer();
            e.setCancelled(true);
            String message = e.getMessage();
            if (message.contains("@i@") && p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                this.sendShowString(p, p.getInventory().getItemInMainHand(), "", message, p);
                ArrayList<Player> to_send = new ArrayList<Player>();
                for (Player pl2 : Bukkit.getServer().getOnlinePlayers()) {
                    if (Vanish.vanished.contains(pl2.getName().toLowerCase()) || pl2 == null || pl2 == p || pl2.getLocation().distance(p.getLocation()) >= 50.0)
                        continue;
                    to_send.add(pl2);
                }
                if (to_send.size() <= 0) {
                    p.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "No one heard you.");
                } else {
                    for (Player pl2 : to_send) {
                        this.sendShowString(p, p.getInventory().getItemInMainHand(), "", message, pl2);
                    }
                }
                for (Player op : Bukkit.getServer().getOnlinePlayers()) {
                    if (!op.isOp() || !Vanish.vanished.contains(op.getName().toLowerCase()) || op == p)
                        continue;
                    this.sendShowString(p, p.getInventory().getItemInMainHand(), "", message, op);
                }
                PracticeServer.log.info(String.valueOf(p.getDisplayName()) + ": " + ChatColor.WHITE + message);
            } else {
                p.sendMessage(String.valueOf(p.getDisplayName()) + ": " + ChatColor.WHITE + message);
                ArrayList<Player> to_send = new ArrayList<Player>();
                for (Player pl3 : Bukkit.getServer().getOnlinePlayers()) {
                    if (Vanish.vanished.contains(pl3.getName().toLowerCase()) || pl3 == null || pl3 == p || pl3.getLocation().distance(p.getLocation()) >= 50.0)
                        continue;
                    to_send.add(pl3);
                }
                if (to_send.size() <= 0) {
                    p.sendMessage(ChatColor.GRAY.toString() + ChatColor.ITALIC + "No one heard you.");
                } else {
                    for (Player pl3 : to_send) {
                        pl3.sendMessage(String.valueOf(p.getDisplayName()) + ": " + ChatColor.WHITE + message);
                    }
                }
                for (Player op : Bukkit.getServer().getOnlinePlayers()) {
                    if (!op.isOp() || !Vanish.vanished.contains(op.getName().toLowerCase()) || op == p)
                        continue;
                    op.sendMessage(String.valueOf(p.getDisplayName()) + ": " + ChatColor.WHITE + message);
                }
                PracticeServer.log.info(String.valueOf(p.getDisplayName()) + ": " + ChatColor.WHITE + message);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public void sendShowString(Player sender, ItemStack is, String prefix, String message, Player p) {
        if (message.contains("@i@") && is != null && is.getType() != Material.AIR) {
            String aprefix = prefix;
            String[] split = message.split("@i@");
            String after = "";
            String before = "";
            if (split.length > 0)
                before = split[0];
            if (split.length > 1)
                after = split[1];

            ItemStack stack = is;

            List<String> hoveredChat = new ArrayList<>();
            ItemMeta meta = stack.getItemMeta();
            hoveredChat.add((meta.hasDisplayName() ? meta.getDisplayName() : stack.getType().name()));
            if (meta.hasLore())
                hoveredChat.addAll(meta.getLore());
             JSONMessage normal = new JSONMessage(aprefix);
            before = sender.getDisplayName() + ": " + ChatColor.WHITE + before;
            normal.addText(before + "");
            normal.addHoverText(hoveredChat, ChatColor.BOLD + ChatColor.UNDERLINE.toString() + "SHOW");
            normal.addText(after);
			PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(normal.toString()));
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }
    }
}

