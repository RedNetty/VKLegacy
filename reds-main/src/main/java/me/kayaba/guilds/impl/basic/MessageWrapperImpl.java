package me.kayaba.guilds.impl.basic;

import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.enums.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import me.kayaba.guilds.util.*;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.inventory.*;

import java.util.*;

public class MessageWrapperImpl extends AbstractVarKeyApplicable<MessageWrapper> implements MessageWrapper {
    private String path;
    private final Set<Flag> flags = new HashSet<>();


    public MessageWrapperImpl(String path, Flag... flags) {
        this.path = path;

        for (Flag flag : flags) {
            if (flag == Flag.LIST) {
                this.flags.add(Flag.NOPREFIX);
            }
        }

        Collections.addAll(this.flags, flags);
    }


    public MessageWrapperImpl(String path) {
        this(path, new Flag[0]);
    }


    public MessageWrapperImpl(Flag... flags) {
        this(null, flags);
    }


    public MessageWrapperImpl(MessageWrapper wrapper) {
        this(wrapper.getPath(), wrapper.getFlags().toArray(new Flag[wrapper.getFlags().size()]));
        vars(wrapper.getVars());
    }

    @Override
    public Set<Flag> getFlags() {
        return flags;
    }

    @Override
    public boolean hasFlag(Flag flag) {
        return flags.contains(flag);
    }

    @Override
    public boolean getTitle() {
        return hasFlag(Flag.TITLE);
    }

    @Override
    public String getPath() {
        if (path == null) {
            throw new IllegalArgumentException("Path has not been set!");
        }

        return path;
    }

    @Override
    public String getName() {
        return StringUtils.replace(path, ".", "_").toUpperCase();
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean isPrefix() {
        return !hasFlag(Flag.NOPREFIX);
    }

    @Override
    public boolean isEmpty() {
        return get().equals("none");
    }

    @Override
    public void send(CommandSender sender) {
        if (hasFlag(Flag.LIST)) {
            MessageManager.sendMessagesList(sender, this);
        } else {
            MessageManager.sendMessagesMsg(sender, this);
        }
    }

    @Override
    public void send(GPlayer nPlayer) {
        if (nPlayer.isOnline()) {
            send(nPlayer.getPlayer());
        }
    }

    @Override
    public MessageWrapper prefix(boolean prefix) {
        if (prefix) {
            if (flags.contains(Flag.NOPREFIX)) {
                flags.remove(Flag.NOPREFIX);
            }
        } else {
            flags.add(Flag.NOPREFIX);
        }

        return this;
    }

    @Override
    public void broadcast(Guild guild) {
        MessageManager.broadcast(guild, this);
    }

    @Override
    public void broadcast() {
        MessageManager.broadcast(this);
    }

    @Override
    public void broadcast(Permission permission) {
        MessageManager.broadcast(this, permission);
    }

    @Override
    public String get() {
        return MessageManager.replaceVarKeyMap(MessageManager.getMessagesString(this), vars, !hasFlag(Flag.NOAFTERVARCOLOR));
    }

    @Override
    public void set(String string) {
        MessageManager.set(this, string);
    }

    @Override
    public void set(List<String> list) {
        MessageManager.set(this, list);
    }

    @Override
    public ItemStack getItemStack() {
        return ItemStackUtils.stringToItemStack(get());
    }

    @Override
    public List<String> getList() {
        return me.kayaba.guilds.util.StringUtils.fixColors(MessageManager.replaceVarKeyMap(MessageManager.getMessages().getStringList(getPath()), vars, !hasFlag(Flag.NOAFTERVARCOLOR)));
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        return MessageManager.getMessages().getConfigurationSection(getParentPath());
    }

    @Override
    public List<MessageWrapper> getNeighbours() {
        List<MessageWrapper> list = new ArrayList<>();
        String parentPath = getParentPath();
        for (String key : getConfigurationSection().getKeys(false)) {
            key = parentPath + "." + key;

            if (!key.equals(getPath())) {
                list.add(Message.fromPath(key).prefix(isPrefix()));
            }
        }

        return list;
    }

    @Override
    public String getParentPath() {
        String[] split = StringUtils.split(getPath(), ".");
        return StringUtils.removeEnd(getPath(), "." + split[split.length - 1]);
    }

    @Override
    public ChatBroadcast newChatBroadcast() {
        return new ChatBroadcastImpl(this);
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public MessageWrapper clone() {
        return new MessageWrapperImpl(this);
    }
}
