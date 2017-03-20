package me.kayaba.guilds.manager;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.impl.basic.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;

import java.util.*;

public class GroupManager {
    private static final PracticeServer plugin = PracticeServer.getInstance();
    private final Map<String, Group> groups = new HashMap<>();


    public void load() {
        groups.clear();
        Set<String> groupsNames = new HashSet<String>();
        groupsNames.add("default");
        groupsNames.add("admin");

        for (String groupName : groupsNames) {
            groups.put(groupName, new GroupImpl(groupName));
        }
    }


    public static Group getGroup(Player player) {
        Map<String, Group> groups = plugin.getGroupManager().getGroups();
        String groupName = "default";

        if (player == null) {
            return getGroup(groupName);
        }

        if (player.hasPermission("PracticeServer.group.admin")) {
            return getGroup("admin");
        }

        for (String name : groups.keySet()) {
            if (player.hasPermission("PracticeServer.group." + name) && !name.equalsIgnoreCase("default")) {
                groupName = name;
                break;
            }
        }

        return getGroup(groupName);
    }


    public static Group getGroup(CommandSender sender) {
        if (sender instanceof Player) {
            return getGroup((Player) sender);
        } else {
            return getGroup("admin");
        }
    }


    public static Group getGroup(String groupName) {
        Map<String, Group> groups = plugin.getGroupManager().getGroups();

        if (groups.containsKey(groupName)) {
            return groups.get(groupName);
        }

        return null;
    }


    public Map<String, Group> getGroups() {
        return groups;
    }
}
