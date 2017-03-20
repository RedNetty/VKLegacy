package me.kayaba.guilds.util;


import me.kayaba.guilds.api.util.*;
import me.kayaba.guilds.impl.util.*;
import me.kayaba.guilds.manager.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public final class BannerUtils {
    private static final BannerMetaSerializer serializer = new BannerMetaSerializerImpl();

    private BannerUtils() {

    }


    public static BannerMetaSerializer getSerializer() {
        return serializer;
    }


    public static ItemStack randomBannerItemStack() {
        if (ConfigManager.getServerVersion().isOlderThan(ConfigManager.ServerVersion.MINECRAFT_1_8_R2)) {
            return null;
        }

        ItemStack itemStack = new ItemStack(Material.BANNER);
        itemStack.setItemMeta(getRandomMeta());
        return itemStack;
    }


    public static BannerMeta getRandomMeta() {
        if (ConfigManager.getServerVersion().isOlderThan(ConfigManager.ServerVersion.MINECRAFT_1_8_R2)) {
            return null;
        }

        BannerMeta meta = (BannerMeta) Bukkit.getItemFactory().getItemMeta(Material.BANNER);
        meta.setBaseColor(randomDyeColor());

        for (int i = NumberUtils.randInt(0, PatternType.values().length) + 2; i > 0; i--) {
            meta.addPattern(new Pattern(randomDyeColor(), randomPatternType()));
        }

        return meta;
    }


    protected static PatternType randomPatternType() {
        return PatternType.values()[NumberUtils.randInt(0, PatternType.values().length - 1)];
    }


    protected static DyeColor randomDyeColor() {
        return DyeColor.values()[NumberUtils.randInt(0, DyeColor.values().length - 1)];
    }


    public static BannerMeta deserialize(String string) {
        if (ConfigManager.getServerVersion().isOlderThan(ConfigManager.ServerVersion.MINECRAFT_1_8_R2)) {
            return null;
        }

        return getSerializer().deserialize(string);
    }


    public static String serialize(Banner banner) {
        return getSerializer().serialize(getBannerMeta(banner));
    }


    public static String serialize(BannerMeta bannerMeta) {
        if (ConfigManager.getServerVersion().isOlderThan(ConfigManager.ServerVersion.MINECRAFT_1_8_R2)) {
            return "";
        }

        return getSerializer().serialize(bannerMeta);
    }


    public static Banner applyMeta(Banner banner, BannerMeta meta) {
        banner.setBaseColor(meta.getBaseColor());
        banner.setPatterns(meta.getPatterns());
        return banner;
    }


    public static ItemStack applyMeta(ItemStack itemStack, BannerMeta bannerMeta) {
        if (itemStack.getType() != Material.SHIELD && itemStack.getType() != Material.BANNER) {
            throw new IllegalArgumentException("Passed ItemStack is not a shield nor a banner");
        }

        ItemMeta meta = itemStack.getItemMeta();
        BlockStateMeta blockStateMeta = (BlockStateMeta) meta;
        Banner banner = (Banner) blockStateMeta.getBlockState();
        applyMeta(banner, bannerMeta);
        banner.update();
        blockStateMeta.setBlockState(banner);
        itemStack.setItemMeta(blockStateMeta);
        return itemStack;
    }


    public static BannerMeta getBannerMeta(Banner banner) {
        if (ConfigManager.getServerVersion().isOlderThan(ConfigManager.ServerVersion.MINECRAFT_1_8_R2)) {
            return null;
        }

        BannerMeta meta = (BannerMeta) Bukkit.getItemFactory().getItemMeta(Material.BANNER);

        meta.setBaseColor(banner.getBaseColor());
        for (Pattern pattern : banner.getPatterns()) {
            meta.addPattern(pattern);
        }

        return meta;
    }
}
