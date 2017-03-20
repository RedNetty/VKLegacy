package me.kayaba.guilds.api.util;

import org.bukkit.inventory.meta.*;

public interface BannerMetaSerializer {

    String serialize(BannerMeta bannerMeta);


    BannerMeta deserialize(String string);
}
