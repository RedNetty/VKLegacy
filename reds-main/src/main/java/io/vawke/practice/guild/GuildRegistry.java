package io.vawke.practice.guild;

import io.vawke.practice.Game;
import io.vawke.practice.data.DataRegistry;
import io.vawke.practice.data.Registry;
import io.vawke.practice.data.RegistryObject;
import io.vawke.practice.util.IOUtil;
import org.apache.commons.io.IOUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Giovanni on 12-2-2017.
 */
public class GuildRegistry implements Registry {

    private DataRegistry<Guild> guildDataRegistry;

    @Override
    public void init() {
        this.guildDataRegistry = new DataRegistry<>();
    }

    public void register(Guild guild) {
        this.guildDataRegistry.plus(guild);
    }

    public boolean tryName(Player player, String dataId) {
        if (this.guildDataRegistry.exists(dataId)) {
            player.sendMessage(ChatColor.RED + "A guild with the name " + ChatColor.UNDERLINE + dataId + ChatColor.RED + " already exists!");
            return false;
        }
        if (this.filteredWord(dataId)) {
            player.sendMessage(ChatColor.RED + "The guild name " + ChatColor.UNDERLINE + dataId + ChatColor.RED + " is not allowed!");
            return false;
        }
        if (this.hasGuild(player.getUniqueId())) {
            player.sendMessage(ChatColor.RED + "You are already in a guild!");
            return false;
        }
        return true;
    }

    public boolean hasGuild(UUID uniqueId) {
        for (Guild guild : this.guildDataRegistry.all()) {
            if (guild.getMembers().contains(uniqueId)) {
                return true;
            }
        }
        return false;
    }

    private boolean filteredWord(String dataId) {
        for (String string : new String[]{"fuck, anal, cancer, anus, faggot, autist, cunt, retard, fag, nigger, negro, nig, penis, cock, dick, cuck, ass"}) {
            if (dataId.toLowerCase().contains(string))
                return true;
        }
        return false;
    }

    public Guild ownerGuild(Player player) {
        for (Guild guild : this.guildDataRegistry.all()) {
            if (guild.getOwnerUniqueId().equals(player.getUniqueId())) {
                return guild;
            }
        }
        return null;
    }

    public Guild playerGuild(Player player) {
        for (Guild guild : this.guildDataRegistry.all()) {
            if (guild.getMembers().contains(player.getUniqueId())) {
                return guild;
            }
        }
        return null;
    }

    public void quitAndSave() {
        if (this.guildDataRegistry.size() > 0) {
            this.guildDataRegistry.all().forEach(RegistryObject::save);
        }
    }

    public boolean hasInvitation(UUID uniqueId) {
        for (Guild guild : this.guildDataRegistry.all()) {
            if (guild.getInvitationCache().containsKey(uniqueId)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasInvitationTo(UUID uniqueId, Guild guild) {
        return guild.getInvitationCache().containsKey(uniqueId);
    }

    public Guild getInvitedGuildOf(UUID uniqueId) {
        for (Guild guild : this.guildDataRegistry.all()) {
            if (guild.getInvitationCache().containsKey(uniqueId)) {
                return guild;
            }
        }
        return null;
    }

    public boolean exists(String dataId) {
        return this.guildDataRegistry.exists(dataId);
    }

    // IO
    public void readFromPath(String filePath) {
        try {
            List<File> touchableFiles = Files.walk(Paths.get(filePath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            if (!touchableFiles.isEmpty()) {
                this.continueRead(touchableFiles);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void continueRead(List<File> fileList) {
        fileList.stream().filter(file -> file.getName().toLowerCase().endsWith(".json")).forEach(file -> {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                this.register(IOUtil.getUtil().getGson().fromJson(IOUtils.toString(fileInputStream, "UTF-8"), Guild.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Game.log("&aRegistered &e&l" + this.guildDataRegistry.all().size() + " &aguilds from the guildRoot!");
    }
}
