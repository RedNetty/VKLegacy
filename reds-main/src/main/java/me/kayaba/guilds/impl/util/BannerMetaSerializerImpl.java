package me.kayaba.guilds.impl.util;

import me.kayaba.guilds.api.util.*;
import org.apache.commons.lang.*;
import org.bukkit.*;
import org.bukkit.block.banner.*;
import org.bukkit.inventory.meta.*;

public class BannerMetaSerializerImpl implements BannerMetaSerializer {
    @Override
    public String serialize(BannerMeta bannerMeta) {
        if (bannerMeta == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        builder.append((bannerMeta.getBaseColor() == null ? DyeColor.BLACK : bannerMeta.getBaseColor()).name());

        if (bannerMeta.numberOfPatterns() > 0) {
            builder.append(':');
        }

        int index = 1;
        for (Pattern pattern : bannerMeta.getPatterns()) {
            builder.append(pattern.getColor().name());
            builder.append('-');
            builder.append(pattern.getPattern().getIdentifier());

            if (index < bannerMeta.numberOfPatterns()) {
                builder.append("|");
            }

            index++;
        }

        return builder.toString();
    }

    @Override
    public BannerMeta deserialize(String string) {
        BannerMeta meta = (BannerMeta) Bukkit.getItemFactory().getItemMeta(Material.BANNER);

        if (string == null || string.isEmpty()) {
            return meta;
        }

        String baseColorString;
        String patternsString;

        if (StringUtils.contains(string, ':')) {
            String[] baseSplit = StringUtils.split(string, ':');
            baseColorString = baseSplit[0];
            patternsString = baseSplit[1];
        } else {
            baseColorString = string;
            patternsString = "";
        }

        meta.setBaseColor(DyeColor.valueOf(baseColorString));

        if (!patternsString.isEmpty()) {
            String[] patternsSplit;

            if (StringUtils.contains(patternsString, '|')) {
                patternsSplit = StringUtils.split(patternsString, '|');
            } else {
                patternsSplit = new String[]{
                        patternsString
                };
            }

            for (String patternString : patternsSplit) {
                String[] patternSplit = StringUtils.split(patternString, '-');

                meta.addPattern(new Pattern(DyeColor.valueOf(patternSplit[0]), PatternType.getByIdentifier(patternSplit[1])));
            }
        }

        return meta;
    }
}
