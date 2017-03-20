/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  me.bpweber.practiceserver.AbstractPacket
 */
package me.bpweber.practiceserver;

import net.minecraft.server.v1_9_R2.*;
import org.bukkit.craftbukkit.v1_9_R2.inventory.*;
import org.bukkit.entity.*;

public class AbstractPacket {

    /**
     * KEEP THIS HERE FOR NOTHING HEHE
     *
     * @param p
     * @param pcnt
     */
    public void a(Player p, float pcnt) {
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

