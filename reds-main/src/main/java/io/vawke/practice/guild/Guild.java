package io.vawke.practice.guild;

import com.google.common.collect.Lists;
import io.vawke.practice.Game;
import io.vawke.practice.prompt.StringUtil;
import io.vawke.practice.util.IOUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by Giovanni on 11-2-2017.
 */
@Getter
public class Guild implements Domesticated {

    private String dataId;

    private String name;
    private String tag;

    @Setter
    private String motd;

    private UUID ownerUniqueId;
    private String ownerName;

    private List<UUID> members;
    private List<UUID> officers;

    private HashMap<UUID, String> invitationCache;

    public Guild(String name, String tag, Player owner) {
        this.name = name;
        this.dataId = name;
        this.tag = tag;
        this.ownerUniqueId = owner.getUniqueId();
        this.ownerName = owner.getName();
        this.members = Lists.newArrayList();
        this.officers = Lists.newArrayList();

        this.members.add(this.ownerUniqueId);
    }

    public void sendGuildMessage(String sender, String message) {
        String format = ChatColor.DARK_AQUA.toString() + "<" + ChatColor.BOLD + this.tag + ChatColor.DARK_AQUA + ">" + ChatColor.GRAY + " " + sender + ": " + ChatColor.GRAY + message;
        this.members.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()).forEach(uuid -> {
            Bukkit.getPlayer(uuid).sendMessage(format);
        });
    }

    public void sendAlert(String message) {
        String format = ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + this.tag + ChatColor.DARK_AQUA + "> " + ChatColor.DARK_AQUA + message;
        this.members.stream().filter(uuid -> Bukkit.getPlayer(uuid) != null && Bukkit.getPlayer(uuid).isOnline()).forEach(uuid -> {
            Bukkit.getPlayer(uuid).sendMessage(format);
        });
    }

    public void displayMotd(Player player) {
        if (this.members.contains(player.getUniqueId())) {
            if (this.motd != null && !this.motd.equals("") && motd.length() > 0) {
                player.sendMessage(ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + this.tag + ChatColor.DARK_AQUA + "> " + ChatColor.BOLD + "MOTD: " + ChatColor.DARK_AQUA + this.motd);
            } else {
                player.sendMessage(ChatColor.DARK_AQUA + "<" + ChatColor.BOLD + this.tag + ChatColor.DARK_AQUA + "> No message of the day set. Use /gmotd <motd> to set one!");
            }
        }
    }

    public void showGuildInfo(Player player) {
        if (this.members.contains(player.getUniqueId())) {
            StringUtil.sendCenteredMessage(player, "&7*** &3&lGuild Info &7***");
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "Guild Name: " + ChatColor.WHITE.toString() + ChatColor.BOLD + this.name);
            player.sendMessage(ChatColor.GRAY + "Guild Tag: " + ChatColor.DARK_AQUA + "[" + ChatColor.GRAY + this.tag + ChatColor.DARK_AQUA + "]");
            player.sendMessage(ChatColor.GRAY + "Guild Owner: " + ChatColor.WHITE.toString() + ChatColor.BOLD + this.ownerName);
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "Guild Officers: " + ChatColor.WHITE.toString() + ChatColor.BOLD + (officers.size() == 0 ? "None" : officers.size()));
            player.sendMessage(ChatColor.GRAY + "Guild Members: " + ChatColor.WHITE.toString() + ChatColor.BOLD + (members.size() == 0 ? "None" : members.size()));
            player.sendMessage("");
            player.sendMessage(ChatColor.GRAY + "Message of the Day: " + ChatColor.WHITE.toString() + ChatColor.BOLD + (this.motd == null ? "None" : this.motd));
        }
    }

    public void checkOwnerJoin(Player player) {
        // Fix player name changes
        if (this.ownerUniqueId.equals(player.getUniqueId())) {
            if (!this.ownerName.equals(player.getName())) {
                this.ownerName = player.getName();
            }
        }
    }

    public void acceptJoin(Player player) {
        // Player might accept a guild invitation of which the guild has been disbanded
        if (Game.getGame().directGuildRegistry().exists(this.dataId)) {
            this.members.add(player.getUniqueId());
            String sender = this.invitationCache.get(player.getUniqueId());

            player.sendMessage(ChatColor.DARK_AQUA + "You have joined '" + ChatColor.BOLD + this.name + "'" + ChatColor.DARK_AQUA + ".");
            player.sendMessage(ChatColor.GRAY + "To chat with your new guild, use " + ChatColor.BOLD + "/g" + ChatColor.GRAY + " OR " + ChatColor.BOLD + " /g <message>");

            this.sendAlert(player.getName() + ChatColor.GRAY.toString() + " has " +
                    ChatColor.UNDERLINE + "joined" + ChatColor.GRAY + " your guild." + (sender != null ? " [INVITE: " + ChatColor.ITALIC + sender + ChatColor.GRAY + "]" : ""));
        } else {
            player.sendMessage(ChatColor.RED + "Guild no longer exists!");
        }
    }

    public void register() {
        if (!Game.getGame().directGuildRegistry().exists(this.dataId)) {
            Game.getGame().directGuildRegistry().register(this);
        }
    }

    public boolean tryInvite(Player executor, Player player) {
        if (Game.getGame().directGuildRegistry().hasInvitation(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + player.getName() + ChatColor.RED + " already has a pending guild invitation!");
            return false;
        }
        if (Game.getGame().directGuildRegistry().hasGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + player.getName() + ChatColor.RED + " is already in a guild!");
            return false;
        }
        return true;
    }

    @Override
    public void save() {
        IOUtil.getUtil().writeJson(this.serialize(), Game.getPracticeServer().getDataFolder() + "/guilds/" + this.dataId + ".json");
    }

    private String serialize() {
        // Remove pending invitations
        this.invitationCache.clear();
        return IOUtil.getUtil().getGson().toJson(this);
    }
}
