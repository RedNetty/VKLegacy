package me.kayaba.guilds.impl.basic;

import me.bpweber.practiceserver.*;
import me.kayaba.guilds.api.basic.*;
import me.kayaba.guilds.enums.*;

import java.util.*;

public class GRankImpl extends AbstractResource implements GRank {
    private int id;
    private String name;
    private Guild guild;
    private boolean def;
    private boolean clone;
    private final List<GuildPermission> permissions = new ArrayList<>();
    private final List<GPlayer> members = new ArrayList<>();


    public GRankImpl(UUID uuid) {
        super(uuid);
    }


    public GRankImpl(String name) {
        this(UUID.randomUUID());
        this.name = name;
    }


    public GRankImpl(final GRank rank) {
        this(rank.getName());
        setClone(rank.isGeneric());
        setName(rank.getName());
        setPermissions(rank.getPermissions());
    }

    @Override
    public int getId() {
        if (id <= 0) {
            throw new UnsupportedOperationException("This rank might have been loaded from FLAT and has 0 (or negative) ID");
        }

        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<GPlayer> getMembers() {
        return members;
    }

    @Override
    public List<GuildPermission> getPermissions() {
        return permissions;
    }

    @Override
    public Guild getGuild() {
        return guild;
    }

    @Override
    public boolean isClone() {
        return clone;
    }

    @Override
    public boolean isDefault() {
        return def;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        setChanged();
    }

    @Override
    public void setPermissions(List<GuildPermission> permissions) {
        this.permissions.clear();
        this.permissions.addAll(permissions);
        setChanged();
    }

    @Override
    public void setGuild(Guild guild) {
        if (guild != null) {
            guild.addRank(this);
        }

        this.guild = guild;
        setChanged();
    }

    @Override
    public void setClone(boolean clone) {
        this.clone = clone;
        setChanged();
    }

    @Override
    public void setDefault(boolean def) {
        this.def = def;
        setChanged();
    }

    @Override
    public void addPermission(GuildPermission permission) {
        if (!permissions.contains(permission)) {
            permissions.add(permission);
            setChanged();
        }
    }

    @Override
    public void addPermissions(List<GuildPermission> list) {
        for (GuildPermission guildPermission : list) {
            addPermission(guildPermission);
        }
    }

    @Override
    public void addMember(GPlayer nPlayer) {
        if (!members.contains(nPlayer)) {
            members.add(nPlayer);
            setChanged();
        }
    }

    @Override
    public void removePermission(GuildPermission permission) {
        if (permissions.contains(permission)) {
            permissions.remove(permission);
            setChanged();
        }
    }

    @Override
    public void removePermission(List<GuildPermission> list) {
        for (GuildPermission guildPermission : list) {
            removePermission(guildPermission);
        }
    }

    @Override
    public void removeMember(GPlayer nPlayer) {
        if (members.contains(nPlayer)) {
            members.remove(nPlayer);
            setChanged();
        }
    }

    @Override
    public boolean hasPermission(GuildPermission permission) {
        return permissions.contains(permission);
    }

    @Override
    public boolean isGeneric() {
        return this instanceof GenericRankImpl;
    }

    @Override
    public void delete() {
        setDefault(false);
        for (GPlayer nPlayer : new ArrayList<>(getMembers())) {
            nPlayer.setGuildRank(getGuild().getDefaultRank());
        }

        PracticeServer.getInstance().getRankManager().delete(this);
    }
}
