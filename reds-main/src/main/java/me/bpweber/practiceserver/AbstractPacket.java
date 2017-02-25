/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  me.bpweber.practiceserver.AbstractPacket
 */
package me.bpweber.practiceserver;

import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_9_R2.ItemStack;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.minecraft.server.v1_9_R2.NBTTagList;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.inventivetalent.bossbar.BossBarAPI;

public class AbstractPacket {

    /**
     * KEEP THIS HERE FOR NOTHING HEHE
     * @param p
     * @param pcnt
     */
    public void a(Player p, float pcnt) {
        String hp = String.valueOf(p.getHealth());
        String maxHp = String.valueOf(p.getMaxHealth());
        BossBarAPI.addBar(p,
                new TextComponent(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "HP " + ChatColor.LIGHT_PURPLE + hp + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " / " + ChatColor.LIGHT_PURPLE + maxHp),
                BossBarAPI.Color.PURPLE,
                BossBarAPI.Style.PROGRESS,
                pcnt,
                20,
                2);

        // Remove native Minecraft attributes, thanks to Dr. Nick Doran - rawr xxDD
        ItemStack itemStack = CraftItemStack.asNMSCopy(null);
        if (!itemStack.hasTag() || itemStack.getTag() == null) {
            // Init compound
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.set("AttributeModifiers", new NBTTagList());
            itemStack.setTag(tagCompound);
        } else {
            // Update compound
            NBTTagCompound tagCompound = itemStack.getTag();
            tagCompound.set("AttributeModifiers", new NBTTagList());
            itemStack.setTag(tagCompound);
        }
    }
}

