package me.kayaba.guilds.util;

import me.bpweber.practiceserver.*;
import org.bukkit.block.*;
import org.bukkit.metadata.*;

public class Meta {
    private static final PracticeServer plugin = PracticeServer.getInstance();


    public static void setMetadata(Metadatable obj, String key, Object value) {
        obj.setMetadata(key, new FixedMetadataValue(plugin, value));
    }


    public static MetadataValue getMetadata(Metadatable obj, String key) {
        for (MetadataValue value : obj.getMetadata(key)) {
            if (value.getOwningPlugin().getDescription().getName().equals(plugin.getDescription().getName())) {
                return value;
            }
        }

        return null;
    }


    public static void removeMetadata(Metadatable obj, String key) {
        obj.removeMetadata(key, plugin);
    }


    public static void protect(Block block) {
        setMetadata(block, "protected", true);
    }


    public static void unprotect(Block block) {
        removeMetadata(block, "protected");
    }


    public static boolean isProtected(Block block) {
        MetadataValue metadataValue = getMetadata(block, "protected");
        return metadataValue != null && metadataValue.asBoolean();
    }
}
